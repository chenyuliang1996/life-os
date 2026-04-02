package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record SecurityAuditEntry(
        String id,
        String userId,
        String threadId,
        String category,
        String action,
        String target,
        String outcome,
        String detail,
        Instant createdAt
) {
}
