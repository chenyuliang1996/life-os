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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class PoiDiscoveryService {

    private final ToolModuleFacade toolModuleFacade;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${lifeos.poi.seed-enabled:true}")
    private boolean seedEnabled;

    @Value("${lifeos.poi.crawler-enabled:false}")
    private boolean crawlerEnabled;

    @Value("${lifeos.poi.crawler-endpoint:}")
    private String crawlerEndpoint;

    public PoiDiscoveryService(ToolModuleFacade toolModuleFacade, ObjectMapper objectMapper) {
        this.toolModuleFacade = toolModuleFacade;
        this.objectMapper = objectMapper;
    }

    public List<FestivalPoiCard> discover(String locale, String query) {
        String resolvedLocale = normalizeLocale(locale);
        List<FestivalPoiCard> cards = new ArrayList<>();

        if (seedEnabled) {
            cards.addAll(seedCards(resolvedLocale));
        }

        cards.add(skillCard(resolvedLocale, query));
        cards.addAll(crawlerCards(resolvedLocale, query));

        return cards.stream()
                .filter(card -> card != null && card.pois() != null && !card.pois().isEmpty())
                .sorted(Comparator.comparing(FestivalPoiCard::date))
                .limit(10)
                .toList();
    }

    private FestivalPoiCard skillCard(String locale, String query) {
        String topic = StringUtils.hasText(query)
                ? query
                : locale.startsWith("zh")
                ? "近期节日可玩poi推荐，包含热门和低疲劳路线"
                : "Upcoming holiday playable POIs with low-fatigue itinerary tips";
        try {
            ToolResult result = toolModuleFacade.execute(new ToolRequest("search", java.util.Map.of(
                    "topic", topic,
                    "locale", locale
            )));
            String summary = result.summary();
            List<String> pois = extractPois(summary, locale);
            if (pois.isEmpty()) {
                return null;
            }
            return new FestivalPoiCard(
                    "skill-flyai-" + UUID.randomUUID(),
                    LocalDate.now().plusDays(1).toString(),
                    locale.startsWith("zh") ? "FlyAI 实时发现" : "FlyAI Live Discovery",
                    locale.startsWith("zh") ? "多城市" : "Multi-city",
                    pois,
                    locale.startsWith("zh") ? "实时检索 + 用户偏好融合" : "Live retrieval fused with user preferences",
                    "skill",
                    "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?auto=format&fit=crop&w=1400&q=80",
                    "https://samplelib.com/lib/preview/mp4/sample-5s.mp4"
            );
        } catch (Exception ignore) {
            return null;
        }
    }

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
}
