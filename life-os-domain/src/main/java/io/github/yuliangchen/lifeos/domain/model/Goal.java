package io.github.yuliangchen.lifeos.domain.model;

import java.time.LocalDate;

public record Goal(
        String id,
        GoalType type,
        String title,
        LocalDate targetDate
) {
}
