package io.github.yuliangchen.lifeos.domain.model;

public record ToolPolicyStatus(
        String capability,
        String accessLevel,
        String reason
) {
}
