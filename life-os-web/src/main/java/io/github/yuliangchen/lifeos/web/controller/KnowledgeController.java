package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocumentCreateRequest;
import io.github.yuliangchen.lifeos.rag.KnowledgeModuleFacade;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import io.github.yuliangchen.lifeos.web.security.LifeOsSecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/knowledge/documents")
public class KnowledgeController {

    private final KnowledgeModuleFacade knowledgeModuleFacade;
    private final LifeOsObservabilityService lifeOsObservabilityService;
    private final LifeOsSecurityService lifeOsSecurityService;

    public KnowledgeController(KnowledgeModuleFacade knowledgeModuleFacade,
                               LifeOsObservabilityService lifeOsObservabilityService,
                               LifeOsSecurityService lifeOsSecurityService) {
        this.knowledgeModuleFacade = knowledgeModuleFacade;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
        this.lifeOsSecurityService = lifeOsSecurityService;
    }

    @GetMapping
    public List<KnowledgeDocument> listDocuments(@RequestParam(defaultValue = "lifeos-user") String userId) {
        return knowledgeModuleFacade.listDocuments(userId);
    }

    @PostMapping
    public KnowledgeDocument addDocument(@RequestParam(defaultValue = "lifeos-user") String userId,
                                         @RequestBody KnowledgeDocumentCreateRequest request) {
        long startedAt = System.nanoTime();
        try {
            KnowledgeDocument document = new KnowledgeDocument(
                    UUID.randomUUID().toString(),
                    userId,
                    request.title(),
                    request.sourceType() == null || request.sourceType().isBlank() ? "manual" : request.sourceType(),
                    request.tags() == null ? List.of() : request.tags(),
                    request.summary(),
                    request.content() == null || request.content().isBlank() ? request.summary() : request.content(),
                    request.locale() == null || request.locale().isBlank() ? "en-US" : request.locale(),
                    Instant.now()
            );
            KnowledgeDocument saved = knowledgeModuleFacade.addDocument(document);
            lifeOsObservabilityService.recordKnowledgeWrite(elapsedMillis(startedAt), true);
            lifeOsSecurityService.recordAudit(
                    userId,
                    "n/a",
                    "knowledge",
                    "write",
                    saved.id(),
                    "SUCCESS",
                    "Persisted manual document: " + saved.title()
            );
            return saved;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordKnowledgeWrite(elapsedMillis(startedAt), false);
            lifeOsSecurityService.recordAudit(
                    userId,
                    "n/a",
                    "knowledge",
                    "write",
                    "manual",
                    "FAILURE",
                    exception.getMessage()
            );
            throw exception;
        }
    }

    private long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
