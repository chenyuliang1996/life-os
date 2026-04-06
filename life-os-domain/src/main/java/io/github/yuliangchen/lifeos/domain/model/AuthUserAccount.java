package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record AuthUserAccount(
        String userId,
        String username,
        String passwordHash,
        String displayName,
        String locale,
        boolean enabled,
        Instant createdAt,
        Instant lastLoginAt
) {
}

