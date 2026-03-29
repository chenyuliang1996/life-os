package io.github.yuliangchen.lifeos.web.agent;

import io.agentscope.core.agent.AgentBase;
import io.agentscope.core.interruption.InterruptContext;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.MsgRole;
import io.agentscope.core.session.JsonSession;
import io.agentscope.core.state.SimpleSessionKey;
import io.agentscope.spring.boot.agui.common.AguiAgentId;
import io.github.yuliangchen.lifeos.domain.model.OrchestrationResult;
import io.github.yuliangchen.lifeos.domain.model.PlanPreviewRequest;
import io.github.yuliangchen.lifeos.orchestrator.LifeOrchestrator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AguiAgentId("default")
public class LifeOsAguiAgent extends AgentBase {

    private final LifeOrchestrator lifeOrchestrator;
    private final JsonSession jsonSession;

    public LifeOsAguiAgent(LifeOrchestrator lifeOrchestrator, JsonSession jsonSession) {
        super("life-os-default-agent", "Life OS default AG-UI agent");
        this.lifeOrchestrator = lifeOrchestrator;
        this.jsonSession = jsonSession;
    }

    @Override
    protected Mono<Msg> doCall(List<Msg> messages) {
        String userInput = messages.isEmpty() ? "Help me organize life planning" : messages.get(messages.size() - 1).getTextContent();
        String threadId = resolveThreadId(messages);
        OrchestrationResult result = lifeOrchestrator.execute(new PlanPreviewRequest(
                "demo-user",
                threadId,
                userInput
        ));

        String responseText = """
                I turned your request into a coordinated Life OS plan.

                Summary: %s

                Tasks:
                %s

                Pending confirmations: %d
                """.formatted(
                result.plan().summary(),
                result.plan().tasks().stream()
                        .map(task -> "- " + task.title() + " (" + task.owner() + ")")
                        .collect(Collectors.joining("\n")),
                result.confirmations().size()
        );

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("threadId", threadId);
        metadata.put("planId", result.plan().id());
        metadata.put("runId", result.executionRun().id());
        metadata.put("confirmations", result.confirmations().stream().map(c -> c.id()).toList());

        Msg message = Msg.builder()
                .id(UUID.randomUUID().toString())
                .name(getName())
                .role(MsgRole.ASSISTANT)
                .textContent(responseText)
                .metadata(metadata)
                .build();

        jsonSession.save(SimpleSessionKey.of(threadId), "latest-assistant-message", message);
        return Mono.just(message);
    }

    @Override
    protected Mono<Msg> handleInterrupt(InterruptContext interruptContext, Msg... msgs) {
        return Mono.just(Msg.builder()
                .id(UUID.randomUUID().toString())
                .name(getName())
                .role(MsgRole.ASSISTANT)
                .textContent("The run was interrupted. You can continue after reviewing pending actions.")
                .build());
    }

    private String resolveThreadId(List<Msg> messages) {
        if (!messages.isEmpty() && messages.get(messages.size() - 1).getMetadata() != null) {
            Object threadId = messages.get(messages.size() - 1).getMetadata().get("threadId");
            if (threadId != null) {
                return threadId.toString();
            }
        }
        return "thread-" + UUID.randomUUID();
    }
}
