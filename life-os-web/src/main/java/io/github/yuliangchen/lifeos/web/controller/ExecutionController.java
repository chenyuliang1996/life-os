package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.ExecutionRun;
import io.github.yuliangchen.lifeos.domain.model.TimelineEvent;
import io.github.yuliangchen.lifeos.domain.repository.ExecutionRunRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/executions")
public class ExecutionController {

    private final ExecutionRunRepository executionRunRepository;

    public ExecutionController(ExecutionRunRepository executionRunRepository) {
        this.executionRunRepository = executionRunRepository;
    }

    @GetMapping("/{runId}")
    public ExecutionRun getExecution(@PathVariable String runId) {
        return executionRunRepository.findById(runId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Execution run not found"));
    }

    @GetMapping("/{runId}/timeline")
    public List<TimelineEvent> timeline(@PathVariable String runId) {
        return getExecution(runId).timeline();
    }
}
