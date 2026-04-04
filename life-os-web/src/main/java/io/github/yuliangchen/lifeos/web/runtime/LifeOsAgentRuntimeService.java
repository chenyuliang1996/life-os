package io.github.yuliangchen.lifeos.web.runtime;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.memory.InMemoryMemory;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.MsgRole;
import io.agentscope.core.model.Model;
import io.agentscope.core.model.OllamaChatModel;
import io.agentscope.core.model.OpenAIChatModel;
import io.agentscope.core.session.JsonSession;
import io.agentscope.core.state.SimpleSessionKey;
import io.agentscope.core.tool.Toolkit;
import io.github.yuliangchen.lifeos.domain.model.AgentRuntimeStatus;
import io.github.yuliangchen.lifeos.domain.model.AssistantContinuationRequest;
import io.github.yuliangchen.lifeos.domain.model.AssistantReply;
import io.github.yuliangchen.lifeos.domain.model.AssistantRequest;
import io.github.yuliangchen.lifeos.domain.model.ExecutionContinuationResult;
import io.github.yuliangchen.lifeos.domain.model.OrchestrationResult;
import io.github.yuliangchen.lifeos.domain.model.PlanPreviewRequest;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import io.github.yuliangchen.lifeos.orchestrator.LifeExecutionContinuationService;
import io.github.yuliangchen.lifeos.orchestrator.LifeOrchestrator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@EnableConfigurationProperties(LifeOsAgentRuntimeService.LifeOsAgentProperties.class)
public class LifeOsAgentRuntimeService {

    private final LifeOrchestrator lifeOrchestrator;
    private final LifeExecutionContinuationService lifeExecutionContinuationService;
    private final JsonSession jsonSession;
    private final LifeOsToolset lifeOsToolset;
    private final LifeOsAgentProperties properties;

    public LifeOsAgentRuntimeService(LifeOrchestrator lifeOrchestrator,
                                     LifeExecutionContinuationService lifeExecutionContinuationService,
                                     JsonSession jsonSession,
                                     LifeOsToolset lifeOsToolset,
                                     LifeOsAgentProperties properties) {
        this.lifeOrchestrator = lifeOrchestrator;
        this.lifeExecutionContinuationService = lifeExecutionContinuationService;
        this.jsonSession = jsonSession;
        this.lifeOsToolset = lifeOsToolset;
        this.properties = properties;
    }

    public AgentRuntimeStatus status() {
        Optional<ModelHolder> holder = createModelHolder();
        if (holder.isPresent()) {
            return new AgentRuntimeStatus(
                    "agentscope-react",
                    true,
                    holder.get().provider(),
                    holder.get().model().getModelName(),
                    "AgentScope ReAct runtime is active with real model access."
            );
        }
        return new AgentRuntimeStatus(
                "orchestrator-fallback",
                false,
                "deterministic",
                "none",
                "No external model is configured, so the app uses the deterministic orchestrator fallback."
        );
    }

    public AssistantReply reply(AssistantRequest request) {
        Optional<ModelHolder> holder = createModelHolder();
        if (holder.isPresent()) {
            try {
                return runReActReply(request, holder.get());
            } catch (Exception exception) {
                AssistantReply fallback = runFallbackReply(request);
                return new AssistantReply(
                        fallback.threadId(),
                        fallback.mode(),
                        fallback.message() + "\n\nRuntime note: model-backed execution failed, so the app fell back to the deterministic planner.",
                        fallback.planId(),
                        fallback.runId(),
                        fallback.highlights()
                );
            }
        }
        return runFallbackReply(request);
    }

    public ExecutionContinuationResult resume(AssistantContinuationRequest request) {
        String locale = LocaleSupport.resolve(request.locale(), null);
        return lifeExecutionContinuationService.resumePlan(request.planId(), request.userId(), locale);
    }

    private AssistantReply runReActReply(AssistantRequest request, ModelHolder holder) {
        String locale = LocaleSupport.resolve(request.locale(), request.input());
        ReActAgent agent = buildAgent(holder.model());
        SimpleSessionKey sessionKey = SimpleSessionKey.of(request.threadId());
        agent.loadIfExists(jsonSession, sessionKey);

        Msg response = agent.call(Msg.builder()
                .name(request.userId())
                .role(MsgRole.USER)
                .textContent(request.input())
                .metadata(java.util.Map.of("threadId", request.threadId()))
                .build()).block();

        agent.saveTo(jsonSession, sessionKey);

        return new AssistantReply(
                request.threadId(),
                "agentscope-react",
                response != null ? response.getTextContent() : "No response generated.",
                null,
                null,
                List.of(
                        LocaleSupport.pick(locale, "已使用 AgentScope ReAct 实时运行时", "Used the AgentScope ReAct runtime"),
                        LocaleSupport.pick(locale, "已加载并保存会话状态", "Loaded and saved session state"),
                        LocaleSupport.pick(locale, "可结合工具完成规划与执行判断", "Tool-backed planning is available")
                )
        );
    }

    private AssistantReply runFallbackReply(AssistantRequest request) {
        String locale = LocaleSupport.resolve(request.locale(), request.input());
        OrchestrationResult result = lifeOrchestrator.execute(new PlanPreviewRequest(
                request.userId(),
                request.threadId(),
                request.input(),
                locale,
                request.sessionId(),
                request.contextId(),
                request.traceId()
        ));

        String taskLines = result.plan().tasks().stream()
                .map(task -> LocaleSupport.pick(locale, "- " + task.title() + "（" + task.status() + "）", "- " + task.title() + " (" + task.status() + ")"))
                .reduce((left, right) -> left + "\n" + right)
                .orElse(LocaleSupport.pick(locale, "- 暂无任务", "- No tasks"));

        String message = LocaleSupport.pick(
                locale,
                """
                        我已使用确定性编排器生成 Life OS 方案。

                        摘要：%s

                        关键任务：
                        %s
                        """.formatted(result.plan().summary(), taskLines),
                """
                        I built a grounded Life OS response using the deterministic planner.

                        Summary: %s

                        Top tasks:
                        %s
                        """.formatted(result.plan().summary(), taskLines)
        );

        return new AssistantReply(
                request.threadId(),
                "orchestrator-fallback",
                message + LocaleSupport.pick(
                        locale,
                        "\n\n如需继续执行外部动作，请先完成确认。",
                        "\n\nComplete the pending confirmations before resuming external actions."
                ),
                result.plan().id(),
                result.executionRun().id(),
                List.of(
                        LocaleSupport.pick(locale, "已生成并持久化行动方案", "Generated and stored an action plan"),
                        LocaleSupport.pick(locale, "已记录执行时间线", "Captured the execution timeline"),
                        LocaleSupport.pick(locale, "已排入需要确认的外部动作", "Queued the confirmation steps")
                )
        );
    }

    private ReActAgent buildAgent(Model model) {
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(lifeOsToolset);
        return ReActAgent.builder()
                .name("life-os-react")
                .description("Life OS consumer assistant")
                .sysPrompt("""
                        You are Life OS, a consumer planning copilot.
                        For travel, learning, and life-coordination requests, use tools to ground your answer before responding.
                        If the request needs a concrete plan, call life_plan_preview first.
                        Always mention pending confirmations if tool output includes them.
                        Keep the final answer concise, practical, and user-friendly.
                        """)
                .model(model)
                .toolkit(toolkit)
                .memory(new InMemoryMemory())
                .enablePlan()
                .maxIters(4)
                .build();
    }

    private Optional<ModelHolder> createModelHolder() {
        if (StringUtils.hasText(properties.openaiApiKey())) {
            OpenAIChatModel.Builder builder = OpenAIChatModel.builder()
                    .apiKey(properties.openaiApiKey())
                    .modelName(properties.openaiModel())
                    .stream(false);
            if (StringUtils.hasText(properties.openaiBaseUrl())) {
                builder.baseUrl(properties.openaiBaseUrl());
            }
            return Optional.of(new ModelHolder("openai", builder.build()));
        }

        if (StringUtils.hasText(properties.ollamaBaseUrl()) && StringUtils.hasText(properties.ollamaModel())) {
            OllamaChatModel model = OllamaChatModel.builder()
                    .baseUrl(properties.ollamaBaseUrl())
                    .modelName(properties.ollamaModel())
                    .build();
            return Optional.of(new ModelHolder("ollama", model));
        }

        return Optional.empty();
    }

    private record ModelHolder(String provider, Model model) {
    }

    @ConfigurationProperties(prefix = "lifeos.runtime")
    public record LifeOsAgentProperties(
            String openaiApiKey,
            String openaiBaseUrl,
            String openaiModel,
            String ollamaBaseUrl,
            String ollamaModel
    ) {

        public LifeOsAgentProperties {
            if (openaiModel == null || openaiModel.isBlank()) {
                openaiModel = "gpt-4.1-mini";
            }
            if (ollamaModel == null || ollamaModel.isBlank()) {
                ollamaModel = "qwen2.5:7b";
            }
        }
    }
}
