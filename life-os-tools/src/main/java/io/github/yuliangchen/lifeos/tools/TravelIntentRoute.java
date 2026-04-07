package io.github.yuliangchen.lifeos.tools;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 旅行意图路由结果，描述是否命中旅行场景和对应命令。
 * Travel intent route result describing whether travel flow is selected.
 */
public record TravelIntentRoute(
        boolean travelIntent,
        String routeType,
        String primaryCommand,
        List<String> fallbackCommands,
        String reason
) {

    private static final Set<String> SUPPORTED_COMMANDS = Set.of(
            "ai-search",
            "keyword-search",
            "search-flight",
            "search-train",
            "search-hotel",
            "search-poi"
    );

    /**
     * 规范化路由对象字段并过滤非法命令。
     * Normalizes route fields and filters unsupported commands.
     */
    public TravelIntentRoute {
        routeType = hasText(routeType) ? routeType : "general-qa";
        reason = hasText(reason) ? reason : "n/a";

        if (!travelIntent) {
            primaryCommand = "";
            fallbackCommands = List.of();
        } else {
            String normalizedPrimary = normalizeCommand(primaryCommand);
            if (!hasText(normalizedPrimary)) {
                normalizedPrimary = "ai-search";
            }
            primaryCommand = normalizedPrimary;

            LinkedHashSet<String> merged = new LinkedHashSet<>();
            if (fallbackCommands != null) {
                for (String fallback : fallbackCommands) {
                    String normalizedFallback = normalizeCommand(fallback);
                    if (hasText(normalizedFallback) && !normalizedPrimary.equals(normalizedFallback)) {
                        merged.add(normalizedFallback);
                    }
                }
            }
            fallbackCommands = List.copyOf(new ArrayList<>(merged));
        }
    }

    /**
     * 构建普通问答路由结果。
     * Builds general QA route result.
     */
    public static TravelIntentRoute generalQa(String reason) {
        return new TravelIntentRoute(false, "general-qa", "", List.of(), reason);
    }

    /**
     * 构建旅行路由结果。
     * Builds travel route result.
     */
    public static TravelIntentRoute travel(String routeType,
                                           String primaryCommand,
                                           List<String> fallbackCommands,
                                           String reason) {
        return new TravelIntentRoute(true, routeType, primaryCommand, fallbackCommands, reason);
    }

    /**
     * 检查命令是否在支持列表中。
     * Checks whether command is supported.
     */
    public static boolean isSupportedCommand(String command) {
        return hasText(command) && SUPPORTED_COMMANDS.contains(command.trim().toLowerCase());
    }

    /**
     * 规范化命令字符串。
     * Normalizes command token.
     */
    private static String normalizeCommand(String value) {
        if (!hasText(value)) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        return SUPPORTED_COMMANDS.contains(normalized) ? normalized : null;
    }

    /**
     * 检查文本是否非空白。
     * Returns true when text is non-blank.
     */
    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
