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
/**
 * 工具模块外观层，统一对接搜索与外部动作连接器。
 * Tool module facade that orchestrates search and external action connectors.
 */
public class ToolModuleFacade implements ModuleExecutable<ToolRequest, ToolResult> {

    private final ClawSkillConnector clawSkillConnector;
    private final FlyAiSearchConnector flyAiSearchConnector;
    private final TravelIntentRouter travelIntentRouter;

    /**
     * 构造工具模块外观。
     * Constructs tool module facade.
     */
    public ToolModuleFacade(ClawSkillConnector clawSkillConnector,
                            FlyAiSearchConnector flyAiSearchConnector,
                            TravelIntentRouter travelIntentRouter) {
        this.clawSkillConnector = clawSkillConnector;
        this.flyAiSearchConnector = flyAiSearchConnector;
        this.travelIntentRouter = travelIntentRouter;
    }

    @Override
    /**
     * 返回工具模块名称。
     * Returns canonical module name.
     */
    public String moduleName() {
        return "life-os-tools";
    }

    @Override
    /**
     * 执行工具请求并返回结果。
     * Executes tool request and returns tool result.
     */
    public ToolResult execute(ToolRequest input) {
        return switch (input.toolName()) {
            case "weather" -> new ToolResult("weather", "Tokyo weather looks mild with a few light-rain days.");
            case "search" -> search(input.parameters());
            case "calendar" -> new ToolResult("calendar", "Created a draft calendar block named 'Tokyo trip prep'.");
            case "reminder" -> new ToolResult("reminder", "Prepared a reminder draft for daily English listening.");
            default -> new ToolResult(input.toolName(), "Tool " + input.toolName() + " is available but not yet wired.");
        };
    }

    /**
     * 返回所有连接器状态。
     * Returns health/status of all configured connectors.
     */
    public List<ConnectorStatus> connectorStatuses() {
        return List.of(
                new ConnectorStatus("search", true, "Intent-routed search is enabled (General QA -> LLM, Travel -> FlyAI -> Claw fallback -> seeded fallback)."),
                clawSkillConnector.connectorStatus(),
                flyAiSearchConnector.connectorStatus(),
                new ConnectorStatus("weather", true, "Mock weather connector enabled"),
                new ConnectorStatus("calendar", true, "Mock calendar connector enabled"),
                new ConnectorStatus("reminder", true, "Mock reminder connector enabled")
        );
    }

    /**
     * 按“Claw -> FlyAI -> 回退”顺序执行搜索。
     * Runs search with Claw first, then FlyAI, then seeded fallback.
     */
    private ToolResult search(Map<String, String> parameters) {
        Map<String, String> safeParameters = parameters == null ? Map.of() : parameters;
        String topic = valueOrDefault(
                safeParameters,
                "topic",
                valueOrDefault(safeParameters, "query", "Tokyo travel")
        );
        String locale = valueOrDefault(safeParameters, "locale", "en-US");
        TravelIntentRoute route = travelIntentRouter.route(safeParameters);

        if (!route.travelIntent()) {
            return new ToolResult(
                    "search",
                    locale.toLowerCase().startsWith("zh")
                            ? "已识别为普通问答请求，优先路由到核心助手模型，不触发旅行搜索。"
                            : "The request was classified as general QA and routed to the core assistant model instead of travel search.",
                    Map.of(
                            "provider", "core-llm-router",
                            "connector", "search-router",
                            "mode", "general-qa",
                            "routeType", route.routeType()
                    )
            );
        }

        ToolResult flyAiResult = flyAiSearchConnector.searchTravelIntent(topic, locale, safeParameters, route);
        if (flyAiResult != null) {
            return flyAiResult;
        }

        ToolResult clawResult = clawSkillConnector.searchLive(topic, locale, parametersWithRoute(safeParameters, route));
        if (clawResult != null) {
            return mergeRouteMetadata(clawResult, route, "claw-fallback");
        }

        return mergeRouteMetadata(flyAiSearchConnector.fallbackSearch(topic, locale), route, "seeded-fallback");
    }

    /**
     * 从参数读取值，不存在时返回默认值。
     * Reads parameter value or returns fallback when blank.
     */
    private String valueOrDefault(Map<String, String> parameters, String key, String fallback) {
        String value = parameters.get(key);
        return StringUtils.hasText(value) ? value : fallback;
    }

    /**
     * 将路由信息注入参数上下文，便于下游连接器识别命令意图。
     * Injects route hints into downstream connector context.
     */
    private Map<String, String> parametersWithRoute(Map<String, String> parameters, TravelIntentRoute route) {
        java.util.HashMap<String, String> merged = new java.util.HashMap<>(parameters);
        merged.put("intent_route", route.routeType());
        if (StringUtils.hasText(route.primaryCommand())) {
            merged.put("travel_command", route.primaryCommand());
        }
        return merged;
    }

    /**
     * 合并路由元数据，保留 provider 结果并补充回退阶段标识。
     * Merges route metadata and adds stage label for fallback visibility.
     */
    private ToolResult mergeRouteMetadata(ToolResult result, TravelIntentRoute route, String stage) {
        java.util.HashMap<String, String> metadata = new java.util.HashMap<>();
        if (result.metadata() != null) {
            metadata.putAll(result.metadata());
        }
        metadata.put("routeType", route.routeType());
        metadata.put("routeReason", route.reason());
        metadata.put("travelIntent", String.valueOf(route.travelIntent()));
        metadata.put("searchStage", stage);
        if (StringUtils.hasText(route.primaryCommand())) {
            metadata.put("travelCommand", route.primaryCommand());
        }
        return new ToolResult(result.toolName(), result.summary(), metadata);
    }

    @Override
    /**
     * 返回工具模块探针结果。
     * Returns probe result for tool module.
     */
    public String executeProbe() {
        return execute(new ToolRequest("weather", Map.of("city", "Tokyo"))).summary();
    }
}
