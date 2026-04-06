package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.AuthUserAccount;

import java.util.Optional;

public interface AuthUserAccountRepository {

    AuthUserAccount save(AuthUserAccount account);

    Optional<AuthUserAccount> findByUserId(String userId);

    Optional<AuthUserAccount> findByUsername(String username);
}

