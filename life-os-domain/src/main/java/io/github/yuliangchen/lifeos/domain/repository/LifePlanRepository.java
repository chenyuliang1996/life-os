package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.LifePlan;
import io.github.yuliangchen.lifeos.domain.model.PlanStatus;

import java.util.List;
import java.util.Optional;

public interface LifePlanRepository {

    LifePlan save(LifePlan plan);

    List<LifePlan> findByStatus(PlanStatus status);

    Optional<LifePlan> findById(String id);
}
