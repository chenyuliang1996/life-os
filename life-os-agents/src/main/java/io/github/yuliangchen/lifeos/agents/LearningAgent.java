package io.github.yuliangchen.lifeos.agents;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.AgentContribution;
import io.github.yuliangchen.lifeos.domain.model.AgentTask;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeQuery;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeSnippet;
import io.github.yuliangchen.lifeos.domain.model.PlanTask;
import io.github.yuliangchen.lifeos.domain.model.TaskStatus;
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
        List<KnowledgeSnippet> snippets = knowledgeModuleFacade.execute(
                new KnowledgeQuery("demo-user", "english", List.of("learning", "english"))
        );
        String studyGoal = memoryModuleFacade.getOrCreate("demo-user")
                .preferences()
                .getOrDefault("studyGoal", "english");

        PlanTask task = new PlanTask(
                "task-learning-plan",
                "Adjust learning cadence",
                "Keep " + studyGoal + " practice alive with short daily sessions during the trip.",
                TaskStatus.PENDING,
                moduleName(),
                Instant.now().plusSeconds(172800)
        );

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("knowledgeSnippets", snippets);
        metadata.put("studyGoal", studyGoal);

        return new AgentContribution(
                moduleName(),
                "Replanned daily learning into lightweight sessions that fit around travel days.",
                List.of(task),
                metadata
        );
    }

    @Override
    public String executeDemo() {
        return execute(new AgentTask(
                io.github.yuliangchen.lifeos.domain.model.SpecialistType.LEARNING,
                "Keep English study going during travel",
                List.of("prefer short sessions")
        )).summary();
    }
}
