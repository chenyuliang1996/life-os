package io.github.yuliangchen.lifeos.domain.model;

public record ServiceLevelTarget(
        long dailyActiveUsers,
        double peakQps,
        double availabilityPercentage,
        double assistantP95Ms,
        double confirmationP95Ms,
        double uxBootstrapP95Ms,
        double uxInteractionP95Ms
) {
}
