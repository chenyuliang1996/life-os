package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record ExecutionContinuationResult(
        boolean resumed,
        boolean blocked,
        String message,
        ExecutionRun executionRun,
        LifePlan plan,
        List<ConfirmationRequest> confirmations
) {
}
