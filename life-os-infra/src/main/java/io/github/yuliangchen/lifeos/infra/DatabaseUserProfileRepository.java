package io.github.yuliangchen.lifeos.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.yuliangchen.lifeos.domain.model.Goal;
import io.github.yuliangchen.lifeos.domain.model.UserProfile;
import io.github.yuliangchen.lifeos.domain.repository.UserProfileRepository;
import io.github.yuliangchen.lifeos.infra.persistence.JsonValueCodec;
import io.github.yuliangchen.lifeos.infra.persistence.entity.UserProfileEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataUserProfileEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseUserProfileRepository implements UserProfileRepository {

    private final SpringDataUserProfileEntityRepository repository;
    private final JsonValueCodec jsonValueCodec;

    public DatabaseUserProfileRepository(SpringDataUserProfileEntityRepository repository, JsonValueCodec jsonValueCodec) {
        this.repository = repository;
        this.jsonValueCodec = jsonValueCodec;
    }

    @Override
    public UserProfile save(UserProfile profile) {
        return toDomain(repository.save(toEntity(profile)));
    }

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        return repository.findById(userId).map(this::toDomain);
    }

    private UserProfileEntity toEntity(UserProfile profile) {
        UserProfileEntity entity = new UserProfileEntity();
        entity.setUserId(profile.userId());
        entity.setPreferencesJson(jsonValueCodec.write(profile.preferences()));
        entity.setGoalsJson(jsonValueCodec.write(profile.goals()));
        entity.setUpdatedAt(profile.updatedAt());
        return entity;
    }

    private UserProfile toDomain(UserProfileEntity entity) {
        return new UserProfile(
                entity.getUserId(),
                jsonValueCodec.read(entity.getPreferencesJson(), new TypeReference<Map<String, String>>() {
                }, Map.of()),
                jsonValueCodec.read(entity.getGoalsJson(), new TypeReference<List<Goal>>() {
                }, List.of()),
                entity.getUpdatedAt()
        );
    }
}
