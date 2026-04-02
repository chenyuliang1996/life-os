package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.SecurityAuditEntryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataSecurityAuditEntryEntityRepository extends JpaRepository<SecurityAuditEntryEntity, String> {

    List<SecurityAuditEntryEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<SecurityAuditEntryEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
}
