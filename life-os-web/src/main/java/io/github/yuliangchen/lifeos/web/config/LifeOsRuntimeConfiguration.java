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
/**
 * Web 运行时配置，注册会话存储与系统架构状态 Bean。
 * Web runtime configuration registering session storage and architecture status beans.
 */
public class LifeOsRuntimeConfiguration {

    @Bean
    /**
     * 构建 AgentScope JsonSession。
     * Builds AgentScope JsonSession store.
     */
    JsonSession jsonSession(LifeOsStorageProperties properties) {
        return new JsonSession(Path.of(properties.sessionStorageDir()));
    }

    @Bean
    /**
     * 构建系统架构状态快照对象。
     * Builds system architecture status snapshot bean.
     */
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
    /**
     * 存储相关配置。
     * Storage-related configuration properties.
     */
    public record LifeOsStorageProperties(String sessionStorageDir) {

        /**
         * 设置默认会话目录。
         * Applies default session storage directory.
         */
        public LifeOsStorageProperties {
            if (sessionStorageDir == null || sessionStorageDir.isBlank()) {
                sessionStorageDir = ".data/sessions";
            }
        }
    }

    @ConfigurationProperties(prefix = "lifeos.architecture")
    /**
     * 架构与拓扑展示配置。
     * Architecture and topology presentation properties.
     */
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

        /**
         * 应用架构配置默认值。
         * Applies default values for architecture presentation fields.
         */
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
                travelSearch = "intent-router-plus-flyai-cli-with-fallback";
            }
            if (travelSpecialist == null || travelSpecialist.isBlank()) {
                travelSpecialist = "local-travel-agent-plus-optional-a2a";
            }
        }
    }
}
