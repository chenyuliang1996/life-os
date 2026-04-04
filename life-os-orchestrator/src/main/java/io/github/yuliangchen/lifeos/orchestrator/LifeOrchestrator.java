package io.github.yuliangchen.lifeos.orchestrator;

import io.github.yuliangchen.lifeos.agents.LearningAgent;
import io.github.yuliangchen.lifeos.agents.ScheduleAgent;
import io.github.yuliangchen.lifeos.agents.TravelAgent;
import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.AgentContribution;
import io.github.yuliangchen.lifeos.domain.model.AgentTask;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationRequest;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationStatus;
import io.github.yuliangchen.lifeos.domain.model.ExecutionRun;
import io.github.yuliangchen.lifeos.domain.model.LifePlan;
import io.github.yuliangchen.lifeos.domain.model.MemoryUpdateCommand;
import io.github.yuliangchen.lifeos.domain.model.OrchestrationResult;
import io.github.yuliangchen.lifeos.domain.model.PlanPreviewRequest;
import io.github.yuliangchen.lifeos.domain.model.PlanStatus;
import io.github.yuliangchen.lifeos.domain.model.PlanTask;
import io.github.yuliangchen.lifeos.domain.model.SpecialistType;
import io.github.yuliangchen.lifeos.domain.model.TaskStatus;
import io.github.yuliangchen.lifeos.domain.model.TimelineEvent;
import io.github.yuliangchen.lifeos.domain.repository.ConfirmationRequestRepository;
import io.github.yuliangchen.lifeos.domain.repository.ExecutionRunRepository;
import io.github.yuliangchen.lifeos.domain.repository.LifePlanRepository;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class LifeOrchestrator implements ModuleExecutable<PlanPreviewRequest, OrchestrationResult> {

    private final TravelAgent travelAgent;
    private final LearningAgent learningAgent;
    private final ScheduleAgent scheduleAgent;
    private final MemoryModuleFacade memoryModuleFacade;
    private final LifePlanRepository lifePlanRepository;
    private final ConfirmationRequestRepository confirmationRequestRepository;
    private final ExecutionRunRepository executionRunRepository;

    public LifeOrchestrator(TravelAgent travelAgent,
                            LearningAgent learningAgent,
                            ScheduleAgent scheduleAgent,
                            MemoryModuleFacade memoryModuleFacade,
                            LifePlanRepository lifePlanRepository,
                            ConfirmationRequestRepository confirmationRequestRepository,
                            ExecutionRunRepository executionRunRepository) {
        this.travelAgent = travelAgent;
        this.learningAgent = learningAgent;
        this.scheduleAgent = scheduleAgent;
        this.memoryModuleFacade = memoryModuleFacade;
        this.lifePlanRepository = lifePlanRepository;
        this.confirmationRequestRepository = confirmationRequestRepository;
        this.executionRunRepository = executionRunRepository;
    }

    @Override
    public String moduleName() {
        return "life-os-orchestrator";
    }

    @Override
    public OrchestrationResult execute(PlanPreviewRequest input) {
        String locale = LocaleSupport.resolve(input.locale(), input.input());
        memoryModuleFacade.execute(new MemoryUpdateCommand(
                input.userId(),
                Map.of("lastIntent", input.input()),
                List.of()
        ));

        AgentContribution travelContribution = travelAgent.execute(new AgentTask(
                input.userId(),
                SpecialistType.TRAVEL,
                input.input(),
                List.of("prioritize low-fatigue pacing", "keep budget visible"),
                locale
        ));
        AgentContribution learningContribution = learningAgent.execute(new AgentTask(
                input.userId(),
                SpecialistType.LEARNING,
                input.input(),
                List.of("preserve daily practice", "prefer short sessions"),
                locale
        ));
        AgentContribution scheduleContribution = scheduleAgent.execute(new AgentTask(
                input.userId(),
                SpecialistType.SCHEDULE,
                input.input(),
                List.of("protect recovery time", "require confirmation for external writes"),
                locale
        ));

        List<AgentContribution> contributions = List.of(travelContribution, learningContribution, scheduleContribution);
        List<PlanTask> tasks = contributions.stream()
                .flatMap(contribution -> contribution.suggestedTasks().stream())
                .toList();

        String runId = UUID.randomUUID().toString();
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("threadId", input.threadId());
        metadata.put("input", input.input());
        metadata.put("locale", locale);
        if (input.sessionId() != null && !input.sessionId().isBlank()) {
            metadata.put("sessionId", input.sessionId());
        }
        if (input.contextId() != null && !input.contextId().isBlank()) {
            metadata.put("contextId", input.contextId());
        }
        if (input.traceId() != null && !input.traceId().isBlank()) {
            metadata.put("traceId", input.traceId());
        }
        metadata.put("runId", runId);
        metadata.put("awaitingConfirmation", tasks.stream().anyMatch(task -> task.status() == TaskStatus.READY_FOR_CONFIRMATION));
        metadata.put("continued", false);

        LifePlan plan = lifePlanRepository.save(new LifePlan(
                UUID.randomUUID().toString(),
                input.userId(),
                LocaleSupport.pick(locale, "生活协同行动方案", "Composite Life Plan"),
                LocaleSupport.pick(
                        locale,
                        "围绕当前目标融合旅行、学习与日程协调的综合方案。",
                        "A merged travel, learning, and scheduling proposal for the user's current goal."
                ),
                PlanStatus.ACTIVE,
                tasks,
                Map.copyOf(metadata)
        ));

        List<ConfirmationRequest> confirmations = buildConfirmations(plan, tasks);
        List<TimelineEvent> timeline = buildTimeline(runId, contributions, confirmations, locale);
        ExecutionRun run = executionRunRepository.save(new ExecutionRun(
                runId,
                input.userId(),
                input.input(),
                confirmations.isEmpty() ? PlanStatus.COMPLETED : PlanStatus.ACTIVE,
                Instant.now(),
                timeline
        ));
        return new OrchestrationResult(run, plan, contributions, confirmations);
    }

    @Override
    public String executeProbe() {
        return "Orchestrator is ready to coordinate travel, learning, and schedule specialists without side effects.";
    }

    private List<TimelineEvent> buildTimeline(String runId,
                                              List<AgentContribution> contributions,
                                              List<ConfirmationRequest> confirmations,
                                              String locale) {
        List<TimelineEvent> events = new ArrayList<>();
        events.add(new TimelineEvent(
                UUID.randomUUID().toString(),
                runId,
                "PLAN",
                LocaleSupport.pick(locale, "主控 Agent 已拆分子任务。", "Orchestrator split the request into specialist tasks."),
                Instant.now()
        ));
        for (AgentContribution contribution : contributions) {
            events.add(new TimelineEvent(
                    UUID.randomUUID().toString(),
                    runId,
                    "AGENT",
                    contribution.agentName() + ": " + contribution.summary(),
                    Instant.now()
            ));
        }
        if (!confirmations.isEmpty()) {
            events.add(new TimelineEvent(
                    UUID.randomUUID().toString(),
                    runId,
                    "HITL_WAIT",
                    LocaleSupport.pick(locale, "执行暂停，等待用户确认外部写入动作。", "Execution paused while waiting for approval on external writes."),
                    Instant.now()
            ));
        }
        return List.copyOf(events);
    }

    private List<ConfirmationRequest> buildConfirmations(LifePlan plan, List<PlanTask> tasks) {
        String locale = LocaleSupport.resolve((String) plan.metadata().get("locale"), plan.summary());
        List<ConfirmationRequest> confirmations = tasks.stream()
                .filter(task -> task.status().name().contains("CONFIRMATION"))
                .map(task -> confirmationRequestRepository.save(new ConfirmationRequest(
                        UUID.randomUUID().toString(),
                        plan.userId(),
                        plan.id(),
                        task.title(),
                        ConfirmationStatus.PENDING,
                        LocaleSupport.pick(locale, "由编排器自动生成", "Generated automatically from orchestrator"),
                        Instant.now()
                )))
                .toList();
        return confirmations;
    }
}
