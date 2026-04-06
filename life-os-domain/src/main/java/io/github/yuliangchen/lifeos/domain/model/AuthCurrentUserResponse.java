package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record AuthCurrentUserResponse(
        String userId,
        String username,
        String displayName,
        String locale,
        Instant sessionExpiresAt,
        Instant lastLoginAt
) {
}
