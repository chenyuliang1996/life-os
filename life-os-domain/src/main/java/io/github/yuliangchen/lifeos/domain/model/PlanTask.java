package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;

public record PlanTask(
        String id,
        String title,
        String description,
        TaskStatus status,
        String owner,
        Instant dueAt
) {
}
