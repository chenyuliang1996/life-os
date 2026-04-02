package io.github.yuliangchen.lifeos.web.security;

import io.github.yuliangchen.lifeos.domain.model.SecurityAuditEntry;
import io.github.yuliangchen.lifeos.domain.model.SecurityOverview;
import io.github.yuliangchen.lifeos.domain.model.SecurityTrustStatus;
import io.github.yuliangchen.lifeos.domain.model.SimulatedPersona;
import io.github.yuliangchen.lifeos.domain.model.ToolPolicyStatus;
import io.github.yuliangchen.lifeos.domain.repository.SecurityAuditRepository;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@EnableConfigurationProperties(LifeOsSecurityService.LifeOsSecurityProperties.class)
public class LifeOsSecurityService {

    private final SecurityAuditRepository securityAuditRepository;
    private final LifeOsSecurityProperties properties;

    public LifeOsSecurityService(SecurityAuditRepository securityAuditRepository,
                                 LifeOsSecurityProperties properties) {
        this.securityAuditRepository = securityAuditRepository;
        this.properties = properties;
    }

    public SecurityOverview overview(String userId, int limit) {
        SecurityTrustStatus trust = trustStatus(userId);
        return new SecurityOverview(
                userId,
                trust,
                toolPolicies(trust),
                securityAuditRepository.findRecentByUserId(userId, limit)
        );
    }

    public List<SecurityAuditEntry> recentAudit(String userId, int limit) {
        if (userId == null || userId.isBlank()) {
            return securityAuditRepository.findRecent(limit);
        }
        return securityAuditRepository.findRecentByUserId(userId, limit);
    }

    public void recordAudit(String userId,
                            String threadId,
                            String category,
                            String action,
                            String target,
                            String outcome,
                            String detail) {
        // Keep audit writes lightweight and structured so the service can explain what happened / 保持审计写入轻量且结构化，便于后续排查和解释系统行为。
        securityAuditRepository.save(new SecurityAuditEntry(
                UUID.randomUUID().toString(),
                normalizeUserId(userId),
                normalize(threadId),
                normalize(category),
                normalize(action),
                normalize(target),
                normalize(outcome),
                normalize(detail),
                Instant.now()
        ));
    }

    public List<SimulatedPersona> personas(String locale) {
        String resolvedLocale = LocaleSupport.resolve(locale, "zh-CN");
        boolean chinese = resolvedLocale.startsWith("zh");
        return List.of(
                new SimulatedPersona(
                        "urban-traveler",
                        resolvedLocale,
                        chinese ? "都市旅行者" : "Urban Traveler",
                        "persona-travel",
                        "thread-travel-primary",
                        chinese
                                ? "五一去东京 7 天，预算 1.5 万，尽量少换乘，晚上保留散步时间。"
                                : "Plan a 7-day Tokyo trip with a 15k RMB budget, low fatigue, and calm evenings.",
                        chinese
                                ? "高频旅行用户，关心节奏、预算和出行体验。"
                                : "A frequent traveler who cares about pacing, budget, and comfort.",
                        Map.of("travelStyle", "city-walk", "budgetLevel", "high", "studyGoal", "minimal")
                ),
                new SimulatedPersona(
                        "habit-builder",
                        resolvedLocale,
                        chinese ? "习惯坚持者" : "Habit Builder",
                        "persona-study",
                        "thread-study-primary",
                        chinese
                                ? "下周工作很忙，帮我保住英语听力和晚间锻炼，不要安排太满。"
                                : "Next week is packed. Protect my English listening habit and evening workouts without overloading the schedule.",
                        chinese
                                ? "学习和习惯优先，偏好稳定、可持续的安排。"
                                : "A user who prioritizes learning habits and sustainable routines.",
                        Map.of("travelStyle", "light", "budgetLevel", "medium", "studyGoal", "english")
                ),
                new SimulatedPersona(
                        "guest-explorer",
                        resolvedLocale,
                        chinese ? "谨慎体验者" : "Cautious Explorer",
                        "guest-weekend",
                        "thread-guest-primary",
                        chinese
                                ? "这周末想在附近轻松逛逛，先给我一版建议，不要自动访问外部服务或创建提醒。"
                                : "I want a low-key weekend outing nearby. Give me a safe first draft without live search or external writes.",
                        chinese
                                ? "首次或低信任用户，优先查看建议和护栏，再决定是否授权更多能力。"
                                : "A first-time or low-trust identity that prefers guardrail visibility before granting more capability.",
                        Map.of("travelStyle", "local-light", "budgetLevel", "low", "studyGoal", "steady")
                ),
                new SimulatedPersona(
                        "ops-reviewer",
                        resolvedLocale,
                        chinese ? "运营观察者" : "Operations Reviewer",
                        "persona-ops",
                        "thread-ops-primary",
                        chinese
                                ? "查看系统对高峰流量、审批积压和异常外呼的处理是否稳定。"
                                : "Inspect whether the service stays stable under traffic spikes, approval backlogs, and outbound-call anomalies.",
                        chinese
                                ? "偏向 ToB 观察视角，关注安全护栏和系统稳定性。"
                                : "A ToB-oriented reviewer focused on guardrails and runtime stability.",
                        Map.of("travelStyle", "observer", "budgetLevel", "ops", "studyGoal", "reliability")
                )
        );
    }

    public SecurityTrustStatus trustStatus(String userId) {
        String normalizedUserId = normalizeUserId(userId);
        String personaId = resolvePersonaId(normalizedUserId);
        boolean operator = matchesPrefix(normalizedUserId, properties.operatorUserPrefixes());
        boolean restricted = matchesPrefix(normalizedUserId, properties.restrictedUserPrefixes());
        boolean trusted = operator || matchesPrefix(normalizedUserId, properties.trustedUserPrefixes());
        boolean mcpTrusted = trusted || matchesPrefix(normalizedUserId, properties.mcpTrustedUserPrefixes());
        boolean outboundAllowed = !restricted && properties.outboundNetworkEnabled();
        boolean writeRequiresApproval = !operator || properties.forceApprovalForOperators();

        String trustTier = restricted ? "restricted" : operator ? "operator" : trusted ? "trusted" : "guarded";
        String summary = switch (trustTier) {
            case "restricted" -> "Outbound network and live specialist calls stay disabled until the identity is trusted.";
            case "operator" -> "Operator identity can inspect system controls, but write actions still remain approval-gated.";
            case "trusted" -> "Trusted identity can reach live search and A2A specialists under approval guardrails.";
            default -> "Guarded end-user identity can plan freely while network and write actions remain policy-bound.";
        };

        return new SecurityTrustStatus(
                normalizedUserId,
                personaId,
                trustTier,
                trusted,
                mcpTrusted,
                outboundAllowed,
                writeRequiresApproval,
                properties.outboundAllowlist(),
                summary
        );
    }

    private List<ToolPolicyStatus> toolPolicies(SecurityTrustStatus trust) {
        return List.of(
                new ToolPolicyStatus(
                        "knowledge.read",
                        "allowed",
                        "Seeded knowledge retrieval is always available for grounded answers."
                ),
                new ToolPolicyStatus(
                        "travel.search",
                        trust.outboundNetworkAllowed() ? "guarded-live" : "seeded-only",
                        trust.outboundNetworkAllowed()
                                ? "FlyAI or external search may run against the outbound allowlist."
                                : "Search is constrained to seeded knowledge until the identity is trusted."
                ),
                new ToolPolicyStatus(
                        "remote.specialist",
                        trust.mcpTrusted() ? "approval-required" : "disabled",
                        trust.mcpTrusted()
                                ? "A2A and MCP specialists stay behind trust checks and manual approvals."
                                : "Remote specialists remain disabled for untrusted identities."
                ),
                new ToolPolicyStatus(
                        "external.write",
                        trust.writeRequiresApproval() ? "approval-required" : "guarded-auto",
                        trust.writeRequiresApproval()
                                ? "Calendar writes, reminders, and other external mutations require user confirmation."
                                : "Operator automation may proceed only inside approved maintenance windows."
                ),
                new ToolPolicyStatus(
                        "system.exec",
                        matchesPrefix(trust.userId(), properties.operatorUserPrefixes()) && properties.execEnabled()
                                ? "approval-required"
                                : "denied",
                        "Shell-like execution stays disabled for consumer identities and is never automatic."
                )
        );
    }

    private String resolvePersonaId(String userId) {
        if (matchesPrefix(userId, properties.operatorUserPrefixes())) {
            return "ops-reviewer";
        }
        if (matchesPrefix(userId, properties.restrictedUserPrefixes())) {
            return "guest-explorer";
        }
        if (userId.contains("travel")) {
            return "urban-traveler";
        }
        if (userId.contains("study")) {
            return "habit-builder";
        }
        return "general-user";
    }

    private boolean matchesPrefix(String value, List<String> prefixes) {
        if (value == null || prefixes == null) {
            return false;
        }
        return prefixes.stream()
                .filter(prefix -> prefix != null && !prefix.isBlank())
                .anyMatch(value::startsWith);
    }

    private String normalizeUserId(String userId) {
        return userId == null || userId.isBlank() ? "lifeos-user" : userId;
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "n/a" : value;
    }

    @ConfigurationProperties(prefix = "lifeos.security")
    public record LifeOsSecurityProperties(
            List<String> trustedUserPrefixes,
            List<String> operatorUserPrefixes,
            List<String> restrictedUserPrefixes,
            List<String> mcpTrustedUserPrefixes,
            List<String> outboundAllowlist,
            boolean outboundNetworkEnabled,
            boolean execEnabled,
            boolean forceApprovalForOperators
    ) {

        public LifeOsSecurityProperties {
            if (trustedUserPrefixes == null || trustedUserPrefixes.isEmpty()) {
                trustedUserPrefixes = List.of("lifeos-", "persona-");
            }
            if (operatorUserPrefixes == null || operatorUserPrefixes.isEmpty()) {
                operatorUserPrefixes = List.of("persona-ops", "ops-", "admin-");
            }
            if (restrictedUserPrefixes == null || restrictedUserPrefixes.isEmpty()) {
                restrictedUserPrefixes = List.of("guest-", "external-");
            }
            if (mcpTrustedUserPrefixes == null || mcpTrustedUserPrefixes.isEmpty()) {
                mcpTrustedUserPrefixes = List.of("lifeos-", "persona-travel", "persona-ops");
            }
            if (outboundAllowlist == null || outboundAllowlist.isEmpty()) {
                outboundAllowlist = List.of("api.openai.com", "localhost", "127.0.0.1", "flyai.example");
            }
        }
    }
}
