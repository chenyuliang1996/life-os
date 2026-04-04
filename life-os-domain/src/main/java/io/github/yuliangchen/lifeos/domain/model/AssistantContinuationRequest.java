package io.github.yuliangchen.lifeos.domain.model;

public record AssistantContinuationRequest(
        String userId,
        String planId,
        String locale,
        String sessionId,
        String contextId,
        String traceId
) {
}
