package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.repository.KnowledgeDocumentRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryKnowledgeDocumentRepository implements KnowledgeDocumentRepository {

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
}
