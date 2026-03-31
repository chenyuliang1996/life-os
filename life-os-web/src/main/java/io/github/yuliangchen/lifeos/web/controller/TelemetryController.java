package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.UxTelemetryEvent;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/telemetry")
public class TelemetryController {

    private final LifeOsObservabilityService lifeOsObservabilityService;

    public TelemetryController(LifeOsObservabilityService lifeOsObservabilityService) {
        this.lifeOsObservabilityService = lifeOsObservabilityService;
    }

    @PostMapping("/ux")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void recordUx(@RequestBody UxTelemetryEvent request) {
        lifeOsObservabilityService.recordUxEvent(request);
    }
}
