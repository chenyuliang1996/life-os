package io.github.yuliangchen.lifeos.web.agent;

import io.agentscope.core.agent.AgentBase;
import io.agentscope.core.interruption.InterruptContext;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.MsgRole;
import io.agentscope.spring.boot.agui.common.AguiAgentId;
import io.github.yuliangchen.lifeos.domain.model.AssistantReply;
import io.github.yuliangchen.lifeos.domain.model.AssistantRequest;
import io.github.yuliangchen.lifeos.web.runtime.LifeOsAgentRuntimeService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@AguiAgentId("default")
public class LifeOsAguiAgent extends AgentBase {

    private final LifeOsAgentRuntimeService lifeOsAgentRuntimeService;

    public LifeOsAguiAgent(LifeOsAgentRuntimeService lifeOsAgentRuntimeService) {
        super("life-os-default-agent", "Life OS default AG-UI agent");
        this.lifeOsAgentRuntimeService = lifeOsAgentRuntimeService;
    }

    @Override
    protected Mono<Msg> doCall(List<Msg> messages) {
        String userInput = messages.isEmpty() ? "Help me organize life planning" : messages.get(messages.size() - 1).getTextContent();
        String threadId = resolveThreadId(messages);
        AssistantReply reply = lifeOsAgentRuntimeService.reply(new AssistantRequest("lifeos-user", threadId, userInput, null));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("threadId", threadId);
        metadata.put("planId", reply.planId());
        metadata.put("runId", reply.runId());
        metadata.put("mode", reply.mode());
        metadata.put("highlights", reply.highlights());

        Msg message = Msg.builder()
                .id(UUID.randomUUID().toString())
                .name(getName())
                .role(MsgRole.ASSISTANT)
                .textContent(reply.message())
                .metadata(metadata)
                .build();

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
