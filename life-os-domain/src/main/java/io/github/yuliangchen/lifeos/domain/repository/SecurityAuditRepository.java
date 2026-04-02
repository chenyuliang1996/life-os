package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.SecurityAuditEntry;

import java.util.List;

public interface SecurityAuditRepository {

    SecurityAuditEntry save(SecurityAuditEntry entry);

    List<SecurityAuditEntry> findRecent(int limit);

    List<SecurityAuditEntry> findRecentByUserId(String userId, int limit);
}
