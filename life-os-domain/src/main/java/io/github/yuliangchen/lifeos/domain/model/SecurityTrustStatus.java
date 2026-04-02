package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record SecurityTrustStatus(
        String userId,
        String personaId,
        String trustTier,
        boolean workspaceTrusted,
        boolean mcpTrusted,
        boolean outboundNetworkAllowed,
        boolean writeRequiresApproval,
        List<String> outboundAllowlist,
        String summary
) {
}
