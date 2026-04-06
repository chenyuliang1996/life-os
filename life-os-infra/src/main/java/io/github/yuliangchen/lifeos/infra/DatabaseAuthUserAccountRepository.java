package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.AuthUserAccount;
import io.github.yuliangchen.lifeos.domain.repository.AuthUserAccountRepository;
import io.github.yuliangchen.lifeos.infra.persistence.entity.AuthUserAccountEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataAuthUserAccountEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseAuthUserAccountRepository implements AuthUserAccountRepository {

    private final SpringDataAuthUserAccountEntityRepository repository;

    public DatabaseAuthUserAccountRepository(SpringDataAuthUserAccountEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuthUserAccount save(AuthUserAccount account) {
        return toDomain(repository.save(toEntity(account)));
    }

    @Override
    public Optional<AuthUserAccount> findByUserId(String userId) {
        return repository.findById(userId).map(this::toDomain);
    }

    @Override
    public Optional<AuthUserAccount> findByUsername(String username) {
        return repository.findByUsername(username).map(this::toDomain);
    }

    private AuthUserAccountEntity toEntity(AuthUserAccount account) {
        AuthUserAccountEntity entity = new AuthUserAccountEntity();
        entity.setUserId(account.userId());
        entity.setUsername(account.username());
        entity.setPasswordHash(account.passwordHash());
        entity.setDisplayName(account.displayName());
        entity.setLocale(account.locale());
        entity.setEnabled(account.enabled());
        entity.setCreatedAt(account.createdAt());
        entity.setLastLoginAt(account.lastLoginAt());
        return entity;
    }

    private AuthUserAccount toDomain(AuthUserAccountEntity entity) {
        return new AuthUserAccount(
                entity.getUserId(),
                entity.getUsername(),
                entity.getPasswordHash(),
                entity.getDisplayName(),
                entity.getLocale(),
                entity.isEnabled(),
                entity.getCreatedAt(),
                entity.getLastLoginAt()
        );
    }
}
