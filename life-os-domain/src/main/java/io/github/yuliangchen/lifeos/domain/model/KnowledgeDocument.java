package io.github.yuliangchen.lifeos.domain.model;

import java.time.Instant;
import java.util.List;

public record KnowledgeDocument(
        String id,
        String userId,
        String title,
        String sourceType,
        List<String> tags,
        String summary,
        String content,
        String locale,
        Instant updatedAt
) {
}
