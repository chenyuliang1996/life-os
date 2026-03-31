package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeQuery;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeSnippet;
import io.github.yuliangchen.lifeos.domain.model.RagRuntimeStatus;
import io.github.yuliangchen.lifeos.domain.service.VectorKnowledgeStore;

import java.util.List;

public class NoOpVectorKnowledgeStore implements VectorKnowledgeStore {

    @Override
    public RagRuntimeStatus status() {
        return new RagRuntimeStatus(
                false,
                false,
                "text-only",
                "database-text",
                "none",
                "none",
                0,
                "Vector retrieval is not configured, so the app uses persisted text retrieval only."
        );
    }

    @Override
    public void upsert(KnowledgeDocument document) {
        // Intentionally empty. Text-mode retrieval uses the relational store only.
    }

    @Override
    public List<KnowledgeSnippet> search(KnowledgeQuery query, int limit) {
        return List.of();
    }
}
