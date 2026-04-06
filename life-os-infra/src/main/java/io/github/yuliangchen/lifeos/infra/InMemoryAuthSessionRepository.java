package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.AuthSession;
import io.github.yuliangchen.lifeos.domain.repository.AuthSessionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "memory")
public class InMemoryAuthSessionRepository implements AuthSessionRepository {

    private final ConcurrentHashMap<String, AuthSession> store = new ConcurrentHashMap<>();

    @Override
    public AuthSession save(AuthSession session) {
        store.put(session.token(), session);
        return session;
    }

    @Override
    public Optional<AuthSession> findByToken(String token) {
        return Optional.ofNullable(store.get(token));
    }

    @Override
    public void revoke(String token) {
        store.computeIfPresent(token, (key, session) -> new AuthSession(
                session.token(),
                session.userId(),
                session.issuedAt(),
                session.expiresAt(),
                Instant.now(),
                true
        ));
    }

    @Override
    public void deleteExpired() {
        Instant now = Instant.now();
        store.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(now));
    }
}
