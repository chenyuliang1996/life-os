package io.github.yuliangchen.lifeos.domain.model;

public record SystemArchitectureStatus(
        String deploymentMode,
        String persistenceMode,
        String database,
        String ragStore,
        String sessionStore,
        String topology,
        String travelSearch,
        String travelSpecialist,
        String notes
) {
}
