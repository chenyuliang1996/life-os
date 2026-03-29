package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.LifePlan;
import io.github.yuliangchen.lifeos.domain.model.OrchestrationResult;
import io.github.yuliangchen.lifeos.domain.model.PlanPreviewRequest;
import io.github.yuliangchen.lifeos.domain.model.PlanStatus;
import io.github.yuliangchen.lifeos.domain.repository.LifePlanRepository;
import io.github.yuliangchen.lifeos.orchestrator.LifeOrchestrator;
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

    public PlanController(LifeOrchestrator lifeOrchestrator, LifePlanRepository lifePlanRepository) {
        this.lifeOrchestrator = lifeOrchestrator;
        this.lifePlanRepository = lifePlanRepository;
    }

    @PostMapping("/preview")
    public OrchestrationResult preview(@RequestBody PlanPreviewRequest request) {
        return lifeOrchestrator.execute(request);
    }

    @GetMapping
    public List<LifePlan> listPlans(@RequestParam(defaultValue = "ACTIVE") PlanStatus status) {
        return lifePlanRepository.findByStatus(status);
    }

    @GetMapping("/{planId}")
    public LifePlan getPlan(@PathVariable String planId) {
        return lifePlanRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
    }
}
