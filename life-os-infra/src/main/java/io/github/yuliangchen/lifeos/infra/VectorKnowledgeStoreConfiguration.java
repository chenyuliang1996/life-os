package io.github.yuliangchen.lifeos.infra;

import io.github.yuliangchen.lifeos.domain.service.VectorKnowledgeStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties(AgentScopePgVectorKnowledgeStore.VectorRagProperties.class)
public class VectorKnowledgeStoreConfiguration {

    @Bean
    @ConditionalOnProperty(name = "lifeos.rag.vector.enabled", havingValue = "true")
    public VectorKnowledgeStore pgVectorKnowledgeStore(AgentScopePgVectorKnowledgeStore.VectorRagProperties properties,
                                                       Environment environment) {
        return new AgentScopePgVectorKnowledgeStore(properties, environment);
    }

    @Bean
    @ConditionalOnMissingBean(VectorKnowledgeStore.class)
    public VectorKnowledgeStore noOpVectorKnowledgeStore() {
        return new NoOpVectorKnowledgeStore();
    }
}
