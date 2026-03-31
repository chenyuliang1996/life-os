package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.ConfirmationRequest;
import io.github.yuliangchen.lifeos.domain.model.ConfirmationStatus;
import io.github.yuliangchen.lifeos.domain.repository.ConfirmationRequestRepository;
import io.github.yuliangchen.lifeos.infra.persistence.entity.ConfirmationRequestEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataConfirmationRequestEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseConfirmationRequestRepository implements ConfirmationRequestRepository {

    private final SpringDataConfirmationRequestEntityRepository repository;

    public DatabaseConfirmationRequestRepository(SpringDataConfirmationRequestEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public ConfirmationRequest save(ConfirmationRequest request) {
        return toDomain(repository.save(toEntity(request)));
    }

    @Override
    public List<ConfirmationRequest> findAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ConfirmationRequest> findByPlanId(String planId) {
        return repository.findByPlanIdOrderByCreatedAtAsc(planId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ConfirmationRequest> findByStatus(ConfirmationStatus status) {
        return repository.findByStatusOrderByCreatedAtDesc(status.name()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<ConfirmationRequest> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    private ConfirmationRequestEntity toEntity(ConfirmationRequest request) {
        ConfirmationRequestEntity entity = new ConfirmationRequestEntity();
        entity.setId(request.id());
        entity.setPlanId(request.planId());
        entity.setAction(request.action());
        entity.setStatus(request.status().name());
        entity.setComment(request.comment());
        entity.setCreatedAt(request.createdAt());
        return entity;
    }

    private ConfirmationRequest toDomain(ConfirmationRequestEntity entity) {
        return new ConfirmationRequest(
                entity.getId(),
                entity.getPlanId(),
                entity.getAction(),
                ConfirmationStatus.valueOf(entity.getStatus()),
                entity.getComment(),
                entity.getCreatedAt()
        );
    }
}
