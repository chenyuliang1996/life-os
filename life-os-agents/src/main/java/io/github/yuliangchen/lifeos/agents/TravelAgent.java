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

    public TravelAgent(ToolModuleFacade toolModuleFacade,
                       KnowledgeModuleFacade knowledgeModuleFacade,
                       MemoryModuleFacade memoryModuleFacade) {
        this.toolModuleFacade = toolModuleFacade;
        this.knowledgeModuleFacade = knowledgeModuleFacade;
        this.memoryModuleFacade = memoryModuleFacade;
    }

    @Override
    public String moduleName() {
        return "travel-agent";
    }

    @Override
    public AgentContribution execute(AgentTask input) {
        String locale = LocaleSupport.resolve(input.locale(), input.objective());
        ToolResult weather = toolModuleFacade.execute(new ToolRequest("weather", Map.of("city", "Tokyo")));
        ToolResult search = toolModuleFacade.execute(new ToolRequest("search", Map.of("topic", input.objective())));
        List<KnowledgeSnippet> snippets = knowledgeModuleFacade.execute(
                new KnowledgeQuery(input.userId(), LocaleSupport.pick(locale, "东京", "tokyo"), List.of("travel"))
        );

        String travelStyle = memoryModuleFacade.getOrCreate(input.userId())
                .preferences()
                .getOrDefault("travelStyle", LocaleSupport.pick(locale, "平衡", "balanced"));

        PlanTask task = new PlanTask(
                "task-travel-route",
                LocaleSupport.pick(locale, "生成东京轻松行程", "Build relaxed Tokyo itinerary"),
                LocaleSupport.pick(
                        locale,
                        "按" + travelStyle + "风格生成 7 天东京低疲劳路线，并结合天气做片区节奏安排。",
                        "Create a " + travelStyle + " 7-day itinerary with weather-aware neighborhood pacing."
                ),
                TaskStatus.PENDING,
                moduleName(),
                Instant.now().plusSeconds(86400)
        );

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("weather", weather.summary());
        metadata.put("search", search.summary());
        metadata.put("knowledgeSnippets", snippets);

        return new AgentContribution(
                moduleName(),
                LocaleSupport.pick(
                        locale,
                        "已结合天气和攻略生成低疲劳旅行建议。",
                        "Built a low-fatigue travel proposal with weather and guide references."
                ),
                List.of(task),
                metadata
        );
    }

    @Override
    public String executeDemo() {
        return execute(new AgentTask(
                "demo-user",
                io.github.yuliangchen.lifeos.domain.model.SpecialistType.TRAVEL,
                "Design a low-fatigue Tokyo trip",
                List.of("stay budget-aware", "prefer neighborhood pacing"),
                "en-US"
        )).summary();
    }
}
