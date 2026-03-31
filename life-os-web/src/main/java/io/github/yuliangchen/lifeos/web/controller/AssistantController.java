package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.AgentRuntimeStatus;
import io.github.yuliangchen.lifeos.domain.model.AssistantContinuationRequest;
import io.github.yuliangchen.lifeos.domain.model.AssistantReply;
import io.github.yuliangchen.lifeos.domain.model.AssistantRequest;
import io.github.yuliangchen.lifeos.domain.model.ExecutionContinuationResult;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import io.github.yuliangchen.lifeos.web.runtime.LifeOsAgentRuntimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {

    private final LifeOsAgentRuntimeService lifeOsAgentRuntimeService;
    private final LifeOsObservabilityService lifeOsObservabilityService;

    public AssistantController(LifeOsAgentRuntimeService lifeOsAgentRuntimeService,
                               LifeOsObservabilityService lifeOsObservabilityService) {
        this.lifeOsAgentRuntimeService = lifeOsAgentRuntimeService;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
    }

    @GetMapping("/runtime")
    public AgentRuntimeStatus runtime() {
        return lifeOsAgentRuntimeService.status();
    }

    @PostMapping("/message")
    public AssistantReply message(@RequestBody AssistantRequest request) {
        long startedAt = System.nanoTime();
        try {
            AssistantReply reply = lifeOsAgentRuntimeService.reply(request);
            lifeOsObservabilityService.recordAssistantMessage(elapsedMillis(startedAt), true);
            return reply;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordAssistantMessage(elapsedMillis(startedAt), false);
            throw exception;
        }
    }

    @PostMapping("/resume")
    public ExecutionContinuationResult resume(@RequestBody AssistantContinuationRequest request) {
        long startedAt = System.nanoTime();
        try {
            ExecutionContinuationResult result = lifeOsAgentRuntimeService.resume(request);
            lifeOsObservabilityService.recordAssistantResume(elapsedMillis(startedAt), true);
            return result;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordAssistantResume(elapsedMillis(startedAt), false);
            throw exception;
        }
    }

    private long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
