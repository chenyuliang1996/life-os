package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;
import java.util.List;

public record ExecutionRun(
        String id,
        String userId,
        String input,
        PlanStatus status,
        Instant createdAt,
        List<TimelineEvent> timeline
) {
}
