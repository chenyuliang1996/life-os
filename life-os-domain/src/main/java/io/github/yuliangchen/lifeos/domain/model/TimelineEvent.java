package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record TimelineEvent(
        String id,
        String runId,
        String eventType,
        String message,
        Instant createdAt
) {
}
