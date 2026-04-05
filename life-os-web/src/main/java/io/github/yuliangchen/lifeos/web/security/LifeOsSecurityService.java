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
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "INTJ",
                        "战略规划者",
                        "Strategic Architect",
                        chinese
                                ? "帮我规划东京 6 天高效率路线，优先低换乘和可量化预算。"
                                : "Plan a high-efficiency 6-day Tokyo route with low transfers and measurable budget checkpoints.",
                        chinese
                                ? "偏好结构化决策，关注风险边界与执行闭环。"
                                : "Prefers structured decisions with explicit risk boundaries and execution loops.",
                        "analyst",
                        "systems-thinking",
                        "strategy-grid",
                        "high",
                        "english",
                        "deep",
                        "direct",
                        "solo-focus",
                        "sprint"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "INTP",
                        "探索分析者",
                        "Concept Explorer",
                        chinese
                                ? "比较两种东京行程打法，给我关键假设和验证方式。"
                                : "Compare two Tokyo planning approaches and show key assumptions with validation checks.",
                        chinese
                                ? "注重模型推演和方案可解释性。"
                                : "Values hypothesis-driven reasoning and explainable plans.",
                        "analyst",
                        "evidence-first",
                        "concept-lab",
                        "medium",
                        "english",
                        "deep",
                        "analytical",
                        "solo-focus",
                        "focus-loop"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ENTJ",
                        "效率指挥者",
                        "Execution Commander",
                        chinese
                                ? "给我一个能带团队执行的东京出行方案，重点是效率和明确分工。"
                                : "Create an execution-ready Tokyo plan for a small team with clear ownership and high efficiency.",
                        chinese
                                ? "追求高效推进，偏好里程碑与责任清晰。"
                                : "Pushes for rapid progress with explicit milestones and ownership.",
                        "analyst",
                        "outcome-driven",
                        "command-route",
                        "high",
                        "english",
                        "deep",
                        "direct",
                        "high-social",
                        "sprint"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ENTP",
                        "创意破局者",
                        "Creative Challenger",
                        chinese
                                ? "给我一版有新鲜感的东京玩法，附上可替换备选和风险提示。"
                                : "Design a novel Tokyo itinerary with swappable alternatives and explicit risk notes.",
                        chinese
                                ? "喜欢快速试错，偏好灵活迭代。"
                                : "Enjoys rapid experimentation and flexible iteration.",
                        "analyst",
                        "possibility-scouting",
                        "experimental",
                        "medium",
                        "english",
                        "adaptive",
                        "energetic",
                        "high-social",
                        "burst"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "INFJ",
                        "愿景守护者",
                        "Vision Steward",
                        chinese
                                ? "帮我安排东京行程，同时保留安静恢复时间和学习连续性。"
                                : "Plan Tokyo days that preserve quiet recovery windows and uninterrupted study continuity.",
                        chinese
                                ? "关注长期一致性和个人价值感。"
                                : "Optimizes for long-term alignment and personal meaning.",
                        "diplomat",
                        "meaning-alignment",
                        "calm-ritual",
                        "medium",
                        "english",
                        "deep",
                        "warm",
                        "balanced",
                        "steady"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "INFP",
                        "理想体验者",
                        "Ideal Journeyer",
                        chinese
                                ? "希望东京路线更有在地感，预算可控但不要牺牲体验温度。"
                                : "Keep the Tokyo route authentic and emotionally rich while staying inside a clear budget.",
                        chinese
                                ? "重视体验温度和自我节奏。"
                                : "Cares about emotional quality and personal pacing.",
                        "diplomat",
                        "harmony-first",
                        "local-story",
                        "medium",
                        "english",
                        "adaptive",
                        "supportive",
                        "solo-focus",
                        "steady"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ENFJ",
                        "协同引导者",
                        "Collaborative Mentor",
                        chinese
                                ? "帮我做一版适合结伴出行的东京行程，兼顾群体体验和学习目标。"
                                : "Build a Tokyo plan for companion travel that balances group delight with learning goals.",
                        chinese
                                ? "擅长协调多人偏好，追求整体体验一致。"
                                : "Coordinates group preferences while keeping a coherent shared experience.",
                        "diplomat",
                        "people-alignment",
                        "group-harmony",
                        "medium",
                        "english",
                        "balanced",
                        "warm",
                        "high-social",
                        "steady"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ENFP",
                        "活力探索者",
                        "Momentum Explorer",
                        chinese
                                ? "给我一版有活力又不太累的东京安排，重点是体验密度和可玩性。"
                                : "Design an energetic but low-fatigue Tokyo flow focused on high enjoyment density.",
                        chinese
                                ? "偏好有变化的节奏与即时反馈。"
                                : "Prefers variety, momentum, and immediate feedback.",
                        "diplomat",
                        "people-alignment",
                        "vivid-flow",
                        "medium",
                        "english",
                        "adaptive",
                        "energetic",
                        "high-social",
                        "burst"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ISTJ",
                        "秩序执行者",
                        "Reliable Operator",
                        chinese
                                ? "按规则给我一版东京行程，明确每天预算、交通和回撤方案。"
                                : "Create a rule-based Tokyo itinerary with daily budget, transit, and fallback details.",
                        chinese
                                ? "偏好流程清晰、风险可控。"
                                : "Prefers process clarity and controlled risk.",
                        "sentinel",
                        "process-discipline",
                        "checklist",
                        "medium",
                        "english",
                        "deep",
                        "direct",
                        "solo-focus",
                        "steady"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ISFJ",
                        "稳健照料者",
                        "Careful Caretaker",
                        chinese
                                ? "帮我排一版照顾体感的东京路线，避免赶路并保留稳定作息。"
                                : "Plan a comfort-first Tokyo route with gentle pacing and protected daily routine.",
                        chinese
                                ? "重视稳定、安全和长期可持续。"
                                : "Values safety, routine stability, and long-term sustainability.",
                        "sentinel",
                        "harmony-first",
                        "comfort-care",
                        "low",
                        "english",
                        "balanced",
                        "supportive",
                        "balanced",
                        "steady"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ESTJ",
                        "组织推进者",
                        "Operational Driver",
                        chinese
                                ? "给我可落地的东京执行计划，明确时间块、审批点和责任人。"
                                : "Generate an execution-grade Tokyo plan with explicit time blocks, approval gates, and owners.",
                        chinese
                                ? "重视可执行性和责任闭环。"
                                : "Focuses on accountability and operational closure.",
                        "sentinel",
                        "outcome-driven",
                        "execution-board",
                        "high",
                        "english",
                        "deep",
                        "direct",
                        "high-social",
                        "sprint"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ESFJ",
                        "服务协作者",
                        "Service Coordinator",
                        chinese
                                ? "做一版适合家人同行的东京行程，兼顾体验、饮食和休息。"
                                : "Create a family-friendly Tokyo plan balancing attractions, meals, and rest windows.",
                        chinese
                                ? "关注群体满意度和协同体验。"
                                : "Optimizes for group satisfaction and smooth coordination.",
                        "sentinel",
                        "people-alignment",
                        "family-care",
                        "medium",
                        "english",
                        "balanced",
                        "warm",
                        "high-social",
                        "steady"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ISTP",
                        "实战解决者",
                        "Practical Solver",
                        chinese
                                ? "给我简洁可执行的东京方案，重点放在关键节点和应急替代。"
                                : "Give me a concise, executable Tokyo plan focused on critical checkpoints and fast contingencies.",
                        chinese
                                ? "偏好现场决策和高适应性。"
                                : "Leans toward situational decisions and high adaptability.",
                        "explorer",
                        "direct-action",
                        "lean-route",
                        "medium",
                        "english",
                        "adaptive",
                        "direct",
                        "solo-focus",
                        "burst"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ISFP",
                        "感受体验者",
                        "Aesthetic Explorer",
                        chinese
                                ? "想要轻松有质感的东京日程，不用太满，留一些自由时间。"
                                : "Build a light, aesthetic Tokyo schedule with room for spontaneous free time.",
                        chinese
                                ? "重视体感、审美和即时体验。"
                                : "Values sensory quality, aesthetics, and present-moment experience.",
                        "explorer",
                        "experience-first",
                        "aesthetic-flow",
                        "medium",
                        "english",
                        "adaptive",
                        "supportive",
                        "balanced",
                        "steady"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ESTP",
                        "现场掌控者",
                        "Field Tactician",
                        chinese
                                ? "安排一版快节奏东京行程，重点是体验密度和即时调整能力。"
                                : "Arrange a fast-paced Tokyo route optimized for experience density and live adjustments.",
                        chinese
                                ? "行动导向，偏好快速决策。"
                                : "Action-oriented and comfortable with rapid decisions.",
                        "explorer",
                        "direct-action",
                        "high-tempo",
                        "high",
                        "english",
                        "adaptive",
                        "energetic",
                        "high-social",
                        "burst"
                ),
                mbtiPersona(
                        resolvedLocale,
                        chinese,
                        "ESFP",
                        "氛围营造者",
                        "Experience Host",
                        chinese
                                ? "想要一版好玩不累的东京体验流，重点是氛围和互动感。"
                                : "Design a lively but low-fatigue Tokyo experience flow centered on vibe and interaction.",
                        chinese
                                ? "关注体验氛围和社交互动。"
                                : "Optimizes for atmosphere and social engagement.",
                        "explorer",
                        "experience-first",
                        "social-vibe",
                        "medium",
                        "english",
                        "adaptive",
                        "energetic",
                        "high-social",
                        "steady"
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
                        "N/A",
                        chinese ? "受限体验" : "Restricted",
                        chinese ? "安全优先" : "Safety first",
                        Map.of(
                                "travelStyle", "local-light",
                                "budgetLevel", "low",
                                "studyGoal", "steady",
                                "mbtiType", "N/A",
                                "temperament", "restricted",
                                "decisionLens", "safety-first",
                                "planningDepth", "balanced",
                                "feedbackStyle", "supportive",
                                "energyMode", "balanced",
                                "studyCadence", "steady"
                        )
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
                        "N/A",
                        chinese ? "运营治理" : "Operations",
                        chinese ? "SLO 与风险控制" : "SLO and risk control",
                        Map.of(
                                "travelStyle", "observer",
                                "budgetLevel", "ops",
                                "studyGoal", "reliability",
                                "mbtiType", "N/A",
                                "temperament", "operations",
                                "decisionLens", "slo-risk-control",
                                "planningDepth", "deep",
                                "feedbackStyle", "direct",
                                "energyMode", "balanced",
                                "studyCadence", "steady"
                        )
                ),
                new SimulatedPersona(
                        "urban-traveler",
                        resolvedLocale,
                        chinese ? "都市旅行者（兼容）" : "Urban Traveler (Compat)",
                        "persona-travel",
                        "thread-travel-primary",
                        chinese
                                ? "五一去东京 7 天，预算 1.5 万，尽量少换乘，晚上保留散步时间。"
                                : "Plan a 7-day Tokyo trip with a 15k RMB budget, low fatigue, and calm evenings.",
                        chinese
                                ? "兼容旧链路的旅行身份。"
                                : "Compatibility persona for legacy travel user IDs.",
                        "N/A",
                        chinese ? "兼容身份" : "Compatibility",
                        chinese ? "稳定迁移" : "Stable migration",
                        Map.of(
                                "travelStyle", "city-walk",
                                "budgetLevel", "high",
                                "studyGoal", "minimal",
                                "mbtiType", "N/A",
                                "temperament", "compat",
                                "decisionLens", "stable-migration",
                                "planningDepth", "balanced",
                                "feedbackStyle", "direct",
                                "energyMode", "balanced",
                                "studyCadence", "steady"
                        )
                ),
                new SimulatedPersona(
                        "habit-builder",
                        resolvedLocale,
                        chinese ? "习惯坚持者（兼容）" : "Habit Builder (Compat)",
                        "persona-study",
                        "thread-study-primary",
                        chinese
                                ? "下周工作很忙，帮我保住英语听力和晚间锻炼，不要安排太满。"
                                : "Next week is packed. Protect my English listening habit and evening workouts without overloading the schedule.",
                        chinese
                                ? "兼容旧链路的学习身份。"
                                : "Compatibility persona for legacy study user IDs.",
                        "N/A",
                        chinese ? "兼容身份" : "Compatibility",
                        chinese ? "稳定迁移" : "Stable migration",
                        Map.of(
                                "travelStyle", "light",
                                "budgetLevel", "medium",
                                "studyGoal", "english",
                                "mbtiType", "N/A",
                                "temperament", "compat",
                                "decisionLens", "stable-migration",
                                "planningDepth", "balanced",
                                "feedbackStyle", "supportive",
                                "energyMode", "balanced",
                                "studyCadence", "steady"
                        )
                )
        );
    }

    private SimulatedPersona mbtiPersona(String locale,
                                         boolean chinese,
                                         String mbtiType,
                                         String chineseName,
                                         String englishName,
                                         String prompt,
                                         String description,
                                         String temperamentGroup,
                                         String decisionLensCode,
                                         String travelStyle,
                                         String budgetLevel,
                                         String studyGoal,
                                         String planningDepth,
                                         String feedbackStyle,
                                         String energyMode,
                                         String studyCadence) {
        String lowerType = mbtiType.toLowerCase();
        String temperament = temperamentLabel(temperamentGroup, chinese);
        return new SimulatedPersona(
                "mbti-" + lowerType,
                locale,
                chinese ? mbtiType + " · " + chineseName : mbtiType + " · " + englishName,
                "persona-mbti-" + lowerType,
                "thread-mbti-" + lowerType + "-primary",
                prompt,
                description,
                mbtiType,
                temperament,
                decisionLensLabel(decisionLensCode, chinese),
                Map.of(
                        "travelStyle", travelStyle,
                        "budgetLevel", budgetLevel,
                        "studyGoal", studyGoal,
                        "mbtiType", mbtiType,
                        "temperament", temperamentGroup,
                        "decisionLens", decisionLensCode,
                        "planningDepth", planningDepth,
                        "feedbackStyle", feedbackStyle,
                        "energyMode", energyMode,
                        "studyCadence", studyCadence
                )
        );
    }

    private String temperamentLabel(String temperamentGroup, boolean chinese) {
        return switch (temperamentGroup) {
            case "analyst" -> chinese ? "分析型" : "Analyst";
            case "diplomat" -> chinese ? "理想型" : "Diplomat";
            case "sentinel" -> chinese ? "守护型" : "Sentinel";
            case "explorer" -> chinese ? "探索型" : "Explorer";
            default -> chinese ? "通用型" : "General";
        };
    }

    private String decisionLensLabel(String decisionLensCode, boolean chinese) {
        return switch (decisionLensCode) {
            case "systems-thinking" -> chinese ? "系统思维" : "Systems thinking";
            case "evidence-first" -> chinese ? "证据优先" : "Evidence first";
            case "outcome-driven" -> chinese ? "结果导向" : "Outcome driven";
            case "possibility-scouting" -> chinese ? "机会探索" : "Possibility scouting";
            case "meaning-alignment" -> chinese ? "价值对齐" : "Meaning alignment";
            case "harmony-first" -> chinese ? "关系优先" : "Harmony first";
            case "people-alignment" -> chinese ? "协同对齐" : "People alignment";
            case "process-discipline" -> chinese ? "流程纪律" : "Process discipline";
            case "direct-action" -> chinese ? "快速行动" : "Direct action";
            case "experience-first" -> chinese ? "体验优先" : "Experience first";
            default -> chinese ? "平衡判断" : "Balanced judgment";
        };
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
        if (userId != null && userId.startsWith("persona-mbti-")) {
            return "mbti-" + userId.substring("persona-mbti-".length());
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
                mcpTrustedUserPrefixes = List.of("lifeos-", "persona-travel", "persona-mbti-", "persona-ops");
            }
            if (outboundAllowlist == null || outboundAllowlist.isEmpty()) {
                outboundAllowlist = List.of("api.openai.com", "localhost", "127.0.0.1", "open.fly.ai", "clawhub.ai", "flyai.example");
            }
        }
    }
}
