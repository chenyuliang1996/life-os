package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.RequestTraceEvent;
import io.github.yuliangchen.lifeos.domain.repository.RequestTraceRepository;
import io.github.yuliangchen.lifeos.infra.persistence.entity.RequestTraceEventEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataRequestTraceEventEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseRequestTraceRepository implements RequestTraceRepository {

    private static final int TRACE_RETENTION_DAYS = 30;

    private final SpringDataRequestTraceEventEntityRepository repository;

    public DatabaseRequestTraceRepository(SpringDataRequestTraceEventEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public RequestTraceEvent save(RequestTraceEvent event) {
        cleanupExpired();
        return toDomain(repository.save(toEntity(event)));
    }

    @Override
    public List<RequestTraceEvent> findRecent(int limit) {
        cleanupExpired();
        return repository.findByOrderByCreatedAtDesc(page(limit)).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<RequestTraceEvent> findRecentByUserId(String userId, int limit) {
        cleanupExpired();
        return repository.findByUserIdOrderByCreatedAtDesc(normalize(userId), page(limit)).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<RequestTraceEvent> findRecentBySessionId(String sessionId, int limit) {
        cleanupExpired();
        return repository.findBySessionIdOrderByCreatedAtDesc(normalize(sessionId), page(limit)).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<RequestTraceEvent> findRecentByContextId(String contextId, int limit) {
        cleanupExpired();
        return repository.findByContextIdOrderByCreatedAtDesc(normalize(contextId), page(limit)).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<RequestTraceEvent> findRecentByTraceId(String traceId, int limit) {
        cleanupExpired();
        return repository.findByTraceIdOrderByCreatedAtDesc(normalize(traceId), page(limit)).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<RequestTraceEvent> findRecentByOperation(String operation, int limit) {
        cleanupExpired();
        return repository.findByOperationOrderByCreatedAtDesc(normalize(operation), page(limit)).stream()
                .map(this::toDomain)
                .toList();
    }

    private RequestTraceEventEntity toEntity(RequestTraceEvent event) {
        RequestTraceEventEntity entity = new RequestTraceEventEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCreatedAt(event.timestamp() == null ? Instant.now() : event.timestamp());
        entity.setOperation(normalize(event.operation()));
        entity.setUserId(normalize(event.userId()));
        entity.setThreadId(normalize(event.threadId()));
        entity.setSessionId(normalize(event.sessionId()));
        entity.setContextId(normalize(event.contextId()));
        entity.setTraceId(normalize(event.traceId()));
        entity.setSurface(normalize(event.surface()));
        entity.setLocale(normalize(event.locale()));
        entity.setSuccess(event.success());
        entity.setDurationMs(Math.max(0L, event.durationMs()));
        return entity;
    }

    private RequestTraceEvent toDomain(RequestTraceEventEntity entity) {
        return new RequestTraceEvent(
                entity.getCreatedAt(),
                entity.getOperation(),
                entity.getUserId(),
                entity.getThreadId(),
                entity.getSessionId(),
                entity.getContextId(),
                entity.getTraceId(),
                entity.getSurface(),
                entity.getLocale(),
                entity.isSuccess(),
                entity.getDurationMs()
        );
    }

    private PageRequest page(int limit) {
        return PageRequest.of(0, Math.max(1, Math.min(limit, 500)));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "unknown" : value;
    }

    private void cleanupExpired() {
        Instant retentionEdge = Instant.now().minus(TRACE_RETENTION_DAYS, ChronoUnit.DAYS);
        repository.deleteByCreatedAtBefore(retentionEdge);
    }
}
