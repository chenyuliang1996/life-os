package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.SecurityAuditEntry;
import io.github.yuliangchen.lifeos.domain.repository.SecurityAuditRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "memory")
public class InMemorySecurityAuditRepository implements SecurityAuditRepository {

    private final ConcurrentHashMap<String, SecurityAuditEntry> store = new ConcurrentHashMap<>();

    @Override
    public SecurityAuditEntry save(SecurityAuditEntry entry) {
        store.put(entry.id(), entry);
        return entry;
    }

    @Override
    public List<SecurityAuditEntry> findRecent(int limit) {
        return store.values().stream()
                .sorted(Comparator.comparing(SecurityAuditEntry::createdAt).reversed())
                .limit(sanitize(limit))
                .toList();
    }

    @Override
    public List<SecurityAuditEntry> findRecentByUserId(String userId, int limit) {
        return store.values().stream()
                .filter(entry -> entry.userId().equals(userId))
                .sorted(Comparator.comparing(SecurityAuditEntry::createdAt).reversed())
                .limit(sanitize(limit))
                .toList();
    }

    private long sanitize(int limit) {
        return Math.max(1, Math.min(limit, 50));
    }
}
