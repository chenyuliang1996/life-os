package io.github.yuliangchen.lifeos.domain.service;

import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeQuery;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeSnippet;
import io.github.yuliangchen.lifeos.domain.model.RagRuntimeStatus;

import java.util.List;

public interface VectorKnowledgeStore {

    RagRuntimeStatus status();

    void upsert(KnowledgeDocument document);

    List<KnowledgeSnippet> search(KnowledgeQuery query, int limit);
}
