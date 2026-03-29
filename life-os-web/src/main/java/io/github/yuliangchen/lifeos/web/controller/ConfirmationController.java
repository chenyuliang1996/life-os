package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.ConfirmationRequest;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationStatus;
import io.github.yuliangchen.lifeos.domain.repository.ConfirmationRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/confirmations")
public class ConfirmationController {

    private final ConfirmationRequestRepository confirmationRequestRepository;

    public ConfirmationController(ConfirmationRequestRepository confirmationRequestRepository) {
        this.confirmationRequestRepository = confirmationRequestRepository;
    }

    @GetMapping
    public List<ConfirmationRequest> listPending() {
        return confirmationRequestRepository.findByStatus(ConfirmationStatus.PENDING);
    }

    @PostMapping("/{id}/decision")
    public ConfirmationRequest decide(@PathVariable String id, @RequestBody Map<String, String> request) {
        ConfirmationRequest current = confirmationRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Confirmation not found"));

        ConfirmationStatus targetStatus = ConfirmationStatus.valueOf(request.getOrDefault("decision", "REJECTED"));
        ConfirmationRequest updated = new ConfirmationRequest(
                current.id(),
                current.planId(),
                current.action(),
                targetStatus,
                request.getOrDefault("comment", current.comment()),
                Instant.now()
        );
        return confirmationRequestRepository.save(updated);
    }
}
