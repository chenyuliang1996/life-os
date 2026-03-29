package io.github.yuliangchen.lifeos.rag;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeQuery;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeSnippet;
import io.github.yuliangchen.lifeos.domain.repository.KnowledgeDocumentRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class KnowledgeModuleFacade implements ModuleExecutable<KnowledgeQuery, List<KnowledgeSnippet>> {

    private final KnowledgeDocumentRepository knowledgeDocumentRepository;

    public KnowledgeModuleFacade(KnowledgeDocumentRepository knowledgeDocumentRepository) {
        this.knowledgeDocumentRepository = knowledgeDocumentRepository;
        seedDocuments();
    }

    @Override
    public String moduleName() {
        return "life-os-rag";
    }

    @Override
    public List<KnowledgeSnippet> execute(KnowledgeQuery input) {
        String loweredQuery = input.query().toLowerCase(Locale.ROOT);
        return knowledgeDocumentRepository.findAll().stream()
                .filter(document -> document.title().toLowerCase(Locale.ROOT).contains(loweredQuery)
                        || document.summary().toLowerCase(Locale.ROOT).contains(loweredQuery)
                        || input.tags().stream().anyMatch(document.tags()::contains))
                .limit(3)
                .map(document -> new KnowledgeSnippet(
                        document.id(),
                        document.title(),
                        document.summary()
                ))
                .toList();
    }

    public KnowledgeDocument addDocument(KnowledgeDocument document) {
        return knowledgeDocumentRepository.save(document);
    }

    public List<KnowledgeDocument> listDocuments() {
        return knowledgeDocumentRepository.findAll();
    }

    @Override
    public String executeDemo() {
        List<KnowledgeSnippet> snippets = execute(new KnowledgeQuery("demo-user", "tokyo", List.of("travel")));
        return "RAG ready with " + snippets.size() + " demo snippets";
    }

    private void seedDocuments() {
        if (!knowledgeDocumentRepository.findAll().isEmpty()) {
            return;
        }

        addDocument(new KnowledgeDocument(
                UUID.randomUUID().toString(),
                "Tokyo Relaxed Itinerary",
                "seed",
                List.of("travel", "tokyo"),
                "A gentle 7-day Tokyo itinerary with low-intensity pacing and neighborhood-based planning.",
                Instant.now()
        ));
        addDocument(new KnowledgeDocument(
                UUID.randomUUID().toString(),
                "English Sprint Notes",
                "seed",
                List.of("learning", "english"),
                "A weekday plan for keeping English listening practice during travel-heavy weeks.",
                Instant.now()
        ));
    }
}
