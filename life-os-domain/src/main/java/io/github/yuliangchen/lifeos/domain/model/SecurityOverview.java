package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record SecurityOverview(
        String userId,
        SecurityTrustStatus trust,
        List<ToolPolicyStatus> policies,
        List<SecurityAuditEntry> recentAuditEntries
) {
}
