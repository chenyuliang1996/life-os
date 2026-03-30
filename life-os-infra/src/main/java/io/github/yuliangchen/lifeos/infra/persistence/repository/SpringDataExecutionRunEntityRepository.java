package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.ExecutionRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataExecutionRunEntityRepository extends JpaRepository<ExecutionRunEntity, String> {
}
