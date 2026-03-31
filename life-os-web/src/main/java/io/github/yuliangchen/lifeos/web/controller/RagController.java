package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.RagRuntimeStatus;
import io.github.yuliangchen.lifeos.rag.KnowledgeModuleFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system/rag")
public class RagController {

    private final KnowledgeModuleFacade knowledgeModuleFacade;

    public RagController(KnowledgeModuleFacade knowledgeModuleFacade) {
        this.knowledgeModuleFacade = knowledgeModuleFacade;
    }

    @GetMapping
    public RagRuntimeStatus ragRuntimeStatus() {
        return knowledgeModuleFacade.ragRuntimeStatus();
    }
}
