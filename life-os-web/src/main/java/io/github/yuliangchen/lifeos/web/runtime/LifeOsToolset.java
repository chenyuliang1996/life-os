package io.github.yuliangchen.lifeos.web.runtime;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeQuery;
import io.github.yuliangchen.lifeos.domain.model.PlanPreviewRequest;
import io.github.yuliangchen.lifeos.orchestrator.LifeOrchestrator;
import io.github.yuliangchen.lifeos.rag.KnowledgeModuleFacade;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import org.springframework.stereotype.Component;

@Component
public class LifeOsToolset {

    private final LifeOrchestrator lifeOrchestrator;
    private final KnowledgeModuleFacade knowledgeModuleFacade;
    private final ToolModuleFacade toolModuleFacade;

    public LifeOsToolset(LifeOrchestrator lifeOrchestrator,
                         KnowledgeModuleFacade knowledgeModuleFacade,
                         ToolModuleFacade toolModuleFacade) {
        this.lifeOrchestrator = lifeOrchestrator;
        this.knowledgeModuleFacade = knowledgeModuleFacade;
        this.toolModuleFacade = toolModuleFacade;
    }

    @Tool(name = "life_plan_preview", description = "Generate a grounded Life OS plan preview for a user's request.")
    public String lifePlanPreview(
            @ToolParam(name = "threadId", required = true, description = "Conversation thread id") String threadId,
            @ToolParam(name = "input", required = true, description = "User request to plan") String input) {
        var result = lifeOrchestrator.execute(new PlanPreviewRequest("demo-user", threadId, input));
        return """
                Plan title: %s
                Summary: %s
                Tasks: %s
                Pending confirmations: %d
                Plan id: %s
                Run id: %s
                """.formatted(
                result.plan().title(),
                result.plan().summary(),
                result.plan().tasks().stream().map(task -> task.title() + " by " + task.owner()).toList(),
                result.confirmations().size(),
                result.plan().id(),
                result.executionRun().id()
        );
    }

    @Tool(name = "search_personal_knowledge", description = "Search the seeded personal knowledge base for grounding.")
    public String searchPersonalKnowledge(
            @ToolParam(name = "query", required = true, description = "Knowledge search query") String query) {
        return knowledgeModuleFacade.execute(new KnowledgeQuery("demo-user", query, java.util.List.of()))
                .stream()
                .map(snippet -> snippet.title() + ": " + snippet.snippet())
                .reduce((left, right) -> left + "\n" + right)
                .orElse("No matching knowledge snippets found.");
    }

    @Tool(name = "list_connectors", description = "List the currently available consumer tool connectors.")
    public String listConnectors() {
        return toolModuleFacade.connectorStatuses().toString();
    }
}
