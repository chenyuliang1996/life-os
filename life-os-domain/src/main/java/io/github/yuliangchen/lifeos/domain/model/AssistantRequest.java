package io.github.yuliangchen.lifeos.domain.model;

public record AssistantRequest(
        String userId,
        String threadId,
        String input,
        String locale,
        String sessionId,
        String contextId,
        String traceId
) {
}
