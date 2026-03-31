package io.github.yuliangchen.lifeos.domain.model;

public record UxTelemetryEvent(
        String action,
        String surface,
        String locale,
        long durationMs,
        boolean success
) {
}
