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
import java.util.Map;

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
        Map<String, String> preferences = memoryModuleFacade.getOrCreate(input.userId()).preferences();
        String studyGoal = preferences.getOrDefault("studyGoal", LocaleSupport.pick(locale, "英语", "english"));
        String studyCadence = preferences.getOrDefault("studyCadence", "steady");
        String feedbackStyle = preferences.getOrDefault("feedbackStyle", "supportive");
        String decisionLens = preferences.getOrDefault("decisionLens", "balanced-judgment");

        PlanTask task = new PlanTask(
                "task-learning-plan",
                LocaleSupport.pick(locale, "调整学习节奏", "Adjust learning cadence"),
                LocaleSupport.pick(
                        locale,
                        "通过更短的每日练习，保持" + studyGoal + "目标在旅途中不中断。学习节奏=" + studyCadence + "，决策方式=" + localizeDecisionLens(locale, decisionLens) + "。",
                        "Keep " + studyGoal + " practice alive with short daily sessions during the trip. Cadence=" + studyCadence + ", decision lens=" + decisionLens + "."
                ),
                TaskStatus.PENDING,
                moduleName(),
                Instant.now().plusSeconds(172800)
        );

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("knowledgeSnippets", snippets);
        metadata.put("studyGoal", studyGoal);
        metadata.put("studyCadence", studyCadence);
        metadata.put("feedbackStyle", feedbackStyle);
        metadata.put("decisionLens", decisionLens);

        return new AgentContribution(
                moduleName(),
                LocaleSupport.pick(
                        locale,
                        "已将学习安排重排为更轻量的每日片段，并按人格偏好的反馈风格（" + feedbackStyle + "）生成建议。",
                        "Replanned daily learning into lightweight sessions and tuned guidance to the persona feedback style (" + feedbackStyle + ")."
                ),
                List.of(task),
                metadata
        );
    }

    @Override
    public String executeProbe() {
        return "Learning specialist is ready to rebalance short practice loops around busy schedules.";
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
