package io.github.yuliangchen.lifeos.domain.model;

public record PlanPreviewRequest(
        String userId,
        String threadId,
        String input
) {
}
