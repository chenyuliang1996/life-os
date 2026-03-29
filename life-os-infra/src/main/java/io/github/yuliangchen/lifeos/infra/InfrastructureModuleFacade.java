package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import org.springframework.stereotype.Component;

@Component
public class InfrastructureModuleFacade implements ModuleExecutable<String, String> {

    @Override
    public String moduleName() {
        return "life-os-infra";
    }

    @Override
    public String execute(String input) {
        return "Infra received: " + input;
    }

    @Override
    public String executeDemo() {
        return "Infra repositories are ready for in-memory demos";
    }
}
