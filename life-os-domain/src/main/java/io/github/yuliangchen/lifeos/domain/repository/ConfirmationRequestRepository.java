package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.ConfirmationRequest;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationStatus;

import java.util.List;
import java.util.Optional;

public interface ConfirmationRequestRepository {

    ConfirmationRequest save(ConfirmationRequest request);

    List<ConfirmationRequest> findAll();

    List<ConfirmationRequest> findByPlanId(String planId);

    List<ConfirmationRequest> findByStatus(ConfirmationStatus status);

    List<ConfirmationRequest> findByUserIdAndStatus(String userId, ConfirmationStatus status);

    Optional<ConfirmationRequest> findById(String id);
}
