package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;
import java.util.Map;

public record LifePlan(
        String id,
        String userId,
        String title,
        String summary,
        PlanStatus status,
        List<PlanTask> tasks,
        Map<String, Object> metadata
) {
}
