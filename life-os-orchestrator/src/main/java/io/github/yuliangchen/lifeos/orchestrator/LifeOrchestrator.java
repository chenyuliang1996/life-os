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
import io.github.yuliangchen.lifeos.domain.model.TimelineEvent;
import io.github.yuliangchen.lifeos.domain.repository.ConfirmationRequestRepository;
import io.github.yuliangchen.lifeos.domain.repository.ExecutionRunRepository;
import io.github.yuliangchen.lifeos.domain.repository.LifePlanRepository;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
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
        memoryModuleFacade.execute(new MemoryUpdateCommand(
                input.userId(),
                Map.of("lastIntent", input.input()),
                List.of()
        ));

        AgentContribution travelContribution = travelAgent.execute(new AgentTask(
                SpecialistType.TRAVEL,
                input.input(),
                List.of("prioritize low-fatigue pacing", "keep budget visible")
        ));
        AgentContribution learningContribution = learningAgent.execute(new AgentTask(
                SpecialistType.LEARNING,
                input.input(),
                List.of("preserve daily practice", "prefer short sessions")
        ));
        AgentContribution scheduleContribution = scheduleAgent.execute(new AgentTask(
                SpecialistType.SCHEDULE,
                input.input(),
                List.of("protect recovery time", "require confirmation for external writes")
        ));

        List<AgentContribution> contributions = List.of(travelContribution, learningContribution, scheduleContribution);
        List<PlanTask> tasks = contributions.stream()
                .flatMap(contribution -> contribution.suggestedTasks().stream())
                .toList();

        String runId = UUID.randomUUID().toString();
        List<TimelineEvent> timeline = buildTimeline(runId, contributions);
        ExecutionRun run = executionRunRepository.save(new ExecutionRun(
                runId,
                input.userId(),
                input.input(),
                PlanStatus.ACTIVE,
                Instant.now(),
                timeline
        ));

        LifePlan plan = lifePlanRepository.save(new LifePlan(
                UUID.randomUUID().toString(),
                input.userId(),
                "Composite Life Plan",
                "A merged travel, learning, and scheduling proposal for the user's current goal.",
                PlanStatus.ACTIVE,
                tasks,
                Map.of("threadId", input.threadId(), "input", input.input())
        ));

        List<ConfirmationRequest> confirmations = buildConfirmations(plan, tasks);
        return new OrchestrationResult(run, plan, contributions, confirmations);
    }

    @Override
    public String executeDemo() {
        OrchestrationResult result = execute(new PlanPreviewRequest(
                "demo-user",
                "thread-demo",
                "Plan a relaxed Tokyo trip without interrupting English study"
        ));
        return "Orchestrator built plan " + result.plan().id() + " with " + result.plan().tasks().size() + " tasks";
    }

    private List<TimelineEvent> buildTimeline(String runId, List<AgentContribution> contributions) {
        List<TimelineEvent> events = new ArrayList<>();
        events.add(new TimelineEvent(UUID.randomUUID().toString(), runId, "PLAN", "Orchestrator split the request into specialist tasks.", Instant.now()));
        for (AgentContribution contribution : contributions) {
            events.add(new TimelineEvent(
                    UUID.randomUUID().toString(),
                    runId,
                    "AGENT",
                    contribution.agentName() + ": " + contribution.summary(),
                    Instant.now()
            ));
        }
        return List.copyOf(events);
    }

    private List<ConfirmationRequest> buildConfirmations(LifePlan plan, List<PlanTask> tasks) {
        List<ConfirmationRequest> confirmations = tasks.stream()
                .filter(task -> task.status().name().contains("CONFIRMATION"))
                .map(task -> confirmationRequestRepository.save(new ConfirmationRequest(
                        UUID.randomUUID().toString(),
                        plan.id(),
                        task.title(),
                        ConfirmationStatus.PENDING,
                        "Generated automatically from orchestrator",
                        Instant.now()
                )))
                .toList();
        return confirmations;
    }
}
