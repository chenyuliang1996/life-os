package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record ConfirmationRequest(
        String id,
        String planId,
        String action,
        ConfirmationStatus status,
        String comment,
        Instant createdAt
) {
}
