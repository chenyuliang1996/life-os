package io.github.yuliangchen.lifeos.domain.repository;

import io.github.yuliangchen.lifeos.domain.model.UserProfile;

import java.util.Optional;

public interface UserProfileRepository {

    UserProfile save(UserProfile profile);

    Optional<UserProfile> findByUserId(String userId);
}
