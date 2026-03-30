package io.github.yuliangchen.lifeos.agents;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.domain.model.AgentContribution;
import io.github.yuliangchen.lifeos.domain.model.AgentTask;
import io.github.yuliangchen.lifeos.domain.model.PlanTask;
import io.github.yuliangchen.lifeos.domain.model.TaskStatus;
import io.github.yuliangchen.lifeos.domain.model.ToolRequest;
import io.github.yuliangchen.lifeos.domain.model.ToolResult;
import io.github.yuliangchen.lifeos.domain.support.LocaleSupport;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduleAgent implements ModuleExecutable<AgentTask, AgentContribution> {

    private final ToolModuleFacade toolModuleFacade;

    public ScheduleAgent(ToolModuleFacade toolModuleFacade) {
        this.toolModuleFacade = toolModuleFacade;
    }

    @Override
    public String moduleName() {
        return "schedule-agent";
    }

    @Override
    public AgentContribution execute(AgentTask input) {
        String locale = LocaleSupport.resolve(input.locale(), input.objective());
        ToolResult calendarDraft = toolModuleFacade.execute(new ToolRequest("calendar", Map.of("purpose", "travel-balance")));
        ToolResult reminderDraft = toolModuleFacade.execute(new ToolRequest("reminder", Map.of("purpose", "daily-english")));

        PlanTask task = new PlanTask(
                "task-schedule-balance",
                LocaleSupport.pick(locale, "创建平衡时间块", "Create balanced time blocks"),
                LocaleSupport.pick(
                        locale,
                        "生成保护休息时间与日常练习的日历草稿和提醒草稿。",
                        "Prepare calendar and reminder drafts that protect travel rest time and daily practice."
                ),
                TaskStatus.READY_FOR_CONFIRMATION,
                moduleName(),
                Instant.now().plusSeconds(86400)
        );

        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("calendarDraft", calendarDraft.summary());
        metadata.put("reminderDraft", reminderDraft.summary());

        return new AgentContribution(
                moduleName(),
                LocaleSupport.pick(
                        locale,
                        "已准备时间块和提醒草稿，外部写入前需用户确认。",
                        "Prepared time blocks and reminder drafts, pending user confirmation before writing externally."
                ),
                List.of(task),
                metadata
        );
    }

    @Override
    public String executeDemo() {
        return execute(new AgentTask(
                "demo-user",
                io.github.yuliangchen.lifeos.domain.model.SpecialistType.SCHEDULE,
                "Balance travel and routines",
                List.of("require confirmation for writes"),
                "en-US"
        )).summary();
    }
}
