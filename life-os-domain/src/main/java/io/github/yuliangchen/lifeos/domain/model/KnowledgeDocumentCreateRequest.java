package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record KnowledgeDocumentCreateRequest(
        String title,
        String sourceType,
        List<String> tags,
        String summary,
        String content,
        String locale
) {
}
