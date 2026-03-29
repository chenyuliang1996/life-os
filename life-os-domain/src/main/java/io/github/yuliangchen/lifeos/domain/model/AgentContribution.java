package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;
import java.util.Map;

public record AgentContribution(
        String agentName,
        String summary,
        List<PlanTask> suggestedTasks,
        Map<String, Object> metadata
) {
}
