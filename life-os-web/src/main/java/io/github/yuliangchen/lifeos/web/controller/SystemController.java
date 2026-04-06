package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.ConnectorStatus;
import io.github.yuliangchen.lifeos.domain.model.OperationsSnapshot;
import io.github.yuliangchen.lifeos.domain.model.RequestTraceEvent;
import io.github.yuliangchen.lifeos.domain.model.SystemArchitectureStatus;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import io.github.yuliangchen.lifeos.web.security.LifeOsAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final LifeOsAuthService lifeOsAuthService;

    public SystemController(ToolModuleFacade toolModuleFacade,
                            SystemArchitectureStatus systemArchitectureStatus,
                            LifeOsObservabilityService lifeOsObservabilityService,
                            LifeOsAuthService lifeOsAuthService) {
        this.toolModuleFacade = toolModuleFacade;
        this.systemArchitectureStatus = systemArchitectureStatus;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
        this.lifeOsAuthService = lifeOsAuthService;
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

    @GetMapping("/trace-links")
    public List<RequestTraceEvent> traceLinks(@RequestParam(required = false) String userId,
                                              @RequestParam(required = false) String sessionId,
                                              @RequestParam(required = false) String contextId,
                                              @RequestParam(required = false) String traceId,
                                              @RequestParam(required = false) String operation,
                                              @RequestParam(defaultValue = "20") int limit,
                                              @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String resolvedUserId = userId;
        if (resolvedUserId == null || resolvedUserId.isBlank()) {
            resolvedUserId = lifeOsAuthService.resolveUserId(authorizationHeader);
        }
        return lifeOsObservabilityService.queryTraceEvents(
                resolvedUserId,
                sessionId,
                contextId,
                traceId,
                operation,
                Math.max(1, Math.min(limit, 120))
        );
    }

    @GetMapping("/trace-links/query")
    public List<RequestTraceEvent> queryTraceLinks(@RequestParam(required = false) String userId,
                                                   @RequestParam(required = false) String sessionId,
                                                   @RequestParam(required = false) String contextId,
                                                   @RequestParam(required = false) String traceId,
                                                   @RequestParam(required = false) String operation,
                                                   @RequestParam(defaultValue = "20") int limit,
                                                   @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        return traceLinks(userId, sessionId, contextId, traceId, operation, limit, authorizationHeader);
    }
}
