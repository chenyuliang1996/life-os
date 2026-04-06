package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.AuthSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SpringDataAuthSessionEntityRepository extends JpaRepository<AuthSessionEntity, String> {

    List<AuthSessionEntity> findByExpiresAtBefore(Instant instant);

    long deleteByExpiresAtBefore(Instant instant);
}
