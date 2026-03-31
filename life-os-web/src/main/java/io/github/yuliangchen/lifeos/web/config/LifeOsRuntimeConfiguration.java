package io.github.yuliangchen.lifeos.web.config;

import io.agentscope.core.session.JsonSession;
import io.github.yuliangchen.lifeos.domain.model.SystemArchitectureStatus;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
@EnableConfigurationProperties({
        LifeOsRuntimeConfiguration.LifeOsStorageProperties.class,
        LifeOsRuntimeConfiguration.LifeOsArchitectureProperties.class
})
public class LifeOsRuntimeConfiguration {

    @Bean
    JsonSession jsonSession(LifeOsStorageProperties properties) {
        return new JsonSession(Path.of(properties.sessionStorageDir()));
    }

    @Bean
    SystemArchitectureStatus systemArchitectureStatus(LifeOsArchitectureProperties properties,
                                                      LifeOsStorageProperties storageProperties) {
        return new SystemArchitectureStatus(
                properties.deploymentMode(),
                properties.persistenceMode(),
                properties.primaryDatabase(),
                properties.ragStore(),
                properties.sessionStore(),
                properties.topology(),
                properties.travelSearch(),
                properties.travelSpecialist(),
                "Session files live at " + storageProperties.sessionStorageDir()
        );
    }

    @ConfigurationProperties(prefix = "lifeos")
    public record LifeOsStorageProperties(String sessionStorageDir) {

        public LifeOsStorageProperties {
            if (sessionStorageDir == null || sessionStorageDir.isBlank()) {
                sessionStorageDir = ".data/sessions";
            }
        }
    }

    @ConfigurationProperties(prefix = "lifeos.architecture")
    public record LifeOsArchitectureProperties(
            String deploymentMode,
            String persistenceMode,
            String primaryDatabase,
            String ragStore,
            String sessionStore,
            String topology,
            String travelSearch,
            String travelSpecialist
    ) {

        public LifeOsArchitectureProperties {
            if (deploymentMode == null || deploymentMode.isBlank()) {
                deploymentMode = "single-node";
            }
            if (persistenceMode == null || persistenceMode.isBlank()) {
                persistenceMode = "database";
            }
            if (primaryDatabase == null || primaryDatabase.isBlank()) {
                primaryDatabase = "h2-file";
            }
            if (ragStore == null || ragStore.isBlank()) {
                ragStore = "postgres-text-now-pgvector-target";
            }
            if (sessionStore == null || sessionStore.isBlank()) {
                sessionStore = "json-session";
            }
            if (topology == null || topology.isBlank()) {
                topology = "modular-monolith-ready-for-cluster";
            }
            if (travelSearch == null || travelSearch.isBlank()) {
                travelSearch = "seeded-search-plus-optional-flyai";
            }
            if (travelSpecialist == null || travelSpecialist.isBlank()) {
                travelSpecialist = "local-travel-agent-plus-optional-a2a";
            }
        }
    }
}
