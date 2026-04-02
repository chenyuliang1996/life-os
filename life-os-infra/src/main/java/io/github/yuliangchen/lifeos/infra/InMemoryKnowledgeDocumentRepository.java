package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.repository.KnowledgeDocumentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "memory")
public class InMemoryKnowledgeDocumentRepository implements KnowledgeDocumentRepository {

    public static final String GLOBAL_USER_ID = "system-seed";

    private final CopyOnWriteArrayList<KnowledgeDocument> store = new CopyOnWriteArrayList<>();

    @Override
    public KnowledgeDocument save(KnowledgeDocument document) {
        store.removeIf(existing -> existing.id().equals(document.id()));
        store.add(document);
        return document;
    }

    @Override
    public List<KnowledgeDocument> findAll() {
        return store.stream()
                .sorted(Comparator.comparing(KnowledgeDocument::updatedAt).reversed())
                .toList();
    }

    @Override
    public List<KnowledgeDocument> findAllForUser(String userId) {
        return store.stream()
                .filter(document -> document.userId().equals(GLOBAL_USER_ID) || document.userId().equals(userId))
                .sorted(Comparator.comparing(KnowledgeDocument::updatedAt).reversed())
                .toList();
    }
}
