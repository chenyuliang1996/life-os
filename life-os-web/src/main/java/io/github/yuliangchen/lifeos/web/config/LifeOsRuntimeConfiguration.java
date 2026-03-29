package io.github.yuliangchen.lifeos.web.config;

import io.agentscope.core.session.JsonSession;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
@EnableConfigurationProperties(LifeOsRuntimeConfiguration.LifeOsStorageProperties.class)
public class LifeOsRuntimeConfiguration {

    @Bean
    JsonSession jsonSession(LifeOsStorageProperties properties) {
        return new JsonSession(Path.of(properties.sessionStorageDir()));
    }

    @ConfigurationProperties(prefix = "lifeos")
    public record LifeOsStorageProperties(String sessionStorageDir) {

        public LifeOsStorageProperties {
            if (sessionStorageDir == null || sessionStorageDir.isBlank()) {
                sessionStorageDir = ".data/sessions";
            }
        }
    }
}
