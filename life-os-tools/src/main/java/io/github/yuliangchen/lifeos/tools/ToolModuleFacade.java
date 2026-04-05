package io.github.yuliangchen.lifeos.tools;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.ConnectorStatus;
import io.github.yuliangchen.lifeos.domain.model.ToolRequest;
import io.github.yuliangchen.lifeos.domain.model.ToolResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
public class ToolModuleFacade implements ModuleExecutable<ToolRequest, ToolResult> {

    private final ClawSkillConnector clawSkillConnector;
    private final FlyAiSearchConnector flyAiSearchConnector;

    public ToolModuleFacade(ClawSkillConnector clawSkillConnector,
                            FlyAiSearchConnector flyAiSearchConnector) {
        this.clawSkillConnector = clawSkillConnector;
        this.flyAiSearchConnector = flyAiSearchConnector;
    }

    @Override
    public String moduleName() {
        return "life-os-tools";
    }

    @Override
    public ToolResult execute(ToolRequest input) {
        return switch (input.toolName()) {
            case "weather" -> new ToolResult("weather", "Tokyo weather looks mild with a few light-rain days.");
            case "search" -> search(input.parameters());
            case "calendar" -> new ToolResult("calendar", "Created a draft calendar block named 'Tokyo trip prep'.");
            case "reminder" -> new ToolResult("reminder", "Prepared a reminder draft for daily English listening.");
            default -> new ToolResult(input.toolName(), "Tool " + input.toolName() + " is available but not yet wired.");
        };
    }

    public List<ConnectorStatus> connectorStatuses() {
        return List.of(
                new ConnectorStatus("search", true, "Unified travel search is enabled (Claw -> FlyAI -> seeded fallback)."),
                clawSkillConnector.connectorStatus(),
                flyAiSearchConnector.connectorStatus(),
                new ConnectorStatus("weather", true, "Mock weather connector enabled"),
                new ConnectorStatus("calendar", true, "Mock calendar connector enabled"),
                new ConnectorStatus("reminder", true, "Mock reminder connector enabled")
        );
    }

    private ToolResult search(Map<String, String> parameters) {
        Map<String, String> safeParameters = parameters == null ? Map.of() : parameters;
        String topic = valueOrDefault(safeParameters, "topic", "Tokyo travel");
        String locale = valueOrDefault(safeParameters, "locale", "en-US");

        ToolResult clawResult = clawSkillConnector.searchLive(topic, locale, safeParameters);
        if (clawResult != null) {
            return clawResult;
        }

        ToolResult flyAiResult = flyAiSearchConnector.searchLive(topic, locale, safeParameters);
        if (flyAiResult != null) {
            return flyAiResult;
        }

        return flyAiSearchConnector.fallbackSearch(topic, locale);
    }

    private String valueOrDefault(Map<String, String> parameters, String key, String fallback) {
        String value = parameters.get(key);
        return StringUtils.hasText(value) ? value : fallback;
    }

    @Override
    public String executeProbe() {
        return execute(new ToolRequest("weather", Map.of("city", "Tokyo"))).summary();
    }
}
