package io.github.yuliangchen.lifeos.domain;

public interface ModuleExecutable<I, O> {

    String moduleName();

    O execute(I input);

    default String executeDemo() {
        return moduleName() + " is ready";
    }
}
