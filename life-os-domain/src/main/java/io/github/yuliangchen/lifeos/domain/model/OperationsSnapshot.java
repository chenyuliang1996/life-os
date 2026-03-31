package io.github.yuliangchen.lifeos.domain.model;

public record OperationsSnapshot(
        ServiceLevelTarget target,
        double currentQps,
        double requestsPerMinute,
        long totalRequests,
        double successRate,
        long pendingConfirmations,
        double assistantP95Ms,
        double previewP95Ms,
        double resumeP95Ms,
        double confirmationP95Ms,
        double uxBootstrapP95Ms,
        double uxInteractionP95Ms,
        boolean withinCapacity,
        boolean withinSlo,
        String summary
) {
}
