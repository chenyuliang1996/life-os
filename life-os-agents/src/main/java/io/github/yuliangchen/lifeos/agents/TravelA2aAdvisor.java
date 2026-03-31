package io.github.yuliangchen.lifeos.agents;

import io.agentscope.core.a2a.agent.A2aAgent;
import io.agentscope.core.a2a.agent.card.WellKnownAgentCardResolver;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.MsgRole;
import io.github.yuliangchen.lifeos.domain.model.AgentTask;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeSnippet;
import io.github.yuliangchen.lifeos.domain.model.ToolResult;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class TravelA2aAdvisor {

    private static final Logger log = LoggerFactory.getLogger(TravelA2aAdvisor.class);

    private final boolean enabled;
    private final String baseUrl;
    private final String relativeCardPath;
    private final String authHeaderName;
    private final String authHeaderValue;
    private final Duration timeout;

    private volatile A2aAgent a2aAgent;

    public TravelA2aAdvisor(Environment environment) {
        this.enabled = Boolean.parseBoolean(environment.getProperty("lifeos.a2a.travel.enabled", "false"));
        this.baseUrl = environment.getProperty("lifeos.a2a.travel.base-url", "");
        this.relativeCardPath = environment.getProperty("lifeos.a2a.travel.card-path", "/.well-known/agent.json");
        this.authHeaderName = environment.getProperty("lifeos.a2a.travel.auth-header-name", "");
        this.authHeaderValue = environment.getProperty("lifeos.a2a.travel.auth-header-value", "");
        this.timeout = Duration.ofSeconds(Integer.parseInt(environment.getProperty("lifeos.a2a.travel.timeout-seconds", "15")));
    }

    public String advise(AgentTask task, ToolResult searchResult, List<KnowledgeSnippet> snippets) {
        if (!isReady()) {
            return "";
        }

        try {
            Msg response = getOrCreateAgent().call(List.of(buildPrompt(task, searchResult, snippets))).block(timeout);
            if (response == null || !StringUtils.hasText(response.getTextContent())) {
                return "";
            }
            return response.getTextContent().trim();
        } catch (Exception exception) {
            log.warn("A2A travel specialist is unavailable. Falling back to the local travel agent.", exception);
            return "";
        }
    }

    public boolean isEnabled() {
        return isReady();
    }

    private boolean isReady() {
        return enabled && StringUtils.hasText(baseUrl);
    }

    private A2aAgent getOrCreateAgent() {
        if (a2aAgent != null) {
            return a2aAgent;
        }

        synchronized (this) {
            if (a2aAgent != null) {
                return a2aAgent;
            }

            WellKnownAgentCardResolver.Builder resolverBuilder = WellKnownAgentCardResolver.builder()
                    .baseUrl(baseUrl)
                    .relativeCardPath(relativeCardPath);
            if (StringUtils.hasText(authHeaderName) && StringUtils.hasText(authHeaderValue)) {
                resolverBuilder.authHeaders(Map.of(authHeaderName, authHeaderValue));
            }

            a2aAgent = A2aAgent.builder()
                    .name("life-os-travel-a2a-client")
                    .agentCardResolver(resolverBuilder.build())
                    .checkRunning(false)
                    .build();
            return a2aAgent;
        }
    }

    private Msg buildPrompt(AgentTask task, ToolResult searchResult, List<KnowledgeSnippet> snippets) {
        String locale = LocaleSupport.resolve(task.locale(), task.objective());
        String groundedKnowledge = snippets.stream()
                .map(snippet -> snippet.title() + ": " + snippet.snippet())
                .reduce((left, right) -> left + "\n" + right)
                .orElse(LocaleSupport.pick(locale, "暂无额外知识片段。", "No extra knowledge snippets were found."));

        String prompt = LocaleSupport.pick(
                locale,
                """
                        你是远程旅行专家。请基于以下上下文，补充 3 条面向东京 7 天低疲劳行程的旅行建议，重点是片区路线、节奏安排和预算敏感性。
                        用户目标：%s
                        当前搜索摘要：%s
                        知识片段：
                        %s
                        请用简洁条目输出。
                        """.formatted(task.objective(), searchResult.summary(), groundedKnowledge),
                """
                        You are a remote travel specialist. Add 3 concise recommendations for a low-fatigue 7-day Tokyo trip.
                        Focus on neighborhood sequencing, pacing, and budget sensitivity.
                        User objective: %s
                        Current search summary: %s
                        Knowledge snippets:
                        %s
                        Reply with concise bullet points.
                        """.formatted(task.objective(), searchResult.summary(), groundedKnowledge)
        );

        return Msg.builder()
                .id(UUID.randomUUID().toString())
                .name("life-os-orchestrator")
                .role(MsgRole.USER)
                .textContent(prompt)
                .build();
    }
}
