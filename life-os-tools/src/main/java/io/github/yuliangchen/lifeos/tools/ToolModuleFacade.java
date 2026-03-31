package io.github.yuliangchen.lifeos.tools;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.ConnectorStatus;
import io.github.yuliangchen.lifeos.domain.model.ToolRequest;
import io.github.yuliangchen.lifeos.domain.model.ToolResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ToolModuleFacade implements ModuleExecutable<ToolRequest, ToolResult> {

    private final FlyAiSearchConnector flyAiSearchConnector;

    public ToolModuleFacade(FlyAiSearchConnector flyAiSearchConnector) {
        this.flyAiSearchConnector = flyAiSearchConnector;
    }

    @Override
    public String moduleName() {
        return "life-os-tools";
    }

    @Override
    public ToolResult execute(ToolRequest input) {
        String summary = switch (input.toolName()) {
            case "weather" -> "Tokyo weather looks mild with a few light-rain days.";
            case "search" -> flyAiSearchConnector.search(
                    input.parameters().getOrDefault("topic", "Tokyo travel"),
                    input.parameters().getOrDefault("locale", "en-US")
            ).summary();
            case "calendar" -> "Created a draft calendar block named 'Tokyo trip prep'.";
            case "reminder" -> "Prepared a reminder draft for daily English listening.";
            default -> "Tool " + input.toolName() + " is available but not yet wired.";
        };
        return new ToolResult(input.toolName(), summary);
    }

    public List<ConnectorStatus> connectorStatuses() {
        return List.of(
                new ConnectorStatus("search", true, "Unified travel search connector is enabled"),
                flyAiSearchConnector.connectorStatus(),
                new ConnectorStatus("weather", true, "Mock weather connector enabled"),
                new ConnectorStatus("calendar", true, "Mock calendar connector enabled"),
                new ConnectorStatus("reminder", true, "Mock reminder connector enabled")
        );
    }

    @Override
    public String executeProbe() {
        return execute(new ToolRequest("weather", Map.of("city", "Tokyo"))).summary();
    }
}
