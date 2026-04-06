package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.RequestTraceEvent;
import io.github.yuliangchen.lifeos.domain.repository.RequestTraceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "memory")
public class InMemoryRequestTraceRepository implements RequestTraceRepository {

    private static final int MAX_SIZE = 5_000;
    private static final int TRACE_RETENTION_DAYS = 30;

    private final Deque<RequestTraceEvent> store = new ArrayDeque<>();
    private final Object monitor = new Object();

    @Override
    public RequestTraceEvent save(RequestTraceEvent event) {
        synchronized (monitor) {
            cleanupExpired();
            store.addLast(event);
            while (store.size() > MAX_SIZE) {
                store.removeFirst();
            }
        }
        return event;
    }

    @Override
    public List<RequestTraceEvent> findRecent(int limit) {
        return query(limit, event -> true);
    }

    @Override
    public List<RequestTraceEvent> findRecentByUserId(String userId, int limit) {
        String normalized = normalize(userId);
        return query(limit, event -> normalized.equals(normalize(event.userId())));
    }

    @Override
    public List<RequestTraceEvent> findRecentBySessionId(String sessionId, int limit) {
        String normalized = normalize(sessionId);
        return query(limit, event -> normalized.equals(normalize(event.sessionId())));
    }

    @Override
    public List<RequestTraceEvent> findRecentByContextId(String contextId, int limit) {
        String normalized = normalize(contextId);
        return query(limit, event -> normalized.equals(normalize(event.contextId())));
    }

    @Override
    public List<RequestTraceEvent> findRecentByTraceId(String traceId, int limit) {
        String normalized = normalize(traceId);
        return query(limit, event -> normalized.equals(normalize(event.traceId())));
    }

    @Override
    public List<RequestTraceEvent> findRecentByOperation(String operation, int limit) {
        String normalized = normalize(operation);
        return query(limit, event -> normalized.equals(normalize(event.operation())));
    }

    private List<RequestTraceEvent> query(int limit, java.util.function.Predicate<RequestTraceEvent> predicate) {
        synchronized (monitor) {
            cleanupExpired();
            return store.stream()
                    .filter(predicate)
                    .sorted(Comparator.comparing(RequestTraceEvent::timestamp).reversed())
                    .limit(Math.max(1, Math.min(limit, 500)))
                    .toList();
        }
    }

    private void cleanupExpired() {
        Instant edge = Instant.now().minus(TRACE_RETENTION_DAYS, ChronoUnit.DAYS);
        while (!store.isEmpty()) {
            RequestTraceEvent first = store.peekFirst();
            if (first == null || !first.timestamp().isBefore(edge)) {
                return;
            }
            store.removeFirst();
        }
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "unknown" : value;
    }
}
