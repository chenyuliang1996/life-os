package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.AuthUserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataAuthUserAccountEntityRepository extends JpaRepository<AuthUserAccountEntity, String> {

    Optional<AuthUserAccountEntity> findByUsername(String username);
}

