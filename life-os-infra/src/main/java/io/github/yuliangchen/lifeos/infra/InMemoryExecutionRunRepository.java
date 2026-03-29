package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.ExecutionRun;
import io.github.yuliangchen.lifeos.domain.repository.ExecutionRunRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryExecutionRunRepository implements ExecutionRunRepository {

    private final ConcurrentHashMap<String, ExecutionRun> store = new ConcurrentHashMap<>();

    @Override
    public ExecutionRun save(ExecutionRun run) {
        store.put(run.id(), run);
        return run;
    }

    @Override
    public Optional<ExecutionRun> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
