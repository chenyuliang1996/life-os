package io.github.yuliangchen.lifeos.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.yuliangchen.lifeos.domain.model.ExecutionRun;
import io.github.yuliangchen.lifeos.domain.model.PlanStatus;
import io.github.yuliangchen.lifeos.domain.model.TimelineEvent;
import io.github.yuliangchen.lifeos.domain.repository.ExecutionRunRepository;
import io.github.yuliangchen.lifeos.infra.persistence.JsonValueCodec;
import io.github.yuliangchen.lifeos.infra.persistence.entity.ExecutionRunEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataExecutionRunEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseExecutionRunRepository implements ExecutionRunRepository {

    private final SpringDataExecutionRunEntityRepository repository;
    private final JsonValueCodec jsonValueCodec;

    public DatabaseExecutionRunRepository(SpringDataExecutionRunEntityRepository repository, JsonValueCodec jsonValueCodec) {
        this.repository = repository;
        this.jsonValueCodec = jsonValueCodec;
    }

    @Override
    public ExecutionRun save(ExecutionRun run) {
        ExecutionRunEntity entity = new ExecutionRunEntity();
        entity.setId(run.id());
        entity.setUserId(run.userId());
        entity.setInputText(run.input());
        entity.setStatus(run.status().name());
        entity.setCreatedAt(run.createdAt());
        entity.setTimelineJson(jsonValueCodec.write(run.timeline()));
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<ExecutionRun> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    private ExecutionRun toDomain(ExecutionRunEntity entity) {
        return new ExecutionRun(
                entity.getId(),
                entity.getUserId(),
                entity.getInputText(),
                PlanStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                jsonValueCodec.read(entity.getTimelineJson(), new TypeReference<List<TimelineEvent>>() {
                }, List.of())
        );
    }
}
