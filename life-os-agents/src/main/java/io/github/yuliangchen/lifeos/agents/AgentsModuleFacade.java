package io.github.yuliangchen.lifeos.agents;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import org.springframework.stereotype.Component;

@Component
public class AgentsModuleFacade implements ModuleExecutable<String, String> {

    private final TravelAgent travelAgent;
    private final LearningAgent learningAgent;
    private final ScheduleAgent scheduleAgent;

    public AgentsModuleFacade(TravelAgent travelAgent,
                              LearningAgent learningAgent,
                              ScheduleAgent scheduleAgent) {
        this.travelAgent = travelAgent;
        this.learningAgent = learningAgent;
        this.scheduleAgent = scheduleAgent;
    }

    @Override
    public String moduleName() {
        return "life-os-agents";
    }

    @Override
    public String execute(String input) {
        return String.join(" | ",
                travelAgent.executeProbe(),
                learningAgent.executeProbe(),
                scheduleAgent.executeProbe()
        );
    }

    @Override
    public String executeProbe() {
        return execute("probe");
    }
}
