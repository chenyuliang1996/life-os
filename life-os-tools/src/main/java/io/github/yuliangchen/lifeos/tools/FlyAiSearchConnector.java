package io.github.yuliangchen.lifeos.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agentscope.core.tool.mcp.McpClientBuilder;
import io.agentscope.core.tool.mcp.McpClientWrapper;
import io.github.yuliangchen.lifeos.domain.model.ConnectorStatus;
import io.github.yuliangchen.lifeos.domain.model.ToolResult;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
/**
 * FlyAI 搜索连接器：支持 CLI / MCP / Hybrid 模式并带回退。
 * FlyAI search connector supporting CLI, MCP, and hybrid fallback modes.
 */
public class FlyAiSearchConnector implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(FlyAiSearchConnector.class);
    private static final List<String> MODE_CLI_FIRST = List.of("cli", "mcp");
    private static final List<String> MODE_MCP_FIRST = List.of("mcp", "cli");

    private final boolean enabled;
    private final String mode;

    private final String endpoint;
    private final String transport;
    private final String configuredToolName;
    private final String authHeaderName;
    private final String authHeaderValue;
    private final Duration mcpTimeout;

    private final boolean cliEnabled;
    private final String cliCommand;
    private final String cliWorkingDirectory;
    private final Duration cliTimeout;
    private final boolean execEnabled;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private volatile McpClientWrapper clientWrapper;
    private volatile String resolvedToolName;

    /**
     * 从环境配置初始化 FlyAI 参数。
     * Initializes FlyAI connector configuration from environment properties.
     */
    public FlyAiSearchConnector(Environment environment) {
        this.enabled = Boolean.parseBoolean(environment.getProperty("lifeos.flyai.enabled", "false"));
        this.mode = environment.getProperty("lifeos.flyai.mode", "hybrid");

        this.endpoint = environment.getProperty("lifeos.flyai.endpoint", "");
        this.transport = environment.getProperty("lifeos.flyai.transport", "sse");
        this.configuredToolName = environment.getProperty("lifeos.flyai.tool-name", "");
        this.authHeaderName = environment.getProperty("lifeos.flyai.auth-header-name", "");
        this.authHeaderValue = environment.getProperty("lifeos.flyai.auth-header-value", "");
        this.mcpTimeout = Duration.ofSeconds(Integer.parseInt(environment.getProperty("lifeos.flyai.timeout-seconds", "20")));

        this.cliEnabled = Boolean.parseBoolean(environment.getProperty("lifeos.flyai.cli-enabled", "false"));
        this.cliCommand = environment.getProperty("lifeos.flyai.cli-command", "flyai");
        this.cliWorkingDirectory = environment.getProperty("lifeos.flyai.cli-working-directory", "");
        this.cliTimeout = Duration.ofSeconds(Integer.parseInt(environment.getProperty("lifeos.flyai.cli-timeout-seconds", "18")));
        this.execEnabled = Boolean.parseBoolean(environment.getProperty("lifeos.security.exec-enabled", "false"));
    }

    /**
     * 兼容入口：默认按通用旅行查询执行。
     * Backward-compatible entry using generic travel route.
     */
    public ToolResult search(String topic, String locale) {
        TravelIntentRoute defaultRoute = TravelIntentRoute.travel(
                "travel-generic",
                "ai-search",
                List.of("keyword-search"),
                "Default generic travel route."
        );
        ToolResult live = searchTravelIntent(topic, locale, Map.of(), defaultRoute);
        if (live != null) {
            return live;
        }
        return fallbackSearch(topic, locale);
    }

    /**
     * 按意图路由执行 FlyAI 搜索，按配置模式选择 CLI/MCP。
     * Executes FlyAI search based on route and configured mode order.
     */
    public ToolResult searchTravelIntent(String topic,
                                         String locale,
                                         Map<String, String> context,
                                         TravelIntentRoute route) {
        TravelIntentRoute safeRoute = route == null
                ? TravelIntentRoute.travel("travel-generic", "ai-search", List.of("keyword-search"), "Route defaulted.")
                : route;

        Map<String, String> safeContext = context == null ? Map.of() : context;
        ToolResult live = searchByMode(topic, locale, safeContext, safeRoute);
        if (live == null) {
            return null;
        }
        return enrichRouteMetadata(live, safeRoute);
    }

    /**
     * 直接走 MCP 实时搜索（供 Hybrid 模式内部调用）。
     * Executes live MCP search directly.
     */
    public ToolResult searchLive(String topic, String locale, Map<String, String> context) {
        if (!isMcpReady()) {
            return null;
        }

        try {
            McpClientWrapper wrapper = getOrCreateClient();
            String toolName = getOrResolveToolName(wrapper);
            if (!StringUtils.hasText(toolName)) {
                return null;
            }

            String response = callSearchTool(wrapper, toolName, topic, locale, context == null ? Map.of() : context);
            if (!StringUtils.hasText(response)) {
                return null;
            }

            return new ToolResult("search", response, metadata(toolName));
        } catch (Exception exception) {
            log.warn("FlyAI MCP search failed. Falling back to next provider.", exception);
            return null;
        }
    }

    /**
     * 返回连接器状态快照。
     * Returns connector status snapshot for operator dashboards.
     */
    public ConnectorStatus connectorStatus() {
        if (!enabled) {
            return new ConnectorStatus("flyai-search", false, "FlyAI search is disabled.");
        }

        String normalizedMode = normalizedMode();
        if ("cli".equals(normalizedMode)) {
            boolean ready = isCliReady();
            return new ConnectorStatus(
                    "flyai-search",
                    ready,
                    ready
                            ? "FlyAI search runs in CLI mode."
                            : "FlyAI CLI mode selected but CLI is not ready (check exec permission, command, and enable flag)."
            );
        }
        if ("mcp".equals(normalizedMode)) {
            boolean ready = isMcpReady();
            return new ConnectorStatus(
                    "flyai-search",
                    ready,
                    ready
                            ? "FlyAI search runs in MCP mode."
                            : "FlyAI MCP mode selected but endpoint is not configured."
            );
        }

        boolean cliReady = isCliReady();
        boolean mcpReady = isMcpReady();
        return new ConnectorStatus(
                "flyai-search",
                cliReady || mcpReady,
                "FlyAI hybrid mode (" + normalizedMode + ") readiness: cli=" + cliReady + ", mcp=" + mcpReady + "."
        );
    }

    @Override
    /**
     * 关闭连接器时释放 MCP 客户端。
     * Closes MCP client wrapper during connector shutdown.
     */
    public void destroy() {
        if (clientWrapper != null) {
            clientWrapper.close();
        }
    }

    /**
     * 构建种子回退结果。
     * Builds seeded fallback result when live providers are unavailable.
     */
    public ToolResult fallbackSearch(String topic, String locale) {
        String lowerLocale = locale == null ? "en-us" : locale.toLowerCase(Locale.ROOT);
        String summary = lowerLocale.startsWith("zh")
                ? "未命中实时搜索，已使用内置旅行知识提供\"" + topic + "\"的低疲劳行程建议。"
                : "Live search was unavailable, so seeded travel knowledge generated a low-fatigue itinerary suggestion for \"" + topic + "\".";
        return new ToolResult(
                "search",
                summary,
                Map.of(
                        "provider", "seeded-fallback",
                        "connector", "search",
                        "mode", "fallback"
                )
        );
    }

    /**
     * 按模式顺序执行 FlyAI 实时搜索。
     * Executes live FlyAI search in configured mode order.
     */
    private ToolResult searchByMode(String topic,
                                    String locale,
                                    Map<String, String> context,
                                    TravelIntentRoute route) {
        for (String candidateMode : modeOrder()) {
            if ("cli".equals(candidateMode)) {
                ToolResult cliResult = searchViaCli(topic, locale, context, route);
                if (cliResult != null) {
                    return cliResult;
                }
            } else {
                ToolResult mcpResult = searchLive(topic, locale, contextWithRoute(context, route));
                if (mcpResult != null) {
                    return mcpResult;
                }
            }
        }
        return null;
    }

    /**
     * CLI 模式执行：按命令序列逐个尝试。
     * CLI execution path: tries command sequence with graceful fallbacks.
     */
    private ToolResult searchViaCli(String topic,
                                    String locale,
                                    Map<String, String> context,
                                    TravelIntentRoute route) {
        if (!isCliReady()) {
            return null;
        }

        for (String command : commandSequence(route)) {
            String output = invokeCliCommand(command, topic, locale, context);
            String summary = extractCliSummary(output);
            if (StringUtils.hasText(summary)) {
                return new ToolResult("search", summary, cliMetadata(command));
            }
        }
        return null;
    }

    /**
     * 构建命令尝试序列（主命令 + 回退命令）。
     * Builds command attempts list (primary + fallbacks).
     */
    private List<String> commandSequence(TravelIntentRoute route) {
        LinkedHashSet<String> commands = new LinkedHashSet<>();
        if (route != null && route.travelIntent() && StringUtils.hasText(route.primaryCommand())) {
            commands.add(route.primaryCommand());
            if (route.fallbackCommands() != null) {
                commands.addAll(route.fallbackCommands());
            }
        }
        if (commands.isEmpty()) {
            commands.add("ai-search");
            commands.add("keyword-search");
        }
        return new ArrayList<>(commands);
    }

    /**
     * 执行单个 CLI 命令，内部兼容多种参数格式。
     * Executes one CLI command with multiple argument conventions.
     */
    private String invokeCliCommand(String command,
                                    String topic,
                                    String locale,
                                    Map<String, String> context) {
        List<List<String>> variants = buildCliCommandVariants(command, topic, locale, context);
        for (List<String> variant : variants) {
            CliExecutionResult result = executeCli(variant);
            if (result.timedOut()) {
                log.warn("FlyAI CLI timed out for command {}", command);
                continue;
            }
            if (result.exitCode() == 0 && StringUtils.hasText(result.output())) {
                return result.output();
            }
            if (result.exitCode() != 0) {
                log.debug("FlyAI CLI non-zero exit code {} for {}", result.exitCode(), variant);
            }
        }
        return null;
    }

    /**
     * 构建 CLI 命令变体，覆盖常见 flag 与位置参数风格。
     * Builds CLI command variants for flag-style and positional-style invocations.
     */
    private List<List<String>> buildCliCommandVariants(String command,
                                                       String topic,
                                                       String locale,
                                                       Map<String, String> context) {
        List<String> base = parseCommand(cliCommand);
        if (base.isEmpty()) {
            return List.of();
        }

        String resolvedTopic = StringUtils.hasText(topic)
                ? topic
                : (locale != null && locale.toLowerCase(Locale.ROOT).startsWith("zh")
                ? "近期节日旅行推荐"
                : "upcoming holiday travel ideas");
        String resolvedLocale = StringUtils.hasText(locale) ? locale : "en-US";
        Map<String, String> normalizedContext = normalizeContext(context);

        List<List<String>> variants = new ArrayList<>();

        List<String> styleA = new ArrayList<>(base);
        styleA.add(command);
        appendFlag(styleA, "--query", resolvedTopic);
        appendFlag(styleA, "--locale", resolvedLocale);
        appendGenericContextFlags(styleA, normalizedContext);
        appendCommandSpecificFlags(styleA, command, normalizedContext);
        appendFlag(styleA, "--format", "json");
        variants.add(styleA);

        List<String> styleB = new ArrayList<>(base);
        styleB.add(command);
        styleB.add(resolvedTopic);
        appendFlag(styleB, "--locale", resolvedLocale);
        appendGenericContextFlags(styleB, normalizedContext);
        appendCommandSpecificFlags(styleB, command, normalizedContext);
        variants.add(styleB);

        List<String> styleC = new ArrayList<>(base);
        styleC.add(command);
        appendFlag(styleC, "--keyword", resolvedTopic);
        appendFlag(styleC, "--locale", resolvedLocale);
        appendGenericContextFlags(styleC, normalizedContext);
        appendCommandSpecificFlags(styleC, command, normalizedContext);
        variants.add(styleC);

        return variants;
    }

    /**
     * 执行本地 CLI 命令并捕获输出。
     * Executes local CLI command and captures output.
     */
    private CliExecutionResult executeCli(List<String> commandTokens) {
        if (commandTokens == null || commandTokens.isEmpty()) {
            return new CliExecutionResult(-1, "", false);
        }

        try {
            ProcessBuilder builder = new ProcessBuilder(commandTokens);
            builder.redirectErrorStream(true);

            Path workingDirectory = resolveCliWorkingDirectory();
            if (workingDirectory != null) {
                builder.directory(workingDirectory.toFile());
            }

            Process process = builder.start();
            CompletableFuture<String> outputFuture = CompletableFuture.supplyAsync(() -> readProcessOutput(process));

            boolean finished = process.waitFor(cliTimeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!finished) {
                process.destroy();
                process.waitFor(300, TimeUnit.MILLISECONDS);
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
                return new CliExecutionResult(-1, outputFuture.getNow(""), true);
            }

            String output = outputFuture.get(300, TimeUnit.MILLISECONDS);
            return new CliExecutionResult(process.exitValue(), output, false);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            log.warn("FlyAI CLI invocation interrupted.", interruptedException);
            return new CliExecutionResult(-1, "", false);
        } catch (Exception exception) {
            log.debug("FlyAI CLI invocation failed.", exception);
            return new CliExecutionResult(-1, "", false);
        }
    }

    /**
     * 读取进程标准输出内容。
     * Reads process stdout content.
     */
    private String readProcessOutput(Process process) {
        try (InputStream inputStream = process.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            return "";
        }
    }

    /**
     * 从 CLI 输出中提取可读摘要文本。
     * Extracts readable summary text from CLI output.
     */
    private String extractCliSummary(String output) {
        if (!StringUtils.hasText(output)) {
            return null;
        }

        String trimmed = output.trim();
        if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) {
            return trimmed;
        }

        try {
            JsonNode node = objectMapper.readTree(trimmed);
            String candidate = findPreferredText(node);
            if (StringUtils.hasText(candidate)) {
                return candidate;
            }
            return trimmed;
        } catch (Exception ignored) {
            return trimmed;
        }
    }

    /**
     * 递归提取 JSON 中优先字段文本。
     * Recursively extracts text from preferred JSON fields.
     */
    private String findPreferredText(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return node.asText();
        }
        if (node.isObject()) {
            for (String field : List.of("summary", "answer", "result", "content", "message", "text")) {
                String found = findPreferredText(node.get(field));
                if (StringUtils.hasText(found)) {
                    return found;
                }
            }
            var fields = node.fields();
            while (fields.hasNext()) {
                var entry = fields.next();
                String found = findPreferredText(entry.getValue());
                if (StringUtils.hasText(found)) {
                    return found;
                }
            }
        }
        if (node.isArray()) {
            for (JsonNode child : node) {
                String found = findPreferredText(child);
                if (StringUtils.hasText(found)) {
                    return found;
                }
            }
        }
        return null;
    }

    /**
     * 将上下文补齐为标准键集合，便于构造命令参数。
     * Normalizes context into canonical keys for command construction.
     */
    private Map<String, String> normalizeContext(Map<String, String> context) {
        Map<String, String> normalized = new HashMap<>();
        if (context == null || context.isEmpty()) {
            return normalized;
        }
        putIfHasText(normalized, "travelers", pick(context, "travelers", "traveler_count"));
        putIfHasText(normalized, "budget", pick(context, "budget", "budget_level"));
        putIfHasText(normalized, "time_window", pick(context, "time_window", "timeWindow"));
        putIfHasText(normalized, "from", pick(context, "from", "origin", "depart_city", "departure_city"));
        putIfHasText(normalized, "to", pick(context, "to", "destination", "arrive_city", "arrival_city"));
        putIfHasText(normalized, "date", pick(context, "date", "departure_date", "depart_date"));
        putIfHasText(normalized, "checkin", pick(context, "checkin", "check_in", "checkIn"));
        putIfHasText(normalized, "checkout", pick(context, "checkout", "check_out", "checkOut"));
        putIfHasText(normalized, "city", pick(context, "city", "destination_city", "location"));
        return normalized;
    }

    /**
     * 将路由信息注入上下文，便于 MCP 侧按意图处理。
     * Injects route details into context for MCP-side intent handling.
     */
    private Map<String, String> contextWithRoute(Map<String, String> context, TravelIntentRoute route) {
        Map<String, String> merged = new HashMap<>();
        if (context != null) {
            merged.putAll(context);
        }
        if (route != null) {
            putIfHasText(merged, "travel_command", route.primaryCommand());
            putIfHasText(merged, "intent_route", route.routeType());
        }
        return merged;
    }

    /**
     * 合并路由元数据，方便后续链路追踪。
     * Merges route metadata for traceability.
     */
    private ToolResult enrichRouteMetadata(ToolResult result, TravelIntentRoute route) {
        if (result == null || route == null) {
            return result;
        }
        Map<String, String> metadata = new HashMap<>();
        if (result.metadata() != null) {
            metadata.putAll(result.metadata());
        }
        metadata.put("routeType", route.routeType());
        metadata.put("routeReason", route.reason());
        metadata.put("travelIntent", String.valueOf(route.travelIntent()));
        if (route.travelIntent()) {
            metadata.put("travelCommand", route.primaryCommand());
        }
        return new ToolResult(result.toolName(), result.summary(), metadata);
    }

    /**
     * 获取 FlyAI 模式对应执行顺序。
     * Resolves execution order for configured FlyAI mode.
     */
    private List<String> modeOrder() {
        String normalized = normalizedMode();
        if ("cli".equals(normalized)) {
            return List.of("cli");
        }
        if ("mcp".equals(normalized)) {
            return List.of("mcp");
        }
        if ("mcp-first".equals(normalized)) {
            return MODE_MCP_FIRST;
        }
        return MODE_CLI_FIRST;
    }

    /**
     * 归一化 mode 配置值。
     * Normalizes configured mode value.
     */
    private String normalizedMode() {
        if (!StringUtils.hasText(mode)) {
            return "hybrid";
        }
        String normalized = mode.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "cli", "mcp", "hybrid", "mcp-first" -> normalized;
            default -> "hybrid";
        };
    }

    /**
     * 判断 CLI 能力是否可用。
     * Checks whether CLI path is available.
     */
    private boolean isCliReady() {
        return enabled
                && cliEnabled
                && execEnabled
                && !parseCommand(cliCommand).isEmpty();
    }

    /**
     * 判断 MCP 能力是否可用。
     * Checks whether MCP path is available.
     */
    private boolean isMcpReady() {
        return enabled && StringUtils.hasText(endpoint);
    }

    /**
     * 解析 CLI 命令字符串，支持引号。
     * Parses CLI command string with simple quote support.
     */
    private List<String> parseCommand(String command) {
        if (!StringUtils.hasText(command)) {
            return List.of();
        }
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;

        for (char c : command.toCharArray()) {
            if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
                continue;
            }
            if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
                continue;
            }
            if (Character.isWhitespace(c) && !inSingleQuote && !inDoubleQuote) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                continue;
            }
            current.append(c);
        }
        if (current.length() > 0) {
            tokens.add(current.toString());
        }
        return tokens;
    }

    /**
     * 解析 CLI 工作目录，若无效则返回 null。
     * Resolves CLI working directory and returns null when invalid.
     */
    private Path resolveCliWorkingDirectory() {
        if (!StringUtils.hasText(cliWorkingDirectory)) {
            return null;
        }
        Path path = Path.of(cliWorkingDirectory.trim());
        return Files.isDirectory(path) ? path : null;
    }

    /**
     * 为命令追加通用旅行上下文参数。
     * Appends generic travel context flags.
     */
    private void appendGenericContextFlags(List<String> command, Map<String, String> context) {
        appendFlag(command, "--travelers", context.get("travelers"));
        appendFlag(command, "--budget", context.get("budget"));
        appendFlag(command, "--time-window", context.get("time_window"));
    }

    /**
     * 按命令类型追加专用参数。
     * Appends command-specific flags.
     */
    private void appendCommandSpecificFlags(List<String> command, String commandName, Map<String, String> context) {
        switch (commandName) {
            case "search-flight", "search-train" -> {
                appendFlag(command, "--from", context.get("from"));
                appendFlag(command, "--to", context.get("to"));
                appendFlag(command, "--date", context.get("date"));
            }
            case "search-hotel" -> {
                appendFlag(command, "--city", context.get("city"));
                appendFlag(command, "--checkin", context.get("checkin"));
                appendFlag(command, "--checkout", context.get("checkout"));
            }
            case "search-poi" -> appendFlag(command, "--city", context.get("city"));
            default -> {
                // ai-search / keyword-search keep generic args only.
            }
        }
    }

    /**
     * 仅当值非空时写入 flag。
     * Adds flag only when value is non-blank.
     */
    private void appendFlag(List<String> command, String key, String value) {
        if (StringUtils.hasText(value)) {
            command.add(key);
            command.add(sanitizeArg(value));
        }
    }

    /**
     * 清理命令参数中的换行和首尾空白。
     * Sanitizes argument text by trimming and removing line breaks.
     */
    private String sanitizeArg(String value) {
        return value.trim()
                .replace('\n', ' ')
                .replace('\r', ' ');
    }

    /**
     * 从别名键中取第一个非空值。
     * Picks first non-blank value from alias keys.
     */
    private String pick(Map<String, String> map, String... keys) {
        for (String key : keys) {
            String value = map.get(key);
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 延迟创建 MCP 客户端。
     * Lazily creates and reuses singleton MCP client wrapper.
     */
    private McpClientWrapper getOrCreateClient() {
        if (clientWrapper != null) {
            return clientWrapper;
        }

        synchronized (this) {
            if (clientWrapper != null) {
                return clientWrapper;
            }

            McpClientBuilder builder = McpClientBuilder.create("flyai-search");
            if ("http".equalsIgnoreCase(transport) || "streamable-http".equalsIgnoreCase(transport)) {
                builder.streamableHttpTransport(endpoint);
            } else {
                builder.sseTransport(endpoint);
            }
            if (StringUtils.hasText(authHeaderName) && StringUtils.hasText(authHeaderValue)) {
                builder.header(authHeaderName, authHeaderValue);
            }

            clientWrapper = builder
                    .timeout(mcpTimeout)
                    .initializationTimeout(mcpTimeout)
                    .buildSync();
            clientWrapper.initialize().block(mcpTimeout);
            return clientWrapper;
        }
    }

    /**
     * 解析 MCP 工具名。
     * Resolves tool name from config or MCP discovery.
     */
    private String getOrResolveToolName(McpClientWrapper wrapper) {
        if (StringUtils.hasText(resolvedToolName)) {
            return resolvedToolName;
        }
        if (StringUtils.hasText(configuredToolName)) {
            resolvedToolName = configuredToolName;
            return resolvedToolName;
        }

        List<McpSchema.Tool> tools = wrapper.listTools().block(mcpTimeout);
        if (tools == null || tools.isEmpty()) {
            return null;
        }

        List<String> aliases = List.of("travel_search", "search_travel", "search", "trip_search", "destination_search");
        for (String alias : aliases) {
            for (McpSchema.Tool tool : tools) {
                if (tool.name().equalsIgnoreCase(alias)) {
                    resolvedToolName = tool.name();
                    return resolvedToolName;
                }
            }
        }

        for (McpSchema.Tool tool : tools) {
            String lowered = tool.name().toLowerCase(Locale.ROOT);
            if (lowered.contains("search") || lowered.contains("travel") || lowered.contains("trip")) {
                resolvedToolName = tool.name();
                return resolvedToolName;
            }
        }

        resolvedToolName = tools.get(0).name();
        return resolvedToolName;
    }

    /**
     * 通过多种 payload 兼容方式调用 MCP 工具。
     * Invokes MCP tool with compatibility payload attempts.
     */
    private String callSearchTool(McpClientWrapper wrapper,
                                  String toolName,
                                  String topic,
                                  String locale,
                                  Map<String, String> context) {
        List<Map<String, Object>> attempts = new ArrayList<>();
        attempts.add(Map.of("query", topic, "locale", locale));
        attempts.add(Map.of("topic", topic, "locale", locale));
        attempts.add(Map.of("keyword", topic, "locale", locale));
        attempts.add(Map.of("destination", topic, "locale", locale));
        attempts.add(extendedPayload(topic, locale, context));

        for (Map<String, Object> attempt : attempts) {
            try {
                McpSchema.CallToolResult result = wrapper.callTool(toolName, attempt).block(mcpTimeout);
                String text = extractText(result);
                if (StringUtils.hasText(text)) {
                    return text;
                }
            } catch (Exception exception) {
                log.debug("FlyAI MCP tool invocation failed for payload {}", attempt, exception);
            }
        }
        return null;
    }

    /**
     * 构建扩展 MCP 载荷。
     * Builds extended MCP payload with travel context.
     */
    private Map<String, Object> extendedPayload(String topic, String locale, Map<String, String> context) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", topic);
        payload.put("locale", locale);
        putObjectIfHasText(payload, "travelers", valueOrNull(context, "travelers"));
        putObjectIfHasText(payload, "budget", valueOrNull(context, "budget"));
        String timeWindow = valueOrNull(context, "time_window");
        if (timeWindow == null) {
            timeWindow = valueOrNull(context, "timeWindow");
        }
        putObjectIfHasText(payload, "time_window", timeWindow);
        putObjectIfHasText(payload, "travel_command", valueOrNull(context, "travel_command"));
        putObjectIfHasText(payload, "intent_route", valueOrNull(context, "intent_route"));
        return payload;
    }

    /**
     * 读取可选上下文字段。
     * Reads optional context value and normalizes blank to null.
     */
    private String valueOrNull(Map<String, String> map, String key) {
        if (map == null) {
            return null;
        }
        String value = map.get(key);
        return StringUtils.hasText(value) ? value : null;
    }

    /**
     * 非空才写入 map。
     * Writes key-value only when value has text.
     */
    private void putIfHasText(Map<String, String> map, String key, String value) {
        if (StringUtils.hasText(value)) {
            map.put(key, value);
        }
    }

    /**
     * 非空才写入 map（Object 版本）。
     * Writes key-value only when value has text (Object map overload).
     */
    private void putObjectIfHasText(Map<String, Object> map, String key, String value) {
        if (StringUtils.hasText(value)) {
            map.put(key, value);
        }
    }

    /**
     * 构建 CLI 元数据。
     * Builds metadata for CLI-based responses.
     */
    private Map<String, String> cliMetadata(String command) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("provider", "flyai-cli");
        metadata.put("connector", "flyai-search");
        metadata.put("mode", "live-cli");
        metadata.put("travelCommand", command);
        return metadata;
    }

    /**
     * 构建 MCP 元数据。
     * Builds metadata for MCP responses.
     */
    private Map<String, String> metadata(String toolName) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("provider", "flyai-mcp");
        metadata.put("connector", "flyai-search");
        metadata.put("mode", "live-mcp");
        metadata.put("toolName", toolName);
        return metadata;
    }

    /**
     * 提取 MCP 返回文本。
     * Extracts text content from MCP call result.
     */
    private String extractText(McpSchema.CallToolResult result) {
        if (result == null || Boolean.TRUE.equals(result.isError()) || result.content() == null) {
            return null;
        }
        return result.content().stream()
                .map(content -> {
                    if (content instanceof McpSchema.TextContent textContent) {
                        return textContent.text();
                    }
                    return content.toString();
                })
                .filter(StringUtils::hasText)
                .reduce((left, right) -> left + "\n" + right)
                .orElse(null);
    }

    /**
     * CLI 执行结果模型。
     * CLI execution result model.
     */
    private record CliExecutionResult(
            int exitCode,
            String output,
            boolean timedOut
    ) {
    }
}
