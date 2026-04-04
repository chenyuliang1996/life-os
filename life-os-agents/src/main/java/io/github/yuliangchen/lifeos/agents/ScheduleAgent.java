package io.github.yuliangchen.lifeos.agents;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.AgentContribution;
import io.github.yuliangchen.lifeos.domain.model.AgentTask;
import io.github.yuliangchen.lifeos.domain.model.PlanTask;
import io.github.yuliangchen.lifeos.domain.model.TaskStatus;
import io.github.yuliangchen.lifeos.domain.model.ToolRequest;
import io.github.yuliangchen.lifeos.domain.model.ToolResult;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import io.github.yuliangchen.lifeos.memory.MemoryModuleFacade;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduleAgent implements ModuleExecutable<AgentTask, AgentContribution> {

    private final ToolModuleFacade toolModuleFacade;
    private final MemoryModuleFacade memoryModuleFacade;

    public ScheduleAgent(ToolModuleFacade toolModuleFacade, MemoryModuleFacade memoryModuleFacade) {
        this.toolModuleFacade = toolModuleFacade;
        this.memoryModuleFacade = memoryModuleFacade;
    }

    @Override
    public String moduleName() {
        return "schedule-agent";
    }

    @Override
    public AgentContribution execute(AgentTask input) {
        String locale = LocaleSupport.resolve(input.locale(), input.objective());
        Map<String, String> preferences = memoryModuleFacade.getOrCreate(input.userId()).preferences();
        String planningDepth = preferences.getOrDefault("planningDepth", "balanced");
        String feedbackStyle = preferences.getOrDefault("feedbackStyle", "direct");
        String energyMode = preferences.getOrDefault("energyMode", "balanced");
        ToolResult calendarDraft = toolModuleFacade.execute(new ToolRequest("calendar", Map.of("purpose", "travel-balance")));
        ToolResult reminderDraft = toolModuleFacade.execute(new ToolRequest("reminder", Map.of("purpose", "daily-english")));

        PlanTask task = new PlanTask(
                "task-schedule-balance",
                LocaleSupport.pick(locale, "创建平衡时间块", "Create balanced time blocks"),
                LocaleSupport.pick(
                        locale,
                        "生成保护休息时间与日常练习的日历草稿和提醒草稿。规划深度=" + planningDepth + "，节奏偏好=" + energyMode + "。",
                        "Prepare calendar and reminder drafts that protect travel rest time and daily practice. Planning depth=" + planningDepth + ", energy mode=" + energyMode + "."
                ),
                TaskStatus.READY_FOR_CONFIRMATION,
                moduleName(),
                Instant.now().plusSeconds(86400)
        );

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("calendarDraft", calendarDraft.summary());
        metadata.put("reminderDraft", reminderDraft.summary());
        metadata.put("planningDepth", planningDepth);
        metadata.put("feedbackStyle", feedbackStyle);
        metadata.put("energyMode", energyMode);

        return new AgentContribution(
                moduleName(),
                LocaleSupport.pick(
                        locale,
                        "已准备时间块和提醒草稿，外部写入前需用户确认；反馈风格按人格偏好（" + feedbackStyle + "）输出。",
                        "Prepared time blocks and reminder drafts, pending user confirmation before writing externally, with persona-aware feedback style (" + feedbackStyle + ")."
                ),
                List.of(task),
                metadata
        );
    }

    @Override
    public String executeProbe() {
        return "Schedule specialist is ready to draft balanced time blocks behind approval gates.";
    }
}
