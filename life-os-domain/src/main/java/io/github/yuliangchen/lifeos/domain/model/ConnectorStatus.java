package io.github.yuliangchen.lifeos.domain.model;

public record ConnectorStatus(
        String connectorName,
        boolean enabled,
        String summary
) {
}
