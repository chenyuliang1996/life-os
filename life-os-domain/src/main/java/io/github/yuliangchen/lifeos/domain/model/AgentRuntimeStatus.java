package io.github.yuliangchen.lifeos.domain.model;

public record AgentRuntimeStatus(
        String mode,
        boolean modelBacked,
        String provider,
        String modelName,
        String summary
) {
}
