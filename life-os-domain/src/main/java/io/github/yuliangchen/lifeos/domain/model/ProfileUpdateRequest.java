package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;
import java.util.Map;

public record ProfileUpdateRequest(
        Map<String, String> preferences,
        List<Goal> goals
) {
}
