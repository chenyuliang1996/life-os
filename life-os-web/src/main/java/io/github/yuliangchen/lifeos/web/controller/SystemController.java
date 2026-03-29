package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.ConnectorStatus;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    private final ToolModuleFacade toolModuleFacade;

    public SystemController(ToolModuleFacade toolModuleFacade) {
        this.toolModuleFacade = toolModuleFacade;
    }

    @GetMapping("/connectors")
    public List<ConnectorStatus> connectors() {
        return toolModuleFacade.connectorStatuses();
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "life-os");
    }
}
