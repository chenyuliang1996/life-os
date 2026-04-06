package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.AuthUserAccount;
import io.github.yuliangchen.lifeos.domain.repository.AuthUserAccountRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "memory")
public class InMemoryAuthUserAccountRepository implements AuthUserAccountRepository {

    private final ConcurrentHashMap<String, AuthUserAccount> byUserId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> userIdByUsername = new ConcurrentHashMap<>();

    @Override
    public AuthUserAccount save(AuthUserAccount account) {
        byUserId.put(account.userId(), account);
        userIdByUsername.put(account.username(), account.userId());
        return account;
    }

    @Override
    public Optional<AuthUserAccount> findByUserId(String userId) {
        return Optional.ofNullable(byUserId.get(userId));
    }

    @Override
    public Optional<AuthUserAccount> findByUsername(String username) {
        return Optional.ofNullable(userIdByUsername.get(username))
                .flatMap(this::findByUserId);
    }
}
