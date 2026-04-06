package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.RequestTraceEventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SpringDataRequestTraceEventEntityRepository extends JpaRepository<RequestTraceEventEntity, String> {

    List<RequestTraceEventEntity> findByOrderByCreatedAtDesc(Pageable pageable);

    List<RequestTraceEventEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    List<RequestTraceEventEntity> findBySessionIdOrderByCreatedAtDesc(String sessionId, Pageable pageable);

    List<RequestTraceEventEntity> findByContextIdOrderByCreatedAtDesc(String contextId, Pageable pageable);

    List<RequestTraceEventEntity> findByTraceIdOrderByCreatedAtDesc(String traceId, Pageable pageable);

    List<RequestTraceEventEntity> findByOperationOrderByCreatedAtDesc(String operation, Pageable pageable);

    List<RequestTraceEventEntity> findByCreatedAtBefore(Instant instant);

    long deleteByCreatedAtBefore(Instant instant);
}
