package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.MemoryUpdateCommand;
import io.github.yuliangchen.lifeos.domain.model.ProfileUpdateRequest;
import io.github.yuliangchen.lifeos.domain.model.UserProfile;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
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

    public ProfileController(MemoryModuleFacade memoryModuleFacade) {
        this.memoryModuleFacade = memoryModuleFacade;
    }

    @GetMapping
    public UserProfile getProfile(@RequestParam(defaultValue = "demo-user") String userId) {
        return memoryModuleFacade.getOrCreate(userId);
    }

    @PutMapping
    public UserProfile updateProfile(@RequestParam(defaultValue = "demo-user") String userId,
                                     @RequestBody ProfileUpdateRequest request) {
        return memoryModuleFacade.execute(new MemoryUpdateCommand(
                userId,
                request.preferences(),
                request.goals()
        ));
    }
}
