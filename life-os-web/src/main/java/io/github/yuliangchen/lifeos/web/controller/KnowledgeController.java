package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocumentCreateRequest;
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
    public KnowledgeDocument addDocument(@RequestBody KnowledgeDocumentCreateRequest request) {
        KnowledgeDocument document = new KnowledgeDocument(
                UUID.randomUUID().toString(),
                request.title(),
                request.sourceType() == null || request.sourceType().isBlank() ? "manual" : request.sourceType(),
                request.tags() == null ? List.of() : request.tags(),
                request.summary(),
                request.content() == null || request.content().isBlank() ? request.summary() : request.content(),
                request.locale() == null || request.locale().isBlank() ? "en-US" : request.locale(),
                Instant.now()
        );
        return knowledgeModuleFacade.addDocument(document);
    }
}
