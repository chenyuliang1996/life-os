package io.github.yuliangchen.lifeos.tools;

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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class FlyAiSearchConnector implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(FlyAiSearchConnector.class);

    private final boolean enabled;
    private final String endpoint;
    private final String transport;
    private final String configuredToolName;
    private final String authHeaderName;
    private final String authHeaderValue;
    private final Duration timeout;

    private volatile McpClientWrapper clientWrapper;
    private volatile String resolvedToolName;

    public FlyAiSearchConnector(Environment environment) {
        this.enabled = Boolean.parseBoolean(environment.getProperty("lifeos.flyai.enabled", "false"));
        this.endpoint = environment.getProperty("lifeos.flyai.endpoint", "");
        this.transport = environment.getProperty("lifeos.flyai.transport", "sse");
        this.configuredToolName = environment.getProperty("lifeos.flyai.tool-name", "");
        this.authHeaderName = environment.getProperty("lifeos.flyai.auth-header-name", "");
        this.authHeaderValue = environment.getProperty("lifeos.flyai.auth-header-value", "");
        this.timeout = Duration.ofSeconds(Integer.parseInt(environment.getProperty("lifeos.flyai.timeout-seconds", "20")));
    }

    public ToolResult search(String topic, String locale) {
        if (!isReady()) {
            return fallbackSearch(topic);
        }

        try {
            McpClientWrapper wrapper = getOrCreateClient();
            String toolName = getOrResolveToolName(wrapper);
            if (!StringUtils.hasText(toolName)) {
                return new ToolResult("search", "FlyAI is configured, but no search tool was discovered. Falling back to the seeded travel guides.");
            }

            String response = callSearchTool(wrapper, toolName, topic, locale);
            if (!StringUtils.hasText(response)) {
                return new ToolResult("search", "FlyAI returned no travel search summary, so the app used its seeded travel guides.");
            }

            return new ToolResult("search", response);
        } catch (Exception exception) {
            log.warn("FlyAI search failed. Falling back to deterministic search summary.", exception);
            return fallbackSearch(topic);
        }
    }

    public ConnectorStatus connectorStatus() {
        if (!enabled) {
            return new ConnectorStatus("flyai-search", false, "FlyAI MCP search is disabled.");
        }
        if (!StringUtils.hasText(endpoint)) {
            return new ConnectorStatus("flyai-search", false, "FlyAI is enabled in config but no endpoint is configured.");
        }
        String summary = StringUtils.hasText(resolvedToolName)
                ? "FlyAI MCP search is active via tool " + resolvedToolName + "."
                : "FlyAI MCP endpoint is configured and will be discovered lazily on first use.";
        return new ConnectorStatus("flyai-search", true, summary);
    }

    @Override
    public void destroy() {
        if (clientWrapper != null) {
            clientWrapper.close();
        }
    }

    private boolean isReady() {
        return enabled && StringUtils.hasText(endpoint);
    }

    private ToolResult fallbackSearch(String topic) {
        return new ToolResult(
                "search",
                "Found three low-fatigue Tokyo neighborhood guides for \"" + topic + "\" from the seeded travel knowledge base."
        );
    }

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
                    .timeout(timeout)
                    .initializationTimeout(timeout)
                    .buildSync();
            clientWrapper.initialize().block(timeout);
            return clientWrapper;
        }
    }

    private String getOrResolveToolName(McpClientWrapper wrapper) {
        if (StringUtils.hasText(resolvedToolName)) {
            return resolvedToolName;
        }
        if (StringUtils.hasText(configuredToolName)) {
            resolvedToolName = configuredToolName;
            return resolvedToolName;
        }

        List<McpSchema.Tool> tools = wrapper.listTools().block(timeout);
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

    private String callSearchTool(McpClientWrapper wrapper, String toolName, String topic, String locale) {
        List<Map<String, Object>> attempts = new ArrayList<>();
        attempts.add(Map.of("query", topic, "locale", locale));
        attempts.add(Map.of("topic", topic, "locale", locale));
        attempts.add(Map.of("keyword", topic, "locale", locale));
        attempts.add(Map.of("destination", topic, "locale", locale));

        for (Map<String, Object> attempt : attempts) {
            try {
                McpSchema.CallToolResult result = wrapper.callTool(toolName, attempt).block(timeout);
                String text = extractText(result);
                if (StringUtils.hasText(text)) {
                    return text;
                }
            } catch (Exception exception) {
                log.debug("FlyAI tool invocation failed for payload {}", attempt, exception);
            }
        }
        return null;
    }

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
}
