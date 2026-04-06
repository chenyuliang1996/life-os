package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record AuthLoginResponse(
        String token,
        String userId,
        String username,
        String displayName,
        String locale,
        Instant expiresAt
) {
}

