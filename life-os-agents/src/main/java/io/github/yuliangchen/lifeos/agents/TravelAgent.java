package io.github.yuliangchen.lifeos.agents;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.AgentContribution;
import io.github.yuliangchen.lifeos.domain.model.AgentTask;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeQuery;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeSnippet;
import io.github.yuliangchen.lifeos.domain.model.PlanTask;
import io.github.yuliangchen.lifeos.domain.model.TaskStatus;
import io.github.yuliangchen.lifeos.domain.model.ToolRequest;
import io.github.yuliangchen.lifeos.domain.model.ToolResult;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import io.github.yuliangchen.lifeos.rag.KnowledgeModuleFacade;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TravelAgent implements ModuleExecutable<AgentTask, AgentContribution> {

    private final ToolModuleFacade toolModuleFacade;
    private final KnowledgeModuleFacade knowledgeModuleFacade;
    private final MemoryModuleFacade memoryModuleFacade;
    private final TravelA2aAdvisor travelA2aAdvisor;

    public TravelAgent(ToolModuleFacade toolModuleFacade,
                       KnowledgeModuleFacade knowledgeModuleFacade,
                       MemoryModuleFacade memoryModuleFacade,
                       TravelA2aAdvisor travelA2aAdvisor) {
        this.toolModuleFacade = toolModuleFacade;
        this.knowledgeModuleFacade = knowledgeModuleFacade;
        this.memoryModuleFacade = memoryModuleFacade;
        this.travelA2aAdvisor = travelA2aAdvisor;
    }

    @Override
    public String moduleName() {
        return "travel-agent";
    }

    @Override
    public AgentContribution execute(AgentTask input) {
        String locale = LocaleSupport.resolve(input.locale(), input.objective());
        ToolResult weather = toolModuleFacade.execute(new ToolRequest("weather", Map.of("city", "Tokyo")));
        ToolResult search = toolModuleFacade.execute(new ToolRequest("search", Map.of(
                "topic", input.objective(),
                "locale", locale
        )));
        List<KnowledgeSnippet> snippets = knowledgeModuleFacade.execute(
                new KnowledgeQuery(input.userId(), LocaleSupport.pick(locale, "东京", "tokyo"), List.of("travel"))
        );
        String remoteAdvice = travelA2aAdvisor.advise(input, search, snippets);

        Map<String, String> preferences = memoryModuleFacade.getOrCreate(input.userId()).preferences();
        String travelStyle = preferences.getOrDefault("travelStyle", LocaleSupport.pick(locale, "平衡", "balanced"));
        String decisionLens = preferences.getOrDefault("decisionLens", "balanced-judgment");
        String planningDepth = preferences.getOrDefault("planningDepth", "balanced");
        String feedbackStyle = preferences.getOrDefault("feedbackStyle", "direct");
        String energyMode = preferences.getOrDefault("energyMode", "balanced");

        PlanTask task = new PlanTask(
                "task-travel-route",
                LocaleSupport.pick(locale, "生成东京轻松行程", "Build relaxed Tokyo itinerary"),
                LocaleSupport.pick(
                        locale,
                        "按" + travelStyle + "风格生成 7 天东京低疲劳路线；以" + localizeDecisionLens(locale, decisionLens) + "为主决策方式，规划深度=" + planningDepth + "，节奏偏好=" + energyMode + "。",
                        "Create a " + travelStyle + " 7-day itinerary using a " + decisionLens + " decision lens, planning depth=" + planningDepth + ", and energy mode=" + energyMode + "."
                ),
                TaskStatus.PENDING,
                moduleName(),
                Instant.now().plusSeconds(86400)
        );

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("weather", weather.summary());
        metadata.put("search", search.summary());
        metadata.put("knowledgeSnippets", snippets);
        metadata.put("travelResearchMode", travelA2aAdvisor.isEnabled() ? "a2a-remote-plus-local" : "local-grounded");
        metadata.put("decisionLens", decisionLens);
        metadata.put("planningDepth", planningDepth);
        metadata.put("feedbackStyle", feedbackStyle);
        metadata.put("energyMode", energyMode);
        if (!remoteAdvice.isBlank()) {
            metadata.put("a2aAdvice", remoteAdvice);
        }

        return new AgentContribution(
                moduleName(),
                LocaleSupport.pick(
                        locale,
                        remoteAdvice.isBlank()
                                ? "已结合天气、攻略和旅行搜索生成低疲劳旅行建议，并按人格偏好调整反馈语气（" + feedbackStyle + "）。"
                                : "已结合天气、攻略、FlyAI 搜索和远程 A2A 专家建议生成低疲劳旅行建议，并按人格偏好调整反馈语气（" + feedbackStyle + "）。",
                        remoteAdvice.isBlank()
                                ? "Built a low-fatigue travel proposal with weather, search, and guides, tuned to the user's feedback style (" + feedbackStyle + ")."
                                : "Built a low-fatigue travel proposal with weather, FlyAI search, guides, and remote A2A advice, tuned to the user's feedback style (" + feedbackStyle + ")."
                ),
                List.of(task),
                metadata
        );
    }

    @Override
    public String executeProbe() {
        // Probe endpoints must stay read-only so operator views do not create user data / 探针接口必须保持只读，避免运营台刷新时写入用户数据。
        return "Travel specialist is ready for grounded itinerary planning, live search, and optional A2A advice.";
    }

    private String localizeDecisionLens(String locale, String lens) {
        return switch (lens) {
            case "systems-thinking" -> LocaleSupport.pick(locale, "系统思维", "systems thinking");
            case "evidence-first" -> LocaleSupport.pick(locale, "证据优先", "evidence-first");
            case "outcome-driven" -> LocaleSupport.pick(locale, "结果导向", "outcome-driven");
            case "possibility-scouting" -> LocaleSupport.pick(locale, "机会探索", "possibility scouting");
            case "meaning-alignment" -> LocaleSupport.pick(locale, "价值对齐", "meaning alignment");
            case "harmony-first" -> LocaleSupport.pick(locale, "关系优先", "harmony-first");
            case "people-alignment" -> LocaleSupport.pick(locale, "协同对齐", "people alignment");
            case "process-discipline" -> LocaleSupport.pick(locale, "流程纪律", "process discipline");
            case "direct-action" -> LocaleSupport.pick(locale, "快速行动", "direct action");
            case "experience-first" -> LocaleSupport.pick(locale, "体验优先", "experience-first");
            default -> LocaleSupport.pick(locale, "平衡判断", "balanced judgment");
        };
    }
}
