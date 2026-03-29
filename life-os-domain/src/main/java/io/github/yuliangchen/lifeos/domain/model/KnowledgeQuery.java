package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record KnowledgeQuery(
        String userId,
        String query,
        List<String> tags
) {
}
