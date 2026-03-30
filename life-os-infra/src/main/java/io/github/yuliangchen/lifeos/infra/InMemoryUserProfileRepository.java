package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.model.UserProfile;
import io.github.yuliangchen.lifeos.domain.repository.UserProfileRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "memory")
public class InMemoryUserProfileRepository implements UserProfileRepository {

    private final ConcurrentHashMap<String, UserProfile> store = new ConcurrentHashMap<>();

    @Override
    public UserProfile save(UserProfile profile) {
        store.put(profile.userId(), profile);
        return profile;
    }

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        return Optional.ofNullable(store.get(userId));
    }
}
