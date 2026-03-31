package io.github.yuliangchen.lifeos.orchestrator;

import io.github.yuliangchen.lifeos.domain.model.ConfirmationRequest;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationStatus;
import io.github.yuliangchen.lifeos.domain.model.ExecutionContinuationResult;
import io.github.yuliangchen.lifeos.domain.model.ExecutionRun;
import io.github.yuliangchen.lifeos.domain.model.LifePlan;
import io.github.yuliangchen.lifeos.domain.model.PlanStatus;
import io.github.yuliangchen.lifeos.domain.model.PlanTask;
import io.github.yuliangchen.lifeos.domain.model.TaskStatus;
import io.github.yuliangchen.lifeos.domain.model.TimelineEvent;
import io.github.yuliangchen.lifeos.domain.repository.ConfirmationRequestRepository;
import io.github.yuliangchen.lifeos.domain.repository.ExecutionRunRepository;
import io.github.yuliangchen.lifeos.domain.repository.LifePlanRepository;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LifeExecutionContinuationService {

    private final LifePlanRepository lifePlanRepository;
    private final ConfirmationRequestRepository confirmationRequestRepository;
    private final ExecutionRunRepository executionRunRepository;

    public LifeExecutionContinuationService(LifePlanRepository lifePlanRepository,
                                           ConfirmationRequestRepository confirmationRequestRepository,
                                           ExecutionRunRepository executionRunRepository) {
        this.lifePlanRepository = lifePlanRepository;
        this.confirmationRequestRepository = confirmationRequestRepository;
        this.executionRunRepository = executionRunRepository;
    }

    public ExecutionContinuationResult resumePlan(String planId, String requestedLocale) {
        LifePlan plan = lifePlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));

        String locale = LocaleSupport.resolve(requestedLocale, (String) plan.metadata().get("locale"));
        List<ConfirmationRequest> confirmations = confirmationRequestRepository.findByPlanId(planId);

        String runId = String.valueOf(plan.metadata().getOrDefault("runId", ""));
        ExecutionRun run = runId.isBlank()
                ? null
                : executionRunRepository.findById(runId)
                .orElse(null);

        if (confirmations.stream().anyMatch(request -> request.status() == ConfirmationStatus.REJECTED)) {
            ExecutionRun blockedRun = run == null ? null : saveRun(run, appendUniqueEvent(
                    run.timeline(),
                    run.id(),
                    "HITL_BLOCKED",
                    LocaleSupport.pick(locale, "确认被拒绝，运行暂停。", "A confirmation was rejected, so the run remains blocked.")
            ), PlanStatus.BLOCKED);
            return new ExecutionContinuationResult(
                    false,
                    true,
                    LocaleSupport.pick(locale, "至少有一个确认被拒绝，当前运行不会继续。", "At least one confirmation was rejected, so the run will not continue."),
                    blockedRun,
                    plan,
                    confirmations
            );
        }

        if (confirmations.stream().anyMatch(request -> request.status() == ConfirmationStatus.PENDING)) {
            return new ExecutionContinuationResult(
                    false,
                    false,
                    LocaleSupport.pick(locale, "仍有待确认动作，确认完成后才能继续。", "There are still pending confirmations. The run can continue after all approvals are complete."),
                    run,
                    plan,
                    confirmations
            );
        }

        boolean alreadyContinued = Boolean.TRUE.equals(plan.metadata().get("continued"));
        if (alreadyContinued) {
            return new ExecutionContinuationResult(
                    false,
                    false,
                    LocaleSupport.pick(locale, "该计划已经完成确认后的续跑。", "This plan has already completed its post-confirmation continuation."),
                    run,
                    plan,
                    confirmations
            );
        }

        List<PlanTask> updatedTasks = plan.tasks().stream()
                .map(task -> task.status() == TaskStatus.READY_FOR_CONFIRMATION
                        ? new PlanTask(task.id(), task.title(), task.description(), TaskStatus.DONE, task.owner(), task.dueAt())
                        : task)
                .toList();

        Map<String, Object> updatedMetadata = new LinkedHashMap<>(plan.metadata());
        updatedMetadata.put("awaitingConfirmation", false);
        updatedMetadata.put("continued", true);
        updatedMetadata.put("continuedAt", Instant.now().toString());

        LifePlan updatedPlan = lifePlanRepository.save(new LifePlan(
                plan.id(),
                plan.userId(),
                plan.title(),
                plan.summary(),
                plan.status(),
                updatedTasks,
                Map.copyOf(updatedMetadata)
        ));

        ExecutionRun updatedRun = run;
        if (run != null) {
            List<TimelineEvent> timeline = new ArrayList<>(run.timeline());
            timeline.add(new TimelineEvent(
                    UUID.randomUUID().toString(),
                    run.id(),
                    "HITL_RESUME",
                    LocaleSupport.pick(locale, "所有确认已通过，恢复执行外部动作。", "All confirmations were approved, so external actions resumed."),
                    Instant.now()
            ));
            timeline.add(new TimelineEvent(
                    UUID.randomUUID().toString(),
                    run.id(),
                    "TOOL_WRITE",
                    LocaleSupport.pick(locale, "已提交日历与提醒写入动作。", "Calendar and reminder writes have been committed."),
                    Instant.now()
            ));
            updatedRun = saveRun(run, timeline, PlanStatus.COMPLETED);
        }

        return new ExecutionContinuationResult(
                true,
                false,
                LocaleSupport.pick(locale, "确认流程已完成，运行已恢复并执行外部写入。", "Confirmation is complete. The run resumed and applied the external writes."),
                updatedRun,
                updatedPlan,
                confirmations
        );
    }

    private ExecutionRun saveRun(ExecutionRun original, List<TimelineEvent> timeline, PlanStatus status) {
        return executionRunRepository.save(new ExecutionRun(
                original.id(),
                original.userId(),
                original.input(),
                status,
                original.createdAt(),
                List.copyOf(timeline)
        ));
    }

    private List<TimelineEvent> appendUniqueEvent(List<TimelineEvent> existing,
                                                  String runId,
                                                  String eventType,
                                                  String message) {
        if (existing.stream().anyMatch(event -> event.eventType().equals(eventType) && event.message().equals(message))) {
            return existing;
        }
        List<TimelineEvent> updated = new ArrayList<>(existing);
        updated.add(new TimelineEvent(UUID.randomUUID().toString(), runId, eventType, message, Instant.now()));
        return List.copyOf(updated);
    }
}
