package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.rag.KnowledgeModuleFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/knowledge/documents")
public class KnowledgeController {

    private final KnowledgeModuleFacade knowledgeModuleFacade;

    public KnowledgeController(KnowledgeModuleFacade knowledgeModuleFacade) {
        this.knowledgeModuleFacade = knowledgeModuleFacade;
    }

    @GetMapping
    public List<KnowledgeDocument> listDocuments() {
        return knowledgeModuleFacade.listDocuments();
    }

    @PostMapping
    public KnowledgeDocument addDocument(@RequestBody KnowledgeDocument request) {
        KnowledgeDocument document = new KnowledgeDocument(
                request.id() == null || request.id().isBlank() ? UUID.randomUUID().toString() : request.id(),
                request.title(),
                request.sourceType(),
                request.tags(),
                request.summary(),
                Instant.now()
        );
        return knowledgeModuleFacade.addDocument(document);
    }
}
