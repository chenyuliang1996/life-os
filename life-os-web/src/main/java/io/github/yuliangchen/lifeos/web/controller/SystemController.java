package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.ConnectorStatus;
import io.github.yuliangchen.lifeos.domain.model.OperationsSnapshot;
import io.github.yuliangchen.lifeos.domain.model.SystemArchitectureStatus;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    private final ToolModuleFacade toolModuleFacade;
    private final SystemArchitectureStatus systemArchitectureStatus;
    private final LifeOsObservabilityService lifeOsObservabilityService;

    public SystemController(ToolModuleFacade toolModuleFacade,
                            SystemArchitectureStatus systemArchitectureStatus,
                            LifeOsObservabilityService lifeOsObservabilityService) {
        this.toolModuleFacade = toolModuleFacade;
        this.systemArchitectureStatus = systemArchitectureStatus;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
    }

    @GetMapping("/connectors")
    public List<ConnectorStatus> connectors() {
        return toolModuleFacade.connectorStatuses();
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "life-os");
    }

    @GetMapping("/architecture")
    public SystemArchitectureStatus architecture() {
        return systemArchitectureStatus;
    }

    @GetMapping("/operations")
    public OperationsSnapshot operations() {
        return lifeOsObservabilityService.snapshot();
    }
}
