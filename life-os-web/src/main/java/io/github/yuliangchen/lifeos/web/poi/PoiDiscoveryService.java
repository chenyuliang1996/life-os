package io.github.yuliangchen.lifeos.web.poi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yuliangchen.lifeos.domain.model.FestivalPoiCard;
import io.github.yuliangchen.lifeos.domain.model.ToolRequest;
import io.github.yuliangchen.lifeos.domain.model.ToolResult;
import io.github.yuliangchen.lifeos.tools.ToolModuleFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
/**
 * POI 发现服务，融合种子数据、技能搜索和抓取数据源。
 * POI discovery service combining seeded cards, skill search, and crawler sources.
 */
public class PoiDiscoveryService {

    private final ToolModuleFacade toolModuleFacade;
    private final ObjectMapper objectMapper;
    private final PoiCrawlerService poiCrawlerService;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${lifeos.poi.seed-enabled:true}")
    private boolean seedEnabled;

    @Value("${lifeos.poi.discovery-limit:10}")
    private int discoveryLimit;

    @Value("${lifeos.poi.crawler-enabled:false}")
    private boolean crawlerEnabled;

    @Value("${lifeos.poi.crawler-endpoint:}")
    private String crawlerEndpoint;

    /**
     * 构造 POI 发现服务。
     * Constructs POI discovery service.
     */
    public PoiDiscoveryService(ToolModuleFacade toolModuleFacade,
                              ObjectMapper objectMapper,
                              PoiCrawlerService poiCrawlerService) {
        this.toolModuleFacade = toolModuleFacade;
        this.objectMapper = objectMapper;
        this.poiCrawlerService = poiCrawlerService;
    }

    /**
     * 简化版 POI 发现入口。
     * Simplified discover entry with locale and query only.
     */
    public List<FestivalPoiCard> discover(String locale, String query) {
        return discover(locale, query, null, null, null);
    }

    /**
     * 完整版 POI 发现入口，支持多人、预算与时间窗参数。
     * Full discover entry supporting travelers, budget, and time-window context.
     */
    public List<FestivalPoiCard> discover(String locale,
                                          String query,
                                          String travelers,
                                          String budget,
                                          String timeWindow) {
        String resolvedLocale = normalizeLocale(locale);
        List<FestivalPoiCard> cards = new ArrayList<>();

        if (seedEnabled) {
            cards.addAll(seedCards(resolvedLocale));
        }

        cards.add(skillCard(resolvedLocale, query, travelers, budget, timeWindow));
        cards.addAll(poiCrawlerService.discoverCards(resolvedLocale, query, discoveryLimit));
        cards.addAll(crawlerCards(resolvedLocale, query));

        return deduplicateCards(cards).stream()
                .filter(card -> card != null && card.pois() != null && !card.pois().isEmpty())
                .sorted(Comparator.comparing(FestivalPoiCard::date))
                .limit(Math.max(1, discoveryLimit))
                .toList();
    }

    /**
     * 通过技能搜索生成一张实时推荐卡片。
     * Builds one live recommendation card from skill search output.
     */
    private FestivalPoiCard skillCard(String locale,
                                      String query,
                                      String travelers,
                                      String budget,
                                      String timeWindow) {
        String topic = StringUtils.hasText(query)
                ? query
                : locale.startsWith("zh")
                ? "近期节日可玩poi推荐，包含热门和低疲劳路线"
                : "Upcoming holiday playable POIs with low-fatigue itinerary tips";
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("topic", topic);
            parameters.put("locale", locale);
            if (StringUtils.hasText(travelers)) {
                parameters.put("travelers", travelers);
            }
            if (StringUtils.hasText(budget)) {
                parameters.put("budget", budget);
            }
            if (StringUtils.hasText(timeWindow)) {
                parameters.put("time_window", timeWindow);
            }
            ToolResult result = toolModuleFacade.execute(new ToolRequest("search", parameters));
            String summary = result.summary();
            List<String> pois = extractPois(summary, locale);
            if (pois.isEmpty()) {
                return null;
            }
            Map<String, String> metadata = result.metadata() == null ? Map.of() : result.metadata();
            String provider = metadata.getOrDefault("provider", "seeded-fallback");
            String source = sourceFromProvider(provider);
            String title = switch (source) {
                case "claw-skill" -> locale.startsWith("zh") ? "Claw 实时发现" : "Claw Live Discovery";
                case "flyai-skill" -> locale.startsWith("zh") ? "FlyAI 实时发现" : "FlyAI Live Discovery";
                default -> locale.startsWith("zh") ? "种子知识推荐" : "Seeded Knowledge Discovery";
            };
            String vibe = switch (source) {
                case "claw-skill" -> locale.startsWith("zh")
                        ? "Claw Skill 聚合 + 用户偏好融合"
                        : "Claw skill aggregation fused with user preferences";
                case "flyai-skill" -> locale.startsWith("zh")
                        ? "FlyAI 实时检索 + 用户偏好融合"
                        : "FlyAI live retrieval fused with user preferences";
                default -> locale.startsWith("zh")
                        ? "内置知识回退结果"
                        : "Seeded fallback knowledge result";
            };
            return new FestivalPoiCard(
                    source + "-" + UUID.randomUUID(),
                    LocalDate.now().plusDays(1).toString(),
                    title,
                    locale.startsWith("zh") ? "多城市" : "Multi-city",
                    pois,
                    vibe,
                    source,
                    "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?auto=format&fit=crop&w=1400&q=80",
                    "https://samplelib.com/lib/preview/mp4/sample-5s.mp4"
            );
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 从抓取端点读取并转换推荐卡片。
     * Reads crawler endpoint and converts payload into POI cards.
     */
    private List<FestivalPoiCard> crawlerCards(String locale, String query) {
        if (!crawlerEnabled || !StringUtils.hasText(crawlerEndpoint)) {
            return List.of();
        }
        try {
            String uri = crawlerEndpoint;
            if (StringUtils.hasText(query)) {
                uri = uri + (uri.contains("?") ? "&" : "?") + "q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return List.of();
            }
            JsonNode json = objectMapper.readTree(response.body());
            if (!json.isArray()) {
                return List.of();
            }
            List<FestivalPoiCard> cards = new ArrayList<>();
            for (JsonNode item : json) {
                String name = item.path("name").asText("");
                String city = item.path("city").asText(locale.startsWith("zh") ? "未知城市" : "Unknown city");
                String date = item.path("date").asText(LocalDate.now().plusDays(3).toString());
                List<String> pois = new ArrayList<>();
                JsonNode poiList = item.path("pois");
                if (poiList.isArray()) {
                    poiList.forEach(node -> {
                        if (node.isTextual()) {
                            pois.add(node.asText());
                        }
                    });
                }
                if (!StringUtils.hasText(name) || pois.isEmpty()) {
                    continue;
                }
                cards.add(new FestivalPoiCard(
                        "crawler-" + UUID.randomUUID(),
                        date,
                        name,
                        city,
                        pois,
                        item.path("vibe").asText(locale.startsWith("zh") ? "来源：聚合抓取" : "Source: aggregated crawling"),
                        "crawler",
                        item.path("imageUrl").asText(""),
                        item.path("videoUrl").asText("")
                ));
            }
            return cards;
        } catch (Exception ignore) {
            return List.of();
        }
    }

    /**
     * 返回内置种子卡片。
     * Returns built-in seeded POI cards by locale.
     */
    private List<FestivalPoiCard> seedCards(String locale) {
        if (locale.startsWith("zh")) {
            return List.of(
                    new FestivalPoiCard(
                            "seed-labor-day",
                            "2026-05-01",
                            "五一假期",
                            "上海",
                            List.of("武康路", "愚园路", "苏州河步道"),
                            "城市漫游 + 轻体力夜游",
                            "seeded",
                            "https://images.unsplash.com/photo-1549692520-acc6669e2f0c?auto=format&fit=crop&w=1400&q=80",
                            "https://samplelib.com/lib/preview/mp4/sample-5s.mp4"
                    ),
                    new FestivalPoiCard(
                            "seed-dragon-boat",
                            "2026-06-19",
                            "端午假期",
                            "杭州",
                            List.of("西湖环线", "河坊街", "良渚博物院"),
                            "湖景慢节奏 + 文化体验",
                            "seeded",
                            "https://images.unsplash.com/photo-1517309230475-6736d926b979?auto=format&fit=crop&w=1400&q=80",
                            "https://samplelib.com/lib/preview/mp4/sample-10s.mp4"
                    )
            );
        }
        return List.of(
                new FestivalPoiCard(
                        "seed-labor-day",
                        "2026-05-01",
                        "Labor Day Break",
                        "Shanghai",
                        List.of("Wukang Rd", "Yuyuan Rd", "Suzhou Creek Walk"),
                        "Urban stroll + low-fatigue evenings",
                        "seeded",
                        "https://images.unsplash.com/photo-1549692520-acc6669e2f0c?auto=format&fit=crop&w=1400&q=80",
                        "https://samplelib.com/lib/preview/mp4/sample-5s.mp4"
                ),
                new FestivalPoiCard(
                        "seed-dragon-boat",
                        "2026-06-19",
                        "Dragon Boat Holiday",
                        "Hangzhou",
                        List.of("West Lake Loop", "Hefang Street", "Liangzhu Museum"),
                        "Lake-side pace + culture",
                        "seeded",
                        "https://images.unsplash.com/photo-1517309230475-6736d926b979?auto=format&fit=crop&w=1400&q=80",
                        "https://samplelib.com/lib/preview/mp4/sample-10s.mp4"
                )
        );
    }

    /**
     * 从文本摘要中提取 POI 候选列表。
     * Extracts POI candidates from search summary text.
     */
    private List<String> extractPois(String summary, String locale) {
        if (!StringUtils.hasText(summary)) {
            return List.of();
        }
        String cleaned = summary.replace('\n', ' ')
                .replace('，', ',')
                .replace('。', ',')
                .replace('；', ',');
        List<String> candidates = java.util.Arrays.stream(cleaned.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .filter(text -> text.length() <= 24)
                .distinct()
                .limit(4)
                .toList();
        if (!candidates.isEmpty()) {
            return candidates;
        }
        return List.of(locale.startsWith("zh") ? "热门景点推荐" : "Trending POIs");
    }

    /**
     * 归一化 locale 到 zh-CN/en-US。
     * Normalizes locale to zh-CN or en-US.
     */
    private String normalizeLocale(String locale) {
        if (!StringUtils.hasText(locale)) {
            return "zh-CN";
        }
        String lowered = locale.toLowerCase(Locale.ROOT);
        if (lowered.startsWith("zh")) {
            return "zh-CN";
        }
        return "en-US";
    }

    /**
     * 将 provider 标识映射为来源标签。
     * Maps provider key into source label.
     */
    private String sourceFromProvider(String provider) {
        if ("claw-skill".equalsIgnoreCase(provider)) {
            return "claw-skill";
        }
        if ("flyai-mcp".equalsIgnoreCase(provider)) {
            return "flyai-skill";
        }
        return "seeded";
    }

    /**
     * 对候选卡片做去重，保留更高优先级来源。
     * Deduplicates candidate cards and keeps higher-priority source.
     */
    private List<FestivalPoiCard> deduplicateCards(List<FestivalPoiCard> cards) {
        Map<String, FestivalPoiCard> merged = new java.util.LinkedHashMap<>();
        for (FestivalPoiCard card : cards) {
            if (card == null) {
                continue;
            }
            String key = String.join("|",
                    safe(card.date()),
                    safe(card.name()),
                    safe(card.city()));
            FestivalPoiCard existing = merged.get(key);
            if (existing == null) {
                merged.put(key, card);
                continue;
            }
            if (priority(card.source()) > priority(existing.source())) {
                merged.put(key, card);
            }
        }
        return new ArrayList<>(merged.values());
    }

    /**
     * 卡片来源优先级，crawler/skill 高于 seeded。
     * Source priority for deduplication merge strategy.
     */
    private int priority(String source) {
        if (!StringUtils.hasText(source)) {
            return 0;
        }
        String normalized = source.toLowerCase(Locale.ROOT);
        if (normalized.startsWith("crawler-")) {
            return 3;
        }
        if (normalized.contains("skill")) {
            return 2;
        }
        if ("seeded".equals(normalized)) {
            return 1;
        }
        return 0;
    }

    /**
     * 空值安全转换为去重键文本。
     * Safely converts nullable value for dedupe key.
     */
    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
