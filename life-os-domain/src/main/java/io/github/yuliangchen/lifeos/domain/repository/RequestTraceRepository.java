package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.RequestTraceEvent;

import java.util.List;

public interface RequestTraceRepository {

    RequestTraceEvent save(RequestTraceEvent event);

    List<RequestTraceEvent> findRecent(int limit);

    List<RequestTraceEvent> findRecentByUserId(String userId, int limit);

    List<RequestTraceEvent> findRecentBySessionId(String sessionId, int limit);

    List<RequestTraceEvent> findRecentByContextId(String contextId, int limit);

    List<RequestTraceEvent> findRecentByTraceId(String traceId, int limit);

    List<RequestTraceEvent> findRecentByOperation(String operation, int limit);
}
