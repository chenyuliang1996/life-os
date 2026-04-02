package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.AgentRuntimeStatus;
import io.github.yuliangchen.lifeos.domain.model.AssistantContinuationRequest;
import io.github.yuliangchen.lifeos.domain.model.AssistantReply;
import io.github.yuliangchen.lifeos.domain.model.AssistantRequest;
import io.github.yuliangchen.lifeos.domain.model.ExecutionContinuationResult;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import io.github.yuliangchen.lifeos.web.runtime.LifeOsAgentRuntimeService;
import io.github.yuliangchen.lifeos.web.security.LifeOsSecurityService;
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
    private final LifeOsSecurityService lifeOsSecurityService;

    public AssistantController(LifeOsAgentRuntimeService lifeOsAgentRuntimeService,
                               LifeOsObservabilityService lifeOsObservabilityService,
                               LifeOsSecurityService lifeOsSecurityService) {
        this.lifeOsAgentRuntimeService = lifeOsAgentRuntimeService;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
        this.lifeOsSecurityService = lifeOsSecurityService;
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
            lifeOsSecurityService.recordAudit(
                    request.userId(),
                    request.threadId(),
                    "assistant",
                    "message",
                    reply.planId() == null ? "assistant" : reply.planId(),
                    "SUCCESS",
                    "Reply mode=" + reply.mode()
            );
            return reply;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordAssistantMessage(elapsedMillis(startedAt), false);
            lifeOsSecurityService.recordAudit(
                    request.userId(),
                    request.threadId(),
                    "assistant",
                    "message",
                    "assistant",
                    "FAILURE",
                    exception.getMessage()
            );
            throw exception;
        }
    }

    @PostMapping("/resume")
    public ExecutionContinuationResult resume(@RequestBody AssistantContinuationRequest request) {
        long startedAt = System.nanoTime();
        try {
            ExecutionContinuationResult result = lifeOsAgentRuntimeService.resume(request);
            lifeOsObservabilityService.recordAssistantResume(elapsedMillis(startedAt), true);
            lifeOsSecurityService.recordAudit(
                    request.userId(),
                    "n/a",
                    "assistant",
                    "resume",
                    request.planId(),
                    result.blocked() ? "BLOCKED" : result.resumed() ? "RESUMED" : "WAITING",
                    result.message()
            );
            return result;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordAssistantResume(elapsedMillis(startedAt), false);
            lifeOsSecurityService.recordAudit(
                    request.userId(),
                    "n/a",
                    "assistant",
                    "resume",
                    request.planId(),
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
