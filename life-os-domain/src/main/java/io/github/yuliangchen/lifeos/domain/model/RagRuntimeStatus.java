package io.github.yuliangchen.lifeos.domain.model;

public record RagRuntimeStatus(
        boolean enabled,
        boolean vectorReady,
        String retrievalMode,
        String store,
        String provider,
        String modelName,
        int dimensions,
        String summary
) {
}
