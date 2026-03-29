package io.github.yuliangchen.lifeos.web;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import io.github.yuliangchen.lifeos.orchestrator.LifeOrchestrator;
import org.springframework.stereotype.Component;

@Component
public class WebModuleFacade implements ModuleExecutable<String, String> {

    private final LifeOrchestrator lifeOrchestrator;

    public WebModuleFacade(LifeOrchestrator lifeOrchestrator) {
        this.lifeOrchestrator = lifeOrchestrator;
    }

    @Override
    public String moduleName() {
        return "life-os-web";
    }

    @Override
    public String execute(String input) {
        return lifeOrchestrator.executeDemo() + " | input=" + input;
    }

    @Override
    public String executeDemo() {
        return "Web module ready, orchestrator says: " + lifeOrchestrator.executeDemo();
    }
}
