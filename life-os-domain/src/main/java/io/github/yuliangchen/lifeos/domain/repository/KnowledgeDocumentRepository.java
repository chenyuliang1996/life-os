package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;

import java.util.List;

public interface KnowledgeDocumentRepository {

    KnowledgeDocument save(KnowledgeDocument document);

    List<KnowledgeDocument> findAll();

    List<KnowledgeDocument> findAllForUser(String userId);
}
