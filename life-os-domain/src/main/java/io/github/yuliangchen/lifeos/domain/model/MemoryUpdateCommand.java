package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;
import java.util.Map;

public record MemoryUpdateCommand(
        String userId,
        Map<String, String> preferences,
        List<Goal> goals
) {
}
