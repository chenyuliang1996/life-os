package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.AuthSession;

import java.util.Optional;

public interface AuthSessionRepository {

    AuthSession save(AuthSession session);

    Optional<AuthSession> findByToken(String token);

    void revoke(String token);

    void deleteExpired();
}

