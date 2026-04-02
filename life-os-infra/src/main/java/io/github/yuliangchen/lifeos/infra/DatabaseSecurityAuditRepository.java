package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.SecurityAuditEntry;
import io.github.yuliangchen.lifeos.domain.repository.SecurityAuditRepository;
import io.github.yuliangchen.lifeos.infra.persistence.entity.SecurityAuditEntryEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataSecurityAuditEntryEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseSecurityAuditRepository implements SecurityAuditRepository {

    private final SpringDataSecurityAuditEntryEntityRepository repository;

    public DatabaseSecurityAuditRepository(SpringDataSecurityAuditEntryEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public SecurityAuditEntry save(SecurityAuditEntry entry) {
        return toDomain(repository.save(toEntity(entry)));
    }

    @Override
    public List<SecurityAuditEntry> findRecent(int limit) {
        return repository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, sanitize(limit))).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<SecurityAuditEntry> findRecentByUserId(String userId, int limit) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, sanitize(limit))).stream()
                .map(this::toDomain)
                .toList();
    }

    private int sanitize(int limit) {
        return Math.max(1, Math.min(limit, 50));
    }

    private SecurityAuditEntryEntity toEntity(SecurityAuditEntry entry) {
        SecurityAuditEntryEntity entity = new SecurityAuditEntryEntity();
        entity.setId(entry.id());
        entity.setUserId(entry.userId());
        entity.setThreadId(entry.threadId());
        entity.setCategory(entry.category());
        entity.setAction(entry.action());
        entity.setTarget(entry.target());
        entity.setOutcome(entry.outcome());
        entity.setDetail(entry.detail());
        entity.setCreatedAt(entry.createdAt());
        return entity;
    }

    private SecurityAuditEntry toDomain(SecurityAuditEntryEntity entity) {
        return new SecurityAuditEntry(
                entity.getId(),
                entity.getUserId(),
                entity.getThreadId(),
                entity.getCategory(),
                entity.getAction(),
                entity.getTarget(),
                entity.getOutcome(),
                entity.getDetail(),
                entity.getCreatedAt()
        );
    }
}
