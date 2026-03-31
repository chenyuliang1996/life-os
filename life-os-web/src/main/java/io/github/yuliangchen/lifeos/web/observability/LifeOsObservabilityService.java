package io.github.yuliangchen.lifeos.web.observability;

import io.github.yuliangchen.lifeos.domain.model.ConfirmationStatus;
import io.github.yuliangchen.lifeos.domain.model.OperationsSnapshot;
import io.github.yuliangchen.lifeos.domain.model.ServiceLevelTarget;
import io.github.yuliangchen.lifeos.domain.model.UxTelemetryEvent;
import io.github.yuliangchen.lifeos.domain.repository.ConfirmationRequestRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
@EnableConfigurationProperties(LifeOsObservabilityService.ServiceLevelProperties.class)
public class LifeOsObservabilityService {

    private final MeterRegistry meterRegistry;
    private final ConfirmationRequestRepository confirmationRequestRepository;
    private final ServiceLevelProperties properties;
    private final AtomicLong totalRequests;
    private final AtomicLong totalFailures;
    private final Deque<Long> requestWindow;
    private final Object requestWindowMonitor;
    private final Timer planPreviewTimer;
    private final Timer assistantMessageTimer;
    private final Timer assistantResumeTimer;
    private final Timer confirmationDecisionTimer;
    private final Timer profileUpdateTimer;
    private final Timer knowledgeWriteTimer;
    private final Timer uxBootstrapTimer;
    private final Timer uxInteractionTimer;

    public LifeOsObservabilityService(MeterRegistry meterRegistry,
                                      ConfirmationRequestRepository confirmationRequestRepository,
                                      ServiceLevelProperties properties) {
        this.meterRegistry = meterRegistry;
        this.confirmationRequestRepository = confirmationRequestRepository;
        this.properties = properties;
        this.totalRequests = new AtomicLong();
        this.totalFailures = new AtomicLong();
        this.requestWindow = new ArrayDeque<>();
        this.requestWindowMonitor = new Object();
        this.planPreviewTimer = registerTimer("lifeos.plan.preview.latency", "Latency for action-plan creation requests.");
        this.assistantMessageTimer = registerTimer("lifeos.assistant.message.latency", "Latency for assistant replies.");
        this.assistantResumeTimer = registerTimer("lifeos.assistant.resume.latency", "Latency for continuation after approvals.");
        this.confirmationDecisionTimer = registerTimer("lifeos.confirmation.decision.latency", "Latency for approval decisions.");
        this.profileUpdateTimer = registerTimer("lifeos.profile.update.latency", "Latency for profile updates.");
        this.knowledgeWriteTimer = registerTimer("lifeos.knowledge.write.latency", "Latency for knowledge ingestion.");
        this.uxBootstrapTimer = registerTimer("lifeos.ux.bootstrap.latency", "Client-reported page bootstrap latency.");
        this.uxInteractionTimer = registerTimer("lifeos.ux.interaction.latency", "Client-reported interactive action latency.");
        Gauge.builder("lifeos.confirmation.pending", this, LifeOsObservabilityService::pendingConfirmations)
                .description("Current number of pending approval items.")
                .register(meterRegistry);
        Gauge.builder("lifeos.request.current_qps", this, LifeOsObservabilityService::currentQps)
                .description("Approximate request-per-second level across the last 10 seconds.")
                .register(meterRegistry);
    }

    public void recordPlanPreview(long durationMs, boolean success) {
        recordBackendRequest("plan.preview", durationMs, success, planPreviewTimer);
    }

    public void recordAssistantMessage(long durationMs, boolean success) {
        recordBackendRequest("assistant.message", durationMs, success, assistantMessageTimer);
    }

    public void recordAssistantResume(long durationMs, boolean success) {
        recordBackendRequest("assistant.resume", durationMs, success, assistantResumeTimer);
    }

    public void recordConfirmationDecision(long durationMs, boolean success) {
        recordBackendRequest("confirmation.decision", durationMs, success, confirmationDecisionTimer);
    }

    public void recordProfileUpdate(long durationMs, boolean success) {
        recordBackendRequest("profile.update", durationMs, success, profileUpdateTimer);
    }

    public void recordKnowledgeWrite(long durationMs, boolean success) {
        recordBackendRequest("knowledge.write", durationMs, success, knowledgeWriteTimer);
    }

    public void recordUxEvent(UxTelemetryEvent event) {
        Timer timer = "page_bootstrap".equals(event.action()) ? uxBootstrapTimer : uxInteractionTimer;
        timer.record(Math.max(event.durationMs(), 0L), TimeUnit.MILLISECONDS);
        meterRegistry.counter(
                "lifeos.ux.event.total",
                "action", normalize(event.action()),
                "surface", normalize(event.surface()),
                "locale", normalize(event.locale()),
                "success", Boolean.toString(event.success())
        ).increment();
    }

    public OperationsSnapshot snapshot() {
        ServiceLevelTarget target = new ServiceLevelTarget(
                properties.targetDau(),
                properties.targetPeakQps(),
                properties.availabilitySlo(),
                properties.assistantP95Ms(),
                properties.confirmationP95Ms(),
                properties.uxBootstrapP95Ms(),
                properties.uxInteractionP95Ms()
        );
        double currentQps = round(currentQps());
        double successRate = round(successRate());
        double assistantP95 = percentileMillis(assistantMessageTimer, 0.95);
        double previewP95 = percentileMillis(planPreviewTimer, 0.95);
        double resumeP95 = percentileMillis(assistantResumeTimer, 0.95);
        double confirmationP95 = percentileMillis(confirmationDecisionTimer, 0.95);
        double uxBootstrapP95 = percentileMillis(uxBootstrapTimer, 0.95);
        double uxInteractionP95 = percentileMillis(uxInteractionTimer, 0.95);
        boolean withinCapacity = currentQps <= properties.targetPeakQps();
        boolean withinSlo = successRate >= properties.availabilitySlo()
                && withinBudget(assistantP95, properties.assistantP95Ms())
                && withinBudget(confirmationP95, properties.confirmationP95Ms())
                && withinBudget(uxBootstrapP95, properties.uxBootstrapP95Ms())
                && withinBudget(uxInteractionP95, properties.uxInteractionP95Ms());
        return new OperationsSnapshot(
                target,
                currentQps,
                round(requestsPerMinute()),
                totalRequests.get(),
                successRate,
                pendingConfirmations(),
                round(assistantP95),
                round(previewP95),
                round(resumeP95),
                round(confirmationP95),
                round(uxBootstrapP95),
                round(uxInteractionP95),
                withinCapacity,
                withinSlo,
                buildSummary(withinCapacity, withinSlo, currentQps, pendingConfirmations())
        );
    }

    private void recordBackendRequest(String operation, long durationMs, boolean success, Timer timer) {
        timer.record(Math.max(durationMs, 0L), TimeUnit.MILLISECONDS);
        meterRegistry.counter(
                "lifeos.backend.request.total",
                "operation", operation,
                "success", Boolean.toString(success)
        ).increment();
        totalRequests.incrementAndGet();
        if (!success) {
            totalFailures.incrementAndGet();
        }
        long now = System.currentTimeMillis();
        synchronized (requestWindowMonitor) {
            requestWindow.addLast(now);
            prune(now);
        }
    }

    private Timer registerTimer(String name, String description) {
        return Timer.builder(name)
                .description(description)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    private long pendingConfirmations() {
        return confirmationRequestRepository.findByStatus(ConfirmationStatus.PENDING).size();
    }

    private double successRate() {
        long total = totalRequests.get();
        if (total == 0L) {
            return 100.0;
        }
        return ((double) (total - totalFailures.get()) / total) * 100.0;
    }

    private double requestsPerMinute() {
        long now = System.currentTimeMillis();
        synchronized (requestWindowMonitor) {
            prune(now);
            return requestWindow.stream().filter(timestamp -> timestamp >= now - 60_000L).count();
        }
    }

    private double currentQps() {
        long now = System.currentTimeMillis();
        synchronized (requestWindowMonitor) {
            prune(now);
            long recentCount = requestWindow.stream().filter(timestamp -> timestamp >= now - 10_000L).count();
            return recentCount / 10.0;
        }
    }

    private void prune(long now) {
        while (!requestWindow.isEmpty() && requestWindow.peekFirst() < now - 60_000L) {
            requestWindow.removeFirst();
        }
    }

    private double percentileMillis(Timer timer, double percentile) {
        ValueAtPercentile[] percentiles = timer.takeSnapshot().percentileValues();
        for (ValueAtPercentile candidate : percentiles) {
            if (Math.abs(candidate.percentile() - percentile) < 0.0001) {
                return candidate.value(TimeUnit.MILLISECONDS);
            }
        }
        return 0.0;
    }

    private boolean withinBudget(double actual, double budget) {
        return actual <= 0.0 || actual <= budget;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private String buildSummary(boolean withinCapacity, boolean withinSlo, double currentQps, long pendingConfirmations) {
        if (withinCapacity && withinSlo) {
            return "Service is operating within capacity and SLO guardrails.";
        }
        if (!withinCapacity) {
            return "Traffic is approaching the configured peak QPS budget. Review autoscaling policy.";
        }
        if (pendingConfirmations > 10) {
            return "Approval backlog is growing. Review confirmation throughput and escalation rules.";
        }
        return "User-facing latency or reliability is drifting outside the target envelope.";
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "unknown" : value;
    }

    @ConfigurationProperties(prefix = "lifeos.service")
    public record ServiceLevelProperties(
            long targetDau,
            double targetPeakQps,
            double availabilitySlo,
            double assistantP95Ms,
            double confirmationP95Ms,
            double uxBootstrapP95Ms,
            double uxInteractionP95Ms
    ) {

        public ServiceLevelProperties {
            if (targetDau <= 0) {
                targetDau = 200_000;
            }
            if (targetPeakQps <= 0) {
                targetPeakQps = 30.0;
            }
            if (availabilitySlo <= 0) {
                availabilitySlo = 99.9;
            }
            if (assistantP95Ms <= 0) {
                assistantP95Ms = 2_200.0;
            }
            if (confirmationP95Ms <= 0) {
                confirmationP95Ms = 900.0;
            }
            if (uxBootstrapP95Ms <= 0) {
                uxBootstrapP95Ms = 1_800.0;
            }
            if (uxInteractionP95Ms <= 0) {
                uxInteractionP95Ms = 1_200.0;
            }
        }
    }
}
