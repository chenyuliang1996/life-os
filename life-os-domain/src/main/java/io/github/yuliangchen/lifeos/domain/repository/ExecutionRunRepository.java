package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.ExecutionRun;

import java.util.Optional;

public interface ExecutionRunRepository {

    ExecutionRun save(ExecutionRun run);

    Optional<ExecutionRun> findById(String id);
}
