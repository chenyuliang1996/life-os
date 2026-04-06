package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.MemoryUpdateCommand;
import io.github.yuliangchen.lifeos.domain.model.MemoryDetailsResponse;
import io.github.yuliangchen.lifeos.domain.model.RequestTraceEvent;
import io.github.yuliangchen.lifeos.domain.model.ProfileUpdateRequest;
import io.github.yuliangchen.lifeos.domain.model.UserProfile;
import io.github.yuliangchen.lifeos.domain.repository.RequestTraceRepository;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import io.github.yuliangchen.lifeos.web.observability.LifeOsObservabilityService;
import io.github.yuliangchen.lifeos.web.security.LifeOsAuthService;
import io.github.yuliangchen.lifeos.web.security.LifeOsSecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final MemoryModuleFacade memoryModuleFacade;
    private final RequestTraceRepository requestTraceRepository;
    private final LifeOsObservabilityService lifeOsObservabilityService;
    private final LifeOsSecurityService lifeOsSecurityService;
    private final LifeOsAuthService lifeOsAuthService;

    public ProfileController(MemoryModuleFacade memoryModuleFacade,
                             RequestTraceRepository requestTraceRepository,
                             LifeOsObservabilityService lifeOsObservabilityService,
                             LifeOsSecurityService lifeOsSecurityService,
                             LifeOsAuthService lifeOsAuthService) {
        this.memoryModuleFacade = memoryModuleFacade;
        this.requestTraceRepository = requestTraceRepository;
        this.lifeOsObservabilityService = lifeOsObservabilityService;
        this.lifeOsSecurityService = lifeOsSecurityService;
        this.lifeOsAuthService = lifeOsAuthService;
    }

    @GetMapping
    public UserProfile getProfile(@RequestParam(defaultValue = "lifeos-user") String userId,
                                  @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        return memoryModuleFacade.getOrCreate(resolveUserId(userId, authorizationHeader));
    }

    @GetMapping("/memory-details")
    public MemoryDetailsResponse memoryDetails(@RequestParam(defaultValue = "lifeos-user") String userId,
                                               @RequestParam(defaultValue = "10") int limit,
                                               @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String resolvedUserId = resolveUserId(userId, authorizationHeader);
        UserProfile profile = memoryModuleFacade.getOrCreate(resolvedUserId);
        int normalizedLimit = Math.max(1, Math.min(limit, 60));
        List<RequestTraceEvent> recentOperations = requestTraceRepository.findRecentByUserId(resolvedUserId, normalizedLimit);
        return new MemoryDetailsResponse(
                resolvedUserId,
                profile.preferences(),
                profile.goals(),
                buildMemorySummary(profile, recentOperations),
                recentOperations
        );
    }

    @PutMapping
    public UserProfile updateProfile(@RequestParam(defaultValue = "lifeos-user") String userId,
                                     @RequestParam(required = false) String sessionId,
                                     @RequestParam(required = false) String contextId,
                                     @RequestParam(required = false) String traceId,
                                     @RequestParam(defaultValue = "toc") String surface,
                                     @RequestParam(defaultValue = "zh-CN") String locale,
                                     @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                     @RequestBody ProfileUpdateRequest request) {
        long startedAt = System.nanoTime();
        String resolvedUserId = resolveUserId(userId, authorizationHeader);
        try {
            UserProfile profile = memoryModuleFacade.execute(new MemoryUpdateCommand(
                    resolvedUserId,
                    request.preferences(),
                    request.goals()
            ));
            lifeOsObservabilityService.recordProfileUpdate(
                    elapsedMillis(startedAt),
                    true,
                    resolvedUserId,
                    "profile-thread",
                    normalize(sessionId),
                    normalize(contextId),
                    normalize(traceId),
                    normalize(surface),
                    normalize(locale)
            );
            lifeOsSecurityService.recordAudit(
                    resolvedUserId,
                    "n/a",
                    "profile",
                    "update",
                    resolvedUserId,
                    "SUCCESS",
                    "Updated explicit long-term preferences."
            );
            return profile;
        } catch (RuntimeException exception) {
            lifeOsObservabilityService.recordProfileUpdate(
                    elapsedMillis(startedAt),
                    false,
                    resolvedUserId,
                    "profile-thread",
                    normalize(sessionId),
                    normalize(contextId),
                    normalize(traceId),
                    normalize(surface),
                    normalize(locale)
            );
            lifeOsSecurityService.recordAudit(
                    resolvedUserId,
                    "n/a",
                    "profile",
                    "update",
                    resolvedUserId,
                    "FAILURE",
                    exception.getMessage()
            );
            throw exception;
        }
    }

    private long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }

    private String resolveUserId(String userId, String authorizationHeader) {
        String authUserId = lifeOsAuthService.resolveUserId(authorizationHeader);
        if (authUserId != null && !authUserId.isBlank()) {
            return authUserId;
        }
        return (userId == null || userId.isBlank()) ? "lifeos-user" : userId;
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "unknown" : value;
    }

    private String buildMemorySummary(UserProfile profile, List<RequestTraceEvent> recentOperations) {
        int preferenceCount = profile.preferences() == null ? 0 : profile.preferences().size();
        int goalCount = profile.goals() == null ? 0 : profile.goals().size();
        if (recentOperations == null || recentOperations.isEmpty()) {
            return "Preferences: " + preferenceCount + ", goals: " + goalCount + ", operations: 0";
        }
        RequestTraceEvent latest = recentOperations.get(0);
        String latestAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault())
                .format(latest.timestamp());
        return "Preferences: " + preferenceCount
                + ", goals: " + goalCount
                + ", operations: " + recentOperations.size()
                + ", latest: " + latest.operation() + " @ " + latestAt;
    }
}
