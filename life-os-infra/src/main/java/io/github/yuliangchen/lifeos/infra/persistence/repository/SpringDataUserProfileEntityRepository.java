package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserProfileEntityRepository extends JpaRepository<UserProfileEntity, String> {
}
