package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.AuthSession;
import io.github.yuliangchen.lifeos.domain.repository.AuthSessionRepository;
import io.github.yuliangchen.lifeos.infra.persistence.entity.AuthSessionEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataAuthSessionEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseAuthSessionRepository implements AuthSessionRepository {

    private final SpringDataAuthSessionEntityRepository repository;

    public DatabaseAuthSessionRepository(SpringDataAuthSessionEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuthSession save(AuthSession session) {
        return toDomain(repository.save(toEntity(session)));
    }

    @Override
    public Optional<AuthSession> findByToken(String token) {
        return repository.findById(token).map(this::toDomain);
    }

    @Override
    public void revoke(String token) {
        repository.findById(token).ifPresent(entity -> {
            entity.setRevoked(true);
            entity.setLastSeenAt(Instant.now());
            repository.save(entity);
        });
    }

    @Override
    public void deleteExpired() {
        repository.deleteByExpiresAtBefore(Instant.now());
    }

    private AuthSessionEntity toEntity(AuthSession session) {
        AuthSessionEntity entity = new AuthSessionEntity();
        entity.setToken(session.token());
        entity.setUserId(session.userId());
        entity.setIssuedAt(session.issuedAt());
        entity.setExpiresAt(session.expiresAt());
        entity.setLastSeenAt(session.lastSeenAt());
        entity.setRevoked(session.revoked());
        return entity;
    }

    private AuthSession toDomain(AuthSessionEntity entity) {
        return new AuthSession(
                entity.getToken(),
                entity.getUserId(),
                entity.getIssuedAt(),
                entity.getExpiresAt(),
                entity.getLastSeenAt(),
                entity.isRevoked()
        );
    }
}
