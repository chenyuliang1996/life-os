package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record OrchestrationResult(
        ExecutionRun executionRun,
        LifePlan plan,
        List<AgentContribution> contributions,
        List<ConfirmationRequest> confirmations
) {
}
