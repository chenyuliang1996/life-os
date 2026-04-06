package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record AuthSession(
        String token,
        String userId,
        Instant issuedAt,
        Instant expiresAt,
        Instant lastSeenAt,
        boolean revoked
) {
}

