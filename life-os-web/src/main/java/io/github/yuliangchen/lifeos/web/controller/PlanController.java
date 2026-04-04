package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.LifePlan;
import io.github.yuliangchen.lifeos.domain.model.OrchestrationResult;
import io.github.yuliangchen.lifeos.domain.model.PlanPreviewRequest;
import io.github.yuliangchen.lifeos.domain.model.PlanStatus;
import io.github.yuliangchen.lifeos.domain.repository.LifePlanRepository;
import io.github.yuliangchen.lifeos.orchestrator.LifeOrchestrator;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import io.github.yuliangchen.lifeos.web.security.LifeOsSecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanController {

    private final LifeOrchestrator lifeOrchestrator;
    private final LifePlanRepository lifePlanRepository;
    private final LifeOsObservabilityService lifeOsObservabilityService;
    private final LifeOsSecurityService lifeOsSecurityService;

    public PlanController(LifeOrchestrator lifeOrchestrator,
                          LifePlanRepository lifePlanRepository,
                          LifeOsObservabilityService lifeOsObservabilityService,
                          LifeOsSecurityService lifeOsSecurityService) {
        this.lifeOrchestrator = lifeOrchestrator;
        this.lifePlanRepository = lifePlanRepository;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
        this.lifeOsSecurityService = lifeOsSecurityService;
    }

    @PostMapping("/preview")
    public OrchestrationResult preview(@RequestBody PlanPreviewRequest request) {
        long startedAt = System.nanoTime();
        try {
            OrchestrationResult result = lifeOrchestrator.execute(request);
            lifeOsObservabilityService.recordPlanPreview(
                    elapsedMillis(startedAt),
                    true,
                    request.userId(),
                    request.threadId(),
                    request.sessionId(),
                    request.contextId(),
                    request.traceId(),
                    "toc",
                    request.locale()
            );
            lifeOsSecurityService.recordAudit(
                    request.userId(),
                    request.threadId(),
                    "plan",
                    "preview",
                    result.plan().id(),
                    "SUCCESS",
                    "Created a user-scoped action plan with " + result.plan().tasks().size() + " tasks."
            );
            return result;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordPlanPreview(
                    elapsedMillis(startedAt),
                    false,
                    request.userId(),
                    request.threadId(),
                    request.sessionId(),
                    request.contextId(),
                    request.traceId(),
                    "toc",
                    request.locale()
            );
            lifeOsSecurityService.recordAudit(
                    request.userId(),
                    request.threadId(),
                    "plan",
                    "preview",
                    "preview",
                    "FAILURE",
                    exception.getMessage()
            );
            throw exception;
        }
    }

    @GetMapping
    public List<LifePlan> listPlans(@RequestParam(defaultValue = "lifeos-user") String userId,
                                    @RequestParam(defaultValue = "ACTIVE") PlanStatus status) {
        return lifePlanRepository.findByUserIdAndStatus(userId, status);
    }

    @GetMapping("/{planId}")
    public LifePlan getPlan(@PathVariable String planId,
                            @RequestParam(defaultValue = "lifeos-user") String userId) {
        LifePlan plan = lifePlanRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        if (!plan.userId().equals(userId)) {
            lifeOsSecurityService.recordAudit(
                    userId,
                    String.valueOf(plan.metadata().getOrDefault("threadId", "n/a")),
                    "plan",
                    "read",
                    planId,
                    "DENIED",
                    "Blocked cross-user plan access attempt."
            );
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found");
        }
        return plan;
    }

    private long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
