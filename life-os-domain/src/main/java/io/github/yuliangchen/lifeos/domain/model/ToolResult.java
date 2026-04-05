package io.github.yuliangchen.lifeos.domain.model;

import java.util.Map;

public record ToolResult(
        String toolName,
        String summary,
        Map<String, String> metadata
) {
    public ToolResult(String toolName, String summary) {
        this(toolName, summary, Map.of());
    }
}
