package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record RequestTraceEvent(
        Instant timestamp,
        String operation,
        String userId,
        String threadId,
        String sessionId,
        String contextId,
        String traceId,
        String surface,
        String locale,
        boolean success,
        long durationMs
) {
}
