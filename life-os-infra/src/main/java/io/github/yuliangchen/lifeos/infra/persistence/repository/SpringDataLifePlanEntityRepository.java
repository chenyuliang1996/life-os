package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.LifePlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataLifePlanEntityRepository extends JpaRepository<LifePlanEntity, String> {

    List<LifePlanEntity> findByStatusOrderByCreatedAtDesc(String status);

    List<LifePlanEntity> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, String status);
}
