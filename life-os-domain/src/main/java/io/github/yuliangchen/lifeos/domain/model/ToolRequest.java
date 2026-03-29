package io.github.yuliangchen.lifeos.domain.model;

import java.util.Map;

public record ToolRequest(
        String toolName,
        Map<String, String> parameters
) {
}
