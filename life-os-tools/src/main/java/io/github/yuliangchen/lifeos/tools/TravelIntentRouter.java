package io.github.yuliangchen.lifeos.tools;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
/**
 * 旅行意图路由器：识别普通问答与旅行检索，并映射 FlyAI 命令。
 * Intent router that classifies general QA vs travel and maps FlyAI commands.
 */
public class TravelIntentRouter {

    /**
     * 对工具入参执行意图识别与命令映射。
     * Performs intent recognition and command mapping for tool parameters.
     */
    public TravelIntentRoute route(Map<String, String> parameters) {
        Map<String, String> safeParameters = parameters == null ? Map.of() : parameters;

        String explicitCommand = normalize(safeParameters.get("travel_command"));
        if (TravelIntentRoute.isSupportedCommand(explicitCommand)) {
            return buildByCommand("travel-explicit", explicitCommand, "Explicit travel command override.");
        }

        String intent = normalize(safeParameters.get("intent"));
        if ("general".equals(intent) || "qa".equals(intent) || "chat".equals(intent)) {
            return TravelIntentRoute.generalQa("Intent explicitly marked as non-travel.");
        }
        if (StringUtils.hasText(intent)) {
            TravelIntentRoute routeFromIntent = routeByIntentLabel(intent);
            if (routeFromIntent != null) {
                return routeFromIntent;
            }
        }

        String topic = resolveTopic(safeParameters);
        String loweredTopic = topic.toLowerCase(Locale.ROOT);
        boolean hasContext = hasTravelContext(safeParameters);

        if (isFlightIntent(loweredTopic, safeParameters)) {
            return TravelIntentRoute.travel(
                    "travel-flight",
                    "search-flight",
                    List.of(),
                    "Flight-related signal detected from query/context."
            );
        }
        if (isTrainIntent(loweredTopic, safeParameters)) {
            return TravelIntentRoute.travel(
                    "travel-train",
                    "search-train",
                    List.of(),
                    "Train-related signal detected from query/context."
            );
        }
        if (isHotelIntent(loweredTopic, safeParameters)) {
            return TravelIntentRoute.travel(
                    "travel-hotel",
                    "search-hotel",
                    List.of(),
                    "Hotel-related signal detected from query/context."
            );
        }
        if (isPoiIntent(loweredTopic, safeParameters)) {
            return TravelIntentRoute.travel(
                    "travel-poi",
                    "search-poi",
                    List.of(),
                    "POI-related signal detected from query/context."
            );
        }

        if (containsAny(loweredTopic, List.of(
                "travel", "trip", "vacation", "itinerary", "holiday", "destination",
                "tour", "tourism", "getaway", "weekend",
                "旅行", "旅游", "出行", "度假", "行程", "周边游", "出国游", "目的地"))) {
            return TravelIntentRoute.travel(
                    "travel-generic",
                    "ai-search",
                    List.of("keyword-search"),
                    "Generic travel signal detected from query."
            );
        }

        if (hasContext) {
            return TravelIntentRoute.travel(
                    "travel-contextual",
                    "ai-search",
                    List.of("keyword-search"),
                    "Travel context keys detected from request parameters."
            );
        }

        return TravelIntentRoute.generalQa("No travel-related signal was detected.");
    }

    /**
     * 按显式 intent 标签映射命令。
     * Maps explicit intent labels to route commands.
     */
    private TravelIntentRoute routeByIntentLabel(String intent) {
        return switch (intent) {
            case "flight", "air", "airfare", "plane", "机票", "航班" -> TravelIntentRoute.travel(
                    "travel-flight",
                    "search-flight",
                    List.of(),
                    "Explicit flight intent detected."
            );
            case "train", "rail", "火车", "高铁" -> TravelIntentRoute.travel(
                    "travel-train",
                    "search-train",
                    List.of(),
                    "Explicit train intent detected."
            );
            case "hotel", "stay", "住宿", "酒店", "民宿" -> TravelIntentRoute.travel(
                    "travel-hotel",
                    "search-hotel",
                    List.of(),
                    "Explicit hotel intent detected."
            );
            case "poi", "attraction", "sightseeing", "景点", "玩法" -> TravelIntentRoute.travel(
                    "travel-poi",
                    "search-poi",
                    List.of(),
                    "Explicit POI intent detected."
            );
            case "travel", "trip", "vacation", "itinerary", "旅游", "旅行", "行程" -> TravelIntentRoute.travel(
                    "travel-generic",
                    "ai-search",
                    List.of("keyword-search"),
                    "Explicit generic travel intent detected."
            );
            case "keyword-search" -> TravelIntentRoute.travel(
                    "travel-keyword",
                    "keyword-search",
                    List.of(),
                    "Explicit keyword travel search intent detected."
            );
            case "ai-search" -> TravelIntentRoute.travel(
                    "travel-ai-search",
                    "ai-search",
                    List.of("keyword-search"),
                    "Explicit AI travel search intent detected."
            );
            default -> null;
        };
    }

    /**
     * 显式命令构建旅行路由。
     * Builds route when command is explicitly provided.
     */
    private TravelIntentRoute buildByCommand(String routeType, String command, String reason) {
        List<String> fallback = "ai-search".equals(command) ? List.of("keyword-search") : List.of();
        return TravelIntentRoute.travel(routeType, command, fallback, reason);
    }

    /**
     * 识别机票意图。
     * Detects flight-search intent.
     */
    private boolean isFlightIntent(String loweredTopic, Map<String, String> parameters) {
        return containsAny(loweredTopic, List.of("flight", "airfare", "plane", "机票", "航班"))
                || hasAnyKey(parameters, List.of("flight_no", "flightNo", "depart_city", "arrive_city", "origin", "destination"));
    }

    /**
     * 识别火车意图。
     * Detects train-search intent.
     */
    private boolean isTrainIntent(String loweredTopic, Map<String, String> parameters) {
        return containsAny(loweredTopic, List.of("train", "rail", "火车", "高铁"))
                || hasAnyKey(parameters, List.of("train_no", "trainNo", "railway", "depart_station", "arrive_station"));
    }

    /**
     * 识别酒店意图。
     * Detects hotel-search intent.
     */
    private boolean isHotelIntent(String loweredTopic, Map<String, String> parameters) {
        return containsAny(loweredTopic, List.of("hotel", "stay", "resort", "住宿", "酒店", "民宿"))
                || hasAnyKey(parameters, List.of("checkin", "checkout", "check_in", "check_out", "nights"));
    }

    /**
     * 识别景点意图。
     * Detects POI-search intent.
     */
    private boolean isPoiIntent(String loweredTopic, Map<String, String> parameters) {
        return containsAny(loweredTopic, List.of("poi", "attraction", "museum", "park", "景点", "玩法", "攻略"))
                || hasAnyKey(parameters, List.of("poi", "attraction", "city", "destination"));
    }

    /**
     * 识别是否携带旅行上下文参数。
     * Detects whether request carries travel context keys.
     */
    private boolean hasTravelContext(Map<String, String> parameters) {
        return hasAnyKey(parameters, List.of(
                "travelers", "budget", "time_window", "timeWindow",
                "depart_city", "arrive_city", "origin", "destination",
                "checkin", "checkout", "check_in", "check_out",
                "city", "destination_city", "poi"
        ));
    }

    /**
     * 从请求参数中解析查询主题。
     * Resolves topic from known query parameter aliases.
     */
    private String resolveTopic(Map<String, String> parameters) {
        for (String key : List.of("topic", "query", "input", "keyword", "destination")) {
            String value = parameters.get(key);
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return "";
    }

    /**
     * 判断文本是否包含任一关键词。
     * Returns true when text contains any keyword.
     */
    private boolean containsAny(String value, List<String> keywords) {
        if (!StringUtils.hasText(value)) {
            return false;
        }
        for (String keyword : keywords) {
            if (StringUtils.hasText(keyword) && value.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断参数表是否命中任一键并且值非空。
     * Checks whether any key exists with non-blank value.
     */
    private boolean hasAnyKey(Map<String, String> parameters, List<String> keys) {
        for (String key : keys) {
            if (StringUtils.hasText(parameters.get(key))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 归一化文本为小写并去空格。
     * Normalizes text to trimmed lower-case string.
     */
    private String normalize(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
