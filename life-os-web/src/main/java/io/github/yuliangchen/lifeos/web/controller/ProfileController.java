package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.MemoryUpdateCommand;
import io.github.yuliangchen.lifeos.domain.model.ProfileUpdateRequest;
import io.github.yuliangchen.lifeos.domain.model.UserProfile;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final MemoryModuleFacade memoryModuleFacade;
    private final LifeOsObservabilityService lifeOsObservabilityService;

    public ProfileController(MemoryModuleFacade memoryModuleFacade,
                             LifeOsObservabilityService lifeOsObservabilityService) {
        this.memoryModuleFacade = memoryModuleFacade;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
    }

    @GetMapping
    public UserProfile getProfile(@RequestParam(defaultValue = "lifeos-user") String userId) {
        return memoryModuleFacade.getOrCreate(userId);
    }

    @PutMapping
    public UserProfile updateProfile(@RequestParam(defaultValue = "lifeos-user") String userId,
                                     @RequestBody ProfileUpdateRequest request) {
        long startedAt = System.nanoTime();
        try {
            UserProfile profile = memoryModuleFacade.execute(new MemoryUpdateCommand(
                    userId,
                    request.preferences(),
                    request.goals()
            ));
            lifeOsObservabilityService.recordProfileUpdate(elapsedMillis(startedAt), true);
            return profile;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordProfileUpdate(elapsedMillis(startedAt), false);
            throw exception;
        }
    }

    private long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
