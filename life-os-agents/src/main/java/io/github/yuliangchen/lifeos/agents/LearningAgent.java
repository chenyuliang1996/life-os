package io.github.yuliangchen.lifeos.agents;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.AgentContribution;
import io.github.yuliangchen.lifeos.domain.model.AgentTask;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeQuery;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeSnippet;
import io.github.yuliangchen.lifeos.domain.model.PlanTask;
import io.github.yuliangchen.lifeos.domain.model.TaskStatus;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import io.github.yuliangchen.lifeos.rag.KnowledgeModuleFacade;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Component
public class LearningAgent implements ModuleExecutable<AgentTask, AgentContribution> {

    private final KnowledgeModuleFacade knowledgeModuleFacade;
    private final MemoryModuleFacade memoryModuleFacade;

    public LearningAgent(KnowledgeModuleFacade knowledgeModuleFacade, MemoryModuleFacade memoryModuleFacade) {
        this.knowledgeModuleFacade = knowledgeModuleFacade;
        this.memoryModuleFacade = memoryModuleFacade;
    }

    @Override
    public String moduleName() {
        return "learning-agent";
    }

    @Override
    public AgentContribution execute(AgentTask input) {
        String locale = LocaleSupport.resolve(input.locale(), input.objective());
        List<KnowledgeSnippet> snippets = knowledgeModuleFacade.execute(
                new KnowledgeQuery(
                        input.userId(),
                        LocaleSupport.pick(locale, "英语", "english"),
                        List.of("learning", "english")
                )
        );
        String studyGoal = memoryModuleFacade.getOrCreate(input.userId())
                .preferences()
                .getOrDefault("studyGoal", LocaleSupport.pick(locale, "英语", "english"));

        PlanTask task = new PlanTask(
                "task-learning-plan",
                LocaleSupport.pick(locale, "调整学习节奏", "Adjust learning cadence"),
                LocaleSupport.pick(
                        locale,
                        "通过更短的每日练习，保持" + studyGoal + "目标在旅途中不中断。",
                        "Keep " + studyGoal + " practice alive with short daily sessions during the trip."
                ),
                TaskStatus.PENDING,
                moduleName(),
                Instant.now().plusSeconds(172800)
        );

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("knowledgeSnippets", snippets);
        metadata.put("studyGoal", studyGoal);

        return new AgentContribution(
                moduleName(),
                LocaleSupport.pick(
                        locale,
                        "已将学习安排重排为更轻量的每日片段。",
                        "Replanned daily learning into lightweight sessions that fit around travel days."
                ),
                List.of(task),
                metadata
        );
    }

    @Override
    public String executeProbe() {
        return execute(new AgentTask(
                "lifeos-user",
                io.github.yuliangchen.lifeos.domain.model.SpecialistType.LEARNING,
                "Keep English study going during travel",
                List.of("prefer short sessions"),
                "en-US"
        )).summary();
    }
}
