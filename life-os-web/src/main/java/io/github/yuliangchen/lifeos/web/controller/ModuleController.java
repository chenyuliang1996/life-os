package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.agents.AgentsModuleFacade;
import io.github.yuliangchen.lifeos.domain.DomainModuleFacade;
import io.github.yuliangchen.lifeos.infra.InfrastructureModuleFacade;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import io.github.yuliangchen.lifeos.orchestrator.LifeOrchestrator;
import io.github.yuliangchen.lifeos.rag.KnowledgeModuleFacade;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import io.github.yuliangchen.lifeos.web.WebModuleFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController {

    private final DomainModuleFacade domainModuleFacade;
    private final AgentsModuleFacade agentsModuleFacade;
    private final MemoryModuleFacade memoryModuleFacade;
    private final KnowledgeModuleFacade knowledgeModuleFacade;
    private final ToolModuleFacade toolModuleFacade;
    private final LifeOrchestrator lifeOrchestrator;
    private final InfrastructureModuleFacade infrastructureModuleFacade;
    private final WebModuleFacade webModuleFacade;

    public ModuleController(DomainModuleFacade domainModuleFacade,
                            AgentsModuleFacade agentsModuleFacade,
                            MemoryModuleFacade memoryModuleFacade,
                            KnowledgeModuleFacade knowledgeModuleFacade,
                            ToolModuleFacade toolModuleFacade,
                            LifeOrchestrator lifeOrchestrator,
                            InfrastructureModuleFacade infrastructureModuleFacade,
                            WebModuleFacade webModuleFacade) {
        this.domainModuleFacade = domainModuleFacade;
        this.agentsModuleFacade = agentsModuleFacade;
        this.memoryModuleFacade = memoryModuleFacade;
        this.knowledgeModuleFacade = knowledgeModuleFacade;
        this.toolModuleFacade = toolModuleFacade;
        this.lifeOrchestrator = lifeOrchestrator;
        this.infrastructureModuleFacade = infrastructureModuleFacade;
        this.webModuleFacade = webModuleFacade;
    }

    @GetMapping
    public Map<String, String> demos() {
        return Map.of(
                "domain", domainModuleFacade.executeDemo(),
                "agents", agentsModuleFacade.executeDemo(),
                "memory", memoryModuleFacade.executeDemo(),
                "rag", knowledgeModuleFacade.executeDemo(),
                "tools", toolModuleFacade.executeDemo(),
                "orchestrator", lifeOrchestrator.executeDemo(),
                "infra", infrastructureModuleFacade.executeDemo(),
                "web", webModuleFacade.executeDemo()
        );
    }
}
