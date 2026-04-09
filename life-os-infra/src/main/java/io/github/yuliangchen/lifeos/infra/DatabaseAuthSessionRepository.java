package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.AuthSession;
import io.github.yuliangchen.lifeos.domain.repository.AuthSessionRepository;
import io.github.yuliangchen.lifeos.infra.persistence.entity.AuthSessionEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataAuthSessionEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
/**
 * 基于数据库的会话仓储实现。
 * Database-backed repository for auth session persistence.
 */
public class DatabaseAuthSessionRepository implements AuthSessionRepository {

    private final SpringDataAuthSessionEntityRepository repository;

    /**
     * 构造数据库会话仓储。
     * Constructs database auth session repository.
     */
    public DatabaseAuthSessionRepository(SpringDataAuthSessionEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    /**
     * 保存或更新会话记录。
     * Persists or updates an auth session.
     */
    public AuthSession save(AuthSession session) {
        return toDomain(repository.save(toEntity(session)));
    }

    @Override
    /**
     * 根据 token 查询会话。
     * Finds session by token.
     */
    public Optional<AuthSession> findByToken(String token) {
        return repository.findById(token).map(this::toDomain);
    }

    @Override
    /**
     * 撤销指定 token 会话。
     * Revokes session identified by token.
     */
    public void revoke(String token) {
        repository.findById(token).ifPresent(entity -> {
            entity.setRevoked(true);
            entity.setLastSeenAt(Instant.now());
            repository.save(entity);
        });
    }

    @Override
    /**
     * 删除已过期会话。
     * Removes expired sessions from persistent storage.
     */
    @Transactional
    public void deleteExpired() {
        repository.deleteByExpiresAtBefore(Instant.now());
    }

    /**
     * 将领域对象转换为持久化实体。
     * Converts domain session into JPA entity.
     */
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

    /**
     * 将持久化实体转换为领域对象。
     * Converts JPA entity back into domain session model.
     */
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
