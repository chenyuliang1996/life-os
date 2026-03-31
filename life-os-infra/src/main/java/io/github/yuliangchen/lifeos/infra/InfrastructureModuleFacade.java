package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.ModuleExecutable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InfrastructureModuleFacade implements ModuleExecutable<String, String> {

    private final String persistenceMode;

    public InfrastructureModuleFacade(@Value("${lifeos.persistence.mode:database}") String persistenceMode) {
        this.persistenceMode = persistenceMode;
    }

    @Override
    public String moduleName() {
        return "life-os-infra";
    }

    @Override
    public String execute(String input) {
        return "Infra received: " + input;
    }

    @Override
    public String executeProbe() {
        return "Infra repositories are ready in " + persistenceMode + " mode";
    }
}
