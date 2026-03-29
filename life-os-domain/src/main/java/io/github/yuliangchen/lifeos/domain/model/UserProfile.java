package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record UserProfile(
        String userId,
        Map<String, String> preferences,
        List<Goal> goals,
        Instant updatedAt
) {
}
