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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class ClawSkillConnector implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(ClawSkillConnector.class);

    private final boolean enabled;
    private final String endpoint;
    private final String transport;
    private final String configuredToolName;
    private final String authHeaderName;
    private final String authHeaderValue;
    private final Duration timeout;

    private volatile McpClientWrapper clientWrapper;
    private volatile String resolvedToolName;

    public ClawSkillConnector(Environment environment) {
        this.enabled = Boolean.parseBoolean(environment.getProperty("lifeos.claw.enabled", "false"));
        this.endpoint = environment.getProperty("lifeos.claw.endpoint", "");
        this.transport = environment.getProperty("lifeos.claw.transport", "sse");
        this.configuredToolName = environment.getProperty("lifeos.claw.tool-name", "");
        this.authHeaderName = environment.getProperty("lifeos.claw.auth-header-name", "");
        this.authHeaderValue = environment.getProperty("lifeos.claw.auth-header-value", "");
        this.timeout = Duration.ofSeconds(Integer.parseInt(environment.getProperty("lifeos.claw.timeout-seconds", "20")));
    }

    public ToolResult searchLive(String topic, String locale, Map<String, String> context) {
        if (!isReady()) {
            return null;
        }

        try {
            // Keep initialization lazy so local dev remains stable when Claw endpoint is absent.
            McpClientWrapper wrapper = getOrCreateClient();
            String toolName = getOrResolveToolName(wrapper);
            if (!StringUtils.hasText(toolName)) {
                return null;
            }

            String response = callSearchTool(wrapper, toolName, topic, locale, context);
            if (!StringUtils.hasText(response)) {
                return null;
            }

            return new ToolResult("search", response, metadata(toolName));
        } catch (Exception exception) {
            log.warn("Claw skill search failed. Falling back to next provider.", exception);
            return null;
        }
    }

    public ConnectorStatus connectorStatus() {
        if (!enabled) {
            return new ConnectorStatus("claw-skill-search", false, "Claw skill search is disabled.");
        }
        if (!StringUtils.hasText(endpoint)) {
            return new ConnectorStatus("claw-skill-search", false, "Claw is enabled in config but no endpoint is configured.");
        }
        String summary = StringUtils.hasText(resolvedToolName)
                ? "Claw skill search is active via tool " + resolvedToolName + "."
                : "Claw endpoint is configured and the skill tool will be discovered lazily on first use.";
        return new ConnectorStatus("claw-skill-search", true, summary);
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

    private McpClientWrapper getOrCreateClient() {
        if (clientWrapper != null) {
            return clientWrapper;
        }

        synchronized (this) {
            if (clientWrapper != null) {
                return clientWrapper;
            }

            McpClientBuilder builder = McpClientBuilder.create("claw-skill-search");
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

        List<String> aliases = List.of(
                "travel_search",
                "poi_search",
                "destination_search",
                "flyai_search",
                "search"
        );
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
            if (lowered.contains("claw")
                    || lowered.contains("flyai")
                    || lowered.contains("travel")
                    || lowered.contains("poi")
                    || lowered.contains("destination")
                    || lowered.contains("search")) {
                resolvedToolName = tool.name();
                return resolvedToolName;
            }
        }

        resolvedToolName = tools.get(0).name();
        return resolvedToolName;
    }

    private String callSearchTool(McpClientWrapper wrapper,
                                  String toolName,
                                  String topic,
                                  String locale,
                                  Map<String, String> context) {
        List<Map<String, Object>> attempts = buildAttempts(topic, locale, context);
        for (Map<String, Object> attempt : attempts) {
            try {
                McpSchema.CallToolResult result = wrapper.callTool(toolName, attempt).block(timeout);
                String text = extractText(result);
                if (StringUtils.hasText(text)) {
                    return text;
                }
            } catch (Exception exception) {
                log.debug("Claw tool invocation failed for payload {}", attempt, exception);
            }
        }
        return null;
    }

    private List<Map<String, Object>> buildAttempts(String topic, String locale, Map<String, String> context) {
        String resolvedTopic = StringUtils.hasText(topic) ? topic : "upcoming holiday travel ideas";
        String resolvedLocale = StringUtils.hasText(locale) ? locale : "en-US";
        String travelers = valueOrNull(context, "travelers");
        String budget = valueOrNull(context, "budget");
        String timeWindow = valueOrNull(context, "time_window");
        if (timeWindow == null) {
            timeWindow = valueOrNull(context, "timeWindow");
        }

        List<Map<String, Object>> attempts = new ArrayList<>();

        attempts.add(basePayload("query", resolvedTopic, resolvedLocale));
        attempts.add(basePayload("topic", resolvedTopic, resolvedLocale));
        attempts.add(basePayload("keyword", resolvedTopic, resolvedLocale));
        attempts.add(basePayload("destination", resolvedTopic, resolvedLocale));

        Map<String, Object> extended = basePayload("query", resolvedTopic, resolvedLocale);
        putIfHasText(extended, "travelers", travelers);
        putIfHasText(extended, "budget", budget);
        putIfHasText(extended, "time_window", timeWindow);
        attempts.add(extended);

        Map<String, Object> extendedAlias = basePayload("query", resolvedTopic, resolvedLocale);
        putIfHasText(extendedAlias, "traveler_count", travelers);
        putIfHasText(extendedAlias, "budget_level", budget);
        putIfHasText(extendedAlias, "time_window", timeWindow);
        attempts.add(extendedAlias);

        return attempts;
    }

    private Map<String, Object> basePayload(String key, String topic, String locale) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(key, topic);
        payload.put("locale", locale);
        return payload;
    }

    private String valueOrNull(Map<String, String> map, String key) {
        if (map == null) {
            return null;
        }
        String value = map.get(key);
        return StringUtils.hasText(value) ? value : null;
    }

    private void putIfHasText(Map<String, Object> map, String key, String value) {
        if (StringUtils.hasText(value)) {
            map.put(key, value);
        }
    }

    private Map<String, String> metadata(String toolName) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("provider", "claw-skill");
        metadata.put("connector", "claw-skill-search");
        metadata.put("mode", "live");
        metadata.put("toolName", toolName);
        return metadata;
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

