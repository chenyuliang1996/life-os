package io.github.yuliangchen.lifeos.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.yuliangchen.lifeos.domain.model.LifePlan;
import io.github.yuliangchen.lifeos.domain.model.PlanStatus;
import io.github.yuliangchen.lifeos.domain.model.PlanTask;
import io.github.yuliangchen.lifeos.domain.repository.LifePlanRepository;
import io.github.yuliangchen.lifeos.infra.persistence.JsonValueCodec;
import io.github.yuliangchen.lifeos.infra.persistence.entity.LifePlanEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataLifePlanEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseLifePlanRepository implements LifePlanRepository {

    private final SpringDataLifePlanEntityRepository repository;
    private final JsonValueCodec jsonValueCodec;

    public DatabaseLifePlanRepository(SpringDataLifePlanEntityRepository repository, JsonValueCodec jsonValueCodec) {
        this.repository = repository;
        this.jsonValueCodec = jsonValueCodec;
    }

    @Override
    public LifePlan save(LifePlan plan) {
        LifePlanEntity entity = repository.findById(plan.id()).orElseGet(LifePlanEntity::new);
        entity.setId(plan.id());
        entity.setUserId(plan.userId());
        entity.setTitle(plan.title());
        entity.setSummary(plan.summary());
        entity.setStatus(plan.status().name());
        entity.setTasksJson(jsonValueCodec.write(plan.tasks()));
        entity.setMetadataJson(jsonValueCodec.write(plan.metadata()));
        entity.setCreatedAt(entity.getCreatedAt() == null ? Instant.now() : entity.getCreatedAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public List<LifePlan> findByStatus(PlanStatus status) {
        return repository.findByStatusOrderByCreatedAtDesc(status.name()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<LifePlan> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    private LifePlan toDomain(LifePlanEntity entity) {
        return new LifePlan(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getSummary(),
                PlanStatus.valueOf(entity.getStatus()),
                jsonValueCodec.read(entity.getTasksJson(), new TypeReference<List<PlanTask>>() {
                }, List.of()),
                jsonValueCodec.read(entity.getMetadataJson(), new TypeReference<Map<String, Object>>() {
                }, Map.of())
        );
    }
}
