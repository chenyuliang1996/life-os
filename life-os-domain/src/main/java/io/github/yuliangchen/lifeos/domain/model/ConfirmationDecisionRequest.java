package io.github.yuliangchen.lifeos.domain.model;

public record ConfirmationDecisionRequest(
        String decision,
        String comment
) {
}
