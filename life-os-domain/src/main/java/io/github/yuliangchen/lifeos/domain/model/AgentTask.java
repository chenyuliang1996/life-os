package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record AgentTask(
        SpecialistType specialistType,
        String objective,
        List<String> constraints
) {
}
