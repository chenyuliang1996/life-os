package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;
import java.util.Map;

public record MemoryDetailsResponse(
        String userId,
        Map<String, String> preferences,
        List<Goal> goals,
        String summary,
        List<RequestTraceEvent> recentOperations
) {
}

