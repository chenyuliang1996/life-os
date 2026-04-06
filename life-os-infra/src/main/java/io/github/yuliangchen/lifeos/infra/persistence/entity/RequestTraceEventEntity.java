package io.github.yuliangchen.lifeos.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "request_trace_events", indexes = {
        @Index(name = "idx_trace_user_created_at", columnList = "user_id, created_at"),
        @Index(name = "idx_trace_session_created_at", columnList = "session_id, created_at"),
        @Index(name = "idx_trace_context_created_at", columnList = "context_id, created_at"),
        @Index(name = "idx_trace_traceid_created_at", columnList = "trace_id, created_at"),
        @Index(name = "idx_trace_operation_created_at", columnList = "operation, created_at")
})
public class RequestTraceEventEntity {

    @Id
    @Column(nullable = false, length = 128)
    private String id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(nullable = false, length = 128)
    private String operation;

    @Column(name = "user_id", nullable = false, length = 128)
    private String userId;

    @Column(name = "thread_id", nullable = false, length = 128)
    private String threadId;

    @Column(name = "session_id", nullable = false, length = 128)
    private String sessionId;

    @Column(name = "context_id", nullable = false, length = 128)
    private String contextId;

    @Column(name = "trace_id", nullable = false, length = 128)
    private String traceId;

    @Column(nullable = false, length = 24)
    private String surface;

    @Column(nullable = false, length = 24)
    private String locale;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "duration_ms", nullable = false)
    private long durationMs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }
}
