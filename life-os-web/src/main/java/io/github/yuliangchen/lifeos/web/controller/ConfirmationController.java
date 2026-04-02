package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.ConfirmationRequest;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationDecisionRequest;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationStatus;
import io.github.yuliangchen.lifeos.domain.repository.ConfirmationRequestRepository;
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

import java.time.Instant;
import java.util.List;
@RestController
@RequestMapping("/api/v1/confirmations")
public class ConfirmationController {

    private final ConfirmationRequestRepository confirmationRequestRepository;
    private final LifeOsObservabilityService lifeOsObservabilityService;
    private final LifeOsSecurityService lifeOsSecurityService;

    public ConfirmationController(ConfirmationRequestRepository confirmationRequestRepository,
                                  LifeOsObservabilityService lifeOsObservabilityService,
                                  LifeOsSecurityService lifeOsSecurityService) {
        this.confirmationRequestRepository = confirmationRequestRepository;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
        this.lifeOsSecurityService = lifeOsSecurityService;
    }

    @GetMapping
    public List<ConfirmationRequest> listPending(@RequestParam(defaultValue = "lifeos-user") String userId) {
        return confirmationRequestRepository.findByUserIdAndStatus(userId, ConfirmationStatus.PENDING);
    }

    @PostMapping("/{id}/decision")
    public ConfirmationRequest decide(@PathVariable String id,
                                      @RequestParam(defaultValue = "lifeos-user") String userId,
                                      @RequestBody ConfirmationDecisionRequest request) {
        long startedAt = System.nanoTime();
        try {
            ConfirmationRequest current = confirmationRequestRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Confirmation not found"));

            if (!current.userId().equals(userId)) {
                lifeOsSecurityService.recordAudit(
                        userId,
                        "n/a",
                        "approval",
                        "decision",
                        id,
                        "DENIED",
                        "Blocked cross-user confirmation decision."
                );
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Confirmation not found");
            }

            ConfirmationStatus targetStatus = ConfirmationStatus.valueOf(
                    request.decision() == null || request.decision().isBlank() ? "REJECTED" : request.decision()
            );
            ConfirmationRequest updated = new ConfirmationRequest(
                    current.id(),
                    current.userId(),
                    current.planId(),
                    current.action(),
                    targetStatus,
                    request.comment() == null || request.comment().isBlank() ? current.comment() : request.comment(),
                    Instant.now()
            );
            ConfirmationRequest saved = confirmationRequestRepository.save(updated);
            lifeOsObservabilityService.recordConfirmationDecision(elapsedMillis(startedAt), true);
            lifeOsSecurityService.recordAudit(
                    current.userId(),
                    "n/a",
                    "approval",
                    "decision",
                    saved.id(),
                    saved.status().name(),
                    saved.comment()
            );
            return saved;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordConfirmationDecision(elapsedMillis(startedAt), false);
            throw exception;
        }
    }

    private long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
