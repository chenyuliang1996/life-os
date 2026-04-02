package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.ConfirmationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataConfirmationRequestEntityRepository extends JpaRepository<ConfirmationRequestEntity, String> {

    List<ConfirmationRequestEntity> findAllByOrderByCreatedAtDesc();

    List<ConfirmationRequestEntity> findByPlanIdOrderByCreatedAtAsc(String planId);

    List<ConfirmationRequestEntity> findByStatusOrderByCreatedAtDesc(String status);

    List<ConfirmationRequestEntity> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, String status);
}
