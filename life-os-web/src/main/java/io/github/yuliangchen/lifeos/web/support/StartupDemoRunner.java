package io.github.yuliangchen.lifeos.web.support;

import io.github.yuliangchen.lifeos.agents.AgentsModuleFacade;
import io.github.yuliangchen.lifeos.domain.DomainModuleFacade;
import io.github.yuliangchen.lifeos.infra.InfrastructureModuleFacade;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import io.github.yuliangchen.lifeos.orchestrator.LifeOrchestrator;
import io.github.yuliangchen.lifeos.rag.KnowledgeModuleFacade;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupDemoRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupDemoRunner.class);

    @Bean
    ApplicationRunner applicationRunner(DomainModuleFacade domainModuleFacade,
                                        AgentsModuleFacade agentsModuleFacade,
                                        MemoryModuleFacade memoryModuleFacade,
                                        KnowledgeModuleFacade knowledgeModuleFacade,
                                        ToolModuleFacade toolModuleFacade,
                                        LifeOrchestrator lifeOrchestrator,
                                        InfrastructureModuleFacade infrastructureModuleFacade) {
        return args -> {
            log.info("domain -> {}", domainModuleFacade.executeDemo());
            log.info("agents -> {}", agentsModuleFacade.executeDemo());
            log.info("memory -> {}", memoryModuleFacade.executeDemo());
            log.info("rag -> {}", knowledgeModuleFacade.executeDemo());
            log.info("tools -> {}", toolModuleFacade.executeDemo());
            log.info("infra -> {}", infrastructureModuleFacade.executeDemo());
            log.info("orchestrator -> {}", lifeOrchestrator.executeDemo());
        };
    }
}
