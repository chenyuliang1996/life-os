package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.LifePlan;
import io.github.yuliangchen.lifeos.domain.model.PlanStatus;
import io.github.yuliangchen.lifeos.domain.repository.LifePlanRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "memory")
public class InMemoryLifePlanRepository implements LifePlanRepository {

    private final ConcurrentHashMap<String, LifePlan> store = new ConcurrentHashMap<>();

    @Override
    public LifePlan save(LifePlan plan) {
        store.put(plan.id(), plan);
        return plan;
    }

    @Override
    public List<LifePlan> findByStatus(PlanStatus status) {
        return store.values().stream()
                .filter(plan -> plan.status() == status)
                .sorted(Comparator.comparing(LifePlan::id).reversed())
                .toList();
    }

    @Override
    public List<LifePlan> findByUserIdAndStatus(String userId, PlanStatus status) {
        return store.values().stream()
                .filter(plan -> plan.userId().equals(userId))
                .filter(plan -> plan.status() == status)
                .sorted(Comparator.comparing(LifePlan::id).reversed())
                .toList();
    }

    @Override
    public Optional<LifePlan> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
