package io.github.yuliangchen.lifeos.domain.model;

public record UxTelemetryEvent(
        String action,
        String surface,
        String locale,
        String userId,
        String threadId,
        String sessionId,
        String contextId,
        String traceId,
        long durationMs,
        boolean success
) {
}
