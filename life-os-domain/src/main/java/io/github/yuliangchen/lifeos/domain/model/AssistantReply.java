package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record AssistantReply(
        String threadId,
        String mode,
        String message,
        String planId,
        String runId,
        List<String> highlights
) {
}
