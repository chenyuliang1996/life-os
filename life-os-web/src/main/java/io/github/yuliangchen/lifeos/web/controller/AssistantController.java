package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.AgentRuntimeStatus;
import io.github.yuliangchen.lifeos.domain.model.AssistantReply;
import io.github.yuliangchen.lifeos.domain.model.AssistantRequest;
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

    public AssistantController(LifeOsAgentRuntimeService lifeOsAgentRuntimeService) {
        this.lifeOsAgentRuntimeService = lifeOsAgentRuntimeService;
    }

    @GetMapping("/runtime")
    public AgentRuntimeStatus runtime() {
        return lifeOsAgentRuntimeService.status();
    }

    @PostMapping("/message")
    public AssistantReply message(@RequestBody AssistantRequest request) {
        return lifeOsAgentRuntimeService.reply(request);
    }
}
