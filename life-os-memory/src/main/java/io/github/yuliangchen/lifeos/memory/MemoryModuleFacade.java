package io.github.yuliangchen.lifeos.memory;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.Goal;
import io.github.yuliangchen.lifeos.domain.model.MemoryUpdateCommand;
import io.github.yuliangchen.lifeos.domain.model.UserProfile;
import io.github.yuliangchen.lifeos.domain.repository.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class MemoryModuleFacade implements ModuleExecutable<MemoryUpdateCommand, UserProfile> {

    private final UserProfileRepository userProfileRepository;

    public MemoryModuleFacade(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public String moduleName() {
        return "life-os-memory";
    }

    @Override
    public UserProfile execute(MemoryUpdateCommand input) {
        UserProfile previous = userProfileRepository.findByUserId(input.userId())
                .orElse(defaultProfile(input.userId()));

        UserProfile updated = new UserProfile(
                input.userId(),
                mergePreferences(previous.preferences(), input.preferences()),
                input.goals() == null || input.goals().isEmpty() ? previous.goals() : input.goals(),
                Instant.now()
        );

        return userProfileRepository.save(updated);
    }

    public UserProfile getOrCreate(String userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseGet(() -> userProfileRepository.save(defaultProfile(userId)));
    }

    @Override
    public String executeDemo() {
        UserProfile profile = execute(new MemoryUpdateCommand(
                "demo-user",
                Map.of("travelStyle", "relaxed", "budgetLevel", "medium"),
                List.<Goal>of()
        ));
        return "Memory ready for " + profile.userId() + " with preferences " + profile.preferences();
    }

    private UserProfile defaultProfile(String userId) {
        return new UserProfile(
                userId,
                Map.of("travelStyle", "balanced", "budgetLevel", "medium", "studyGoal", "english"),
                List.of(),
                Instant.now()
        );
    }

    private Map<String, String> mergePreferences(Map<String, String> previous, Map<String, String> incoming) {
        if (incoming == null || incoming.isEmpty()) {
            return previous;
        }
        java.util.HashMap<String, String> merged = new java.util.HashMap<>(previous);
        merged.putAll(incoming);
        return Map.copyOf(merged);
    }
}
