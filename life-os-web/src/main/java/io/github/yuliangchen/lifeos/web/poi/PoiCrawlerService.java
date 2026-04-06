package io.github.yuliangchen.lifeos.web.poi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yuliangchen.lifeos.domain.model.FestivalPoiCard;
import io.github.yuliangchen.lifeos.infra.persistence.entity.PoiCrawlerSnapshotEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataPoiCrawlerSnapshotEntityRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@EnableConfigurationProperties(PoiCrawlerService.PoiCrawlerProperties.class)
/**
 * POI 爬虫服务，负责定时抓取、快照落库与查询融合。
 * POI crawler service for scheduled ingestion, persistence and discovery merge.
 */
public class PoiCrawlerService {

    private static final int MAX_SCAN_SIZE = 500;
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {
    };

    private final SpringDataPoiCrawlerSnapshotEntityRepository repository;
    private final ObjectMapper objectMapper;
    private final PoiCrawlerProperties properties;
    private final HttpClient httpClient;
    private final AtomicReference<Instant> lastRunAt = new AtomicReference<>();
    private final AtomicReference<Instant> lastSuccessAt = new AtomicReference<>();
    private final AtomicReference<String> lastError = new AtomicReference<>();
    private final AtomicReference<Map<String, Integer>> lastRunFetched = new AtomicReference<>(Map.of());

    /**
     * 构造 POI 爬虫服务。
     * Constructs POI crawler service.
     */
    public PoiCrawlerService(SpringDataPoiCrawlerSnapshotEntityRepository repository,
                             ObjectMapper objectMapper,
                             PoiCrawlerProperties properties) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(Math.max(3, properties.requestTimeoutSeconds())))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Scheduled(cron = "${lifeos.poi.crawler.schedule-cron:0 */30 * * * *}")
    /**
     * 定时抓取任务入口。
     * Scheduled crawler entry point.
     */
    public void scheduledCrawl() {
        if (!properties.enabled() || !properties.scheduleEnabled()) {
            return;
        }
        runCrawlerNow();
    }

    /**
     * 手动触发抓取任务。
     * Manually triggers crawler ingestion.
     */
    public synchronized PoiCrawlerStatus runCrawlerNow() {
        Instant startedAt = Instant.now();
        lastRunAt.set(startedAt);

        if (!properties.enabled()) {
            lastRunFetched.set(Map.of());
            lastError.set(null);
            return status();
        }

        Map<String, Integer> fetchedBySource = new LinkedHashMap<>();
        List<String> failures = new ArrayList<>();
        int successSources = 0;

        for (SourceTarget sourceTarget : configuredSources()) {
            String sourceName = sourceTarget.sourceName();
            try {
                List<PoiCrawlerSnapshotEntity> snapshots = fetchFromSource(sourceTarget);
                fetchedBySource.put(sourceName, snapshots.size());
                if (!snapshots.isEmpty()) {
                    repository.saveAll(snapshots);
                }
                successSources++;
            } catch (Exception exception) {
                fetchedBySource.put(sourceName, 0);
                failures.add(sourceName + ": " + compactError(exception));
            }
        }

        cleanupExpiredSnapshots();
        lastRunFetched.set(Map.copyOf(fetchedBySource));

        if (successSources > 0) {
            lastSuccessAt.set(Instant.now());
        }
        if (failures.isEmpty()) {
            lastError.set(null);
        } else {
            lastError.set(String.join(" | ", failures));
        }
        return status();
    }

    /**
     * 从已落库快照中构建 POI 卡片。
     * Builds festival cards from stored crawler snapshots.
     */
    public List<FestivalPoiCard> discoverCards(String locale, String query, int limit) {
        String resolvedLocale = normalizeLocale(locale);
        int safeLimit = Math.max(1, Math.min(limit, 40));
        int scanLimit = Math.max(safeLimit * 10, MAX_SCAN_SIZE);

        List<PoiCrawlerSnapshotEntity> snapshots = repository.findByLocaleOrderByCrawledAtDesc(
                resolvedLocale,
                PageRequest.of(0, scanLimit)
        );
        if (snapshots.isEmpty() && !"zh-CN".equals(resolvedLocale)) {
            snapshots = repository.findByOrderByCrawledAtDesc(PageRequest.of(0, scanLimit));
        }

        Map<String, FestivalPoiCard> deduplicated = new LinkedHashMap<>();
        for (PoiCrawlerSnapshotEntity snapshot : snapshots) {
            if (!matchesQuery(snapshot, query)) {
                continue;
            }
            String key = snapshot.getTravelDate() + "|" + snapshot.getTitle() + "|" + snapshot.getCity() + "|" + snapshot.getSource();
            deduplicated.putIfAbsent(key, toFestivalCard(snapshot));
            if (deduplicated.size() >= safeLimit * 2) {
                break;
            }
        }
        return deduplicated.values().stream()
                .sorted(Comparator.comparing(FestivalPoiCard::date))
                .limit(safeLimit)
                .toList();
    }

    /**
     * 返回爬虫运行状态。
     * Returns crawler runtime status.
     */
    public PoiCrawlerStatus status() {
        Map<String, Long> storedBySource = new LinkedHashMap<>();
        storedBySource.put("xiaohongshu", repository.countBySource("xiaohongshu"));
        storedBySource.put("mafengwo", repository.countBySource("mafengwo"));
        return new PoiCrawlerStatus(
                properties.enabled(),
                properties.scheduleEnabled(),
                properties.scheduleCron(),
                lastRunAt.get(),
                lastSuccessAt.get(),
                lastError.get(),
                properties.retentionDays(),
                repository.count(),
                Map.copyOf(storedBySource),
                lastRunFetched.get()
        );
    }

    /**
     * 读取并解析单个来源数据。
     * Fetches and parses one source endpoint response.
     */
    private List<PoiCrawlerSnapshotEntity> fetchFromSource(SourceTarget sourceTarget) throws Exception {
        PoiCrawlerProperties.SourceProperties sourceProperties = sourceTarget.properties();
        String endpoint = sourceProperties.endpoint();
        if (!StringUtils.hasText(endpoint)) {
            return List.of();
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(Math.max(3, properties.requestTimeoutSeconds())))
                .header("Accept", "application/json")
                .header("User-Agent", "life-os-poi-crawler/1.0")
                .GET();
        if (StringUtils.hasText(sourceProperties.authHeaderName()) && StringUtils.hasText(sourceProperties.authHeaderValue())) {
            builder.header(sourceProperties.authHeaderName().trim(), sourceProperties.authHeaderValue().trim());
        }

        HttpResponse<String> response = httpClient.send(
                builder.build(),
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("HTTP " + response.statusCode());
        }
        return parsePayload(sourceTarget, response.body());
    }

    /**
     * 将来源 JSON 规范化为快照实体列表。
     * Normalizes source JSON payload into snapshot entities.
     */
    private List<PoiCrawlerSnapshotEntity> parsePayload(SourceTarget sourceTarget, String payload) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(payload);
        JsonNode listNode = resolveListNode(root);
        if (listNode == null || !listNode.isArray()) {
            return List.of();
        }

        String locale = normalizeLocale(sourceTarget.properties().locale());
        int maxItems = Math.max(1, properties.maxItemsPerSource());
        List<PoiCrawlerSnapshotEntity> snapshots = new ArrayList<>();
        int index = 0;
        for (JsonNode item : listNode) {
            if (index >= maxItems) {
                break;
            }
            String title = firstNonBlank(
                    text(item, "name"),
                    text(item, "title"),
                    text(item, "festival"),
                    text(item, "topic")
            );
            String city = firstNonBlank(
                    text(item, "city"),
                    text(item, "destination"),
                    text(item, "location")
            );
            if (!StringUtils.hasText(title)) {
                continue;
            }
            List<String> pois = extractPois(item);
            if (pois.isEmpty()) {
                continue;
            }

            LocalDate travelDate = parseDate(firstNonBlank(
                    text(item, "date"),
                    text(item, "travelDate"),
                    text(item, "festivalDate")
            ));
            String vibe = firstNonBlank(
                    text(item, "vibe"),
                    text(item, "summary"),
                    text(item, "note")
            );
            String imageUrl = firstNonBlank(
                    text(item, "imageUrl"),
                    text(item, "cover"),
                    text(item, "coverUrl")
            );
            String videoUrl = firstNonBlank(
                    text(item, "videoUrl"),
                    text(item, "video"),
                    text(item, "mediaUrl")
            );
            String externalUrl = firstNonBlank(
                    text(item, "externalUrl"),
                    text(item, "link"),
                    text(item, "url"),
                    text(item, "postUrl")
            );

            PoiCrawlerSnapshotEntity entity = new PoiCrawlerSnapshotEntity();
            entity.setId(snapshotId(sourceTarget.sourceName(), locale, travelDate, title, city, pois));
            entity.setCrawledAt(Instant.now());
            entity.setTravelDate(travelDate);
            entity.setSource(sourceTarget.sourceName());
            entity.setLocale(locale);
            entity.setTitle(trimToLength(title, 160));
            entity.setCity(trimToLength(defaultIfBlank(city, locale.startsWith("zh") ? "未知城市" : "Unknown city"), 128));
            entity.setVibe(trimToLength(defaultIfBlank(vibe, locale.startsWith("zh") ? "来源：公开内容抓取" : "Source: public crawler feed"), 480));
            entity.setImageUrl(trimToLength(defaultIfBlank(imageUrl, ""), 1024));
            entity.setVideoUrl(trimToLength(defaultIfBlank(videoUrl, ""), 1024));
            entity.setExternalUrl(trimToLength(defaultIfBlank(externalUrl, ""), 1024));
            entity.setPoisJson(objectMapper.writeValueAsString(pois));
            snapshots.add(entity);
            index++;
        }
        return snapshots;
    }

    /**
     * 返回当前已配置并启用的来源列表。
     * Returns currently configured enabled crawler sources.
     */
    private List<SourceTarget> configuredSources() {
        List<SourceTarget> sources = new ArrayList<>();
        if (properties.xiaohongshu().enabled() && StringUtils.hasText(properties.xiaohongshu().endpoint())) {
            sources.add(new SourceTarget("xiaohongshu", properties.xiaohongshu()));
        }
        if (properties.mafengwo().enabled() && StringUtils.hasText(properties.mafengwo().endpoint())) {
            sources.add(new SourceTarget("mafengwo", properties.mafengwo()));
        }
        return sources;
    }

    /**
     * 从响应树中提取列表节点。
     * Resolves list node from common JSON response shapes.
     */
    private JsonNode resolveListNode(JsonNode root) {
        if (root == null || root.isNull()) {
            return null;
        }
        if (root.isArray()) {
            return root;
        }
        if (!root.isObject()) {
            return null;
        }
        for (String field : List.of("items", "list", "results")) {
            JsonNode candidate = root.path(field);
            if (candidate.isArray()) {
                return candidate;
            }
        }
        JsonNode data = root.path("data");
        if (data.isArray()) {
            return data;
        }
        if (data.isObject()) {
            for (String field : List.of("items", "list", "results")) {
                JsonNode candidate = data.path(field);
                if (candidate.isArray()) {
                    return candidate;
                }
            }
        }
        return null;
    }

    /**
     * 提取 POI 文本列表。
     * Extracts POI labels from known payload fields.
     */
    private List<String> extractPois(JsonNode item) {
        for (String field : List.of("pois", "poiList", "spots", "highlights", "points")) {
            List<String> parsed = parsePoiNode(item.path(field));
            if (!parsed.isEmpty()) {
                return parsed;
            }
        }
        List<String> fromText = splitPoiText(firstNonBlank(
                text(item, "pois"),
                text(item, "poiText"),
                text(item, "highlightsText")
        ));
        return fromText.stream()
                .map(entry -> trimToLength(entry, 40))
                .distinct()
                .limit(5)
                .toList();
    }

    /**
     * 解析单个 POI 节点值。
     * Parses one POI field node into labels.
     */
    private List<String> parsePoiNode(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            return List.of();
        }
        if (node.isArray()) {
            List<String> pois = new ArrayList<>();
            node.forEach(item -> {
                if (item.isTextual()) {
                    pois.add(item.asText());
                } else if (item.isObject()) {
                    String candidate = firstNonBlank(
                            text(item, "name"),
                            text(item, "title"),
                            text(item, "poi")
                    );
                    if (StringUtils.hasText(candidate)) {
                        pois.add(candidate);
                    }
                }
            });
            return pois.stream()
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .distinct()
                    .limit(5)
                    .toList();
        }
        if (node.isTextual()) {
            return splitPoiText(node.asText()).stream()
                    .distinct()
                    .limit(5)
                    .toList();
        }
        return List.of();
    }

    /**
     * 按常见分隔符拆分 POI 文本。
     * Splits POI text by common delimiters.
     */
    private List<String> splitPoiText(String raw) {
        if (!StringUtils.hasText(raw)) {
            return List.of();
        }
        return java.util.Arrays.stream(raw
                        .replace('，', ',')
                        .replace('。', ',')
                        .replace('；', ',')
                        .replace('、', ',')
                        .split("[,|/]"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(item -> trimToLength(item, 40))
                .toList();
    }

    /**
     * 清理超过保留周期的历史数据。
     * Cleans snapshots older than retention window.
     */
    private void cleanupExpiredSnapshots() {
        Instant retentionEdge = Instant.now().minus(Math.max(1, properties.retentionDays()), ChronoUnit.DAYS);
        repository.deleteByCrawledAtBefore(retentionEdge);
    }

    /**
     * 判断快照是否命中查询词。
     * Returns whether snapshot matches user query.
     */
    private boolean matchesQuery(PoiCrawlerSnapshotEntity snapshot, String query) {
        if (!StringUtils.hasText(query)) {
            return true;
        }
        String normalizedQuery = query.trim().toLowerCase(Locale.ROOT);
        String searchable = (defaultIfBlank(snapshot.getTitle(), "")
                + " " + defaultIfBlank(snapshot.getCity(), "")
                + " " + defaultIfBlank(snapshot.getVibe(), "")
                + " " + defaultIfBlank(snapshot.getSource(), "")
                + " " + String.join(" ", readPois(snapshot.getPoisJson())))
                .toLowerCase(Locale.ROOT);
        return searchable.contains(normalizedQuery);
    }

    /**
     * 快照转前端卡片模型。
     * Converts snapshot entity into festival card model.
     */
    private FestivalPoiCard toFestivalCard(PoiCrawlerSnapshotEntity snapshot) {
        String source = "crawler-" + snapshot.getSource();
        return new FestivalPoiCard(
                source + "-" + snapshot.getId(),
                snapshot.getTravelDate() == null ? LocalDate.now().plusDays(2).toString() : snapshot.getTravelDate().toString(),
                snapshot.getTitle(),
                snapshot.getCity(),
                readPois(snapshot.getPoisJson()),
                snapshot.getVibe(),
                source,
                defaultIfBlank(snapshot.getImageUrl(), ""),
                defaultIfBlank(snapshot.getVideoUrl(), "")
        );
    }

    /**
     * 读取持久化的 POI JSON。
     * Reads persisted POI JSON array.
     */
    private List<String> readPois(String poisJson) {
        if (!StringUtils.hasText(poisJson)) {
            return List.of();
        }
        try {
            return objectMapper.readValue(poisJson, STRING_LIST_TYPE).stream()
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .distinct()
                    .limit(5)
                    .toList();
        } catch (JsonProcessingException exception) {
            return List.of();
        }
    }

    /**
     * 解析来源中的日期字符串。
     * Parses source date text into local date.
     */
    private LocalDate parseDate(String rawDate) {
        if (StringUtils.hasText(rawDate)) {
            try {
                return LocalDate.parse(rawDate.trim());
            } catch (Exception ignore) {
                // Keep fallback for non-ISO date text.
            }
        }
        return LocalDate.now().plusDays(2);
    }

    /**
     * 构建稳定快照 ID，避免重复累积。
     * Builds deterministic snapshot id to avoid duplicate accumulation.
     */
    private String snapshotId(String source, String locale, LocalDate travelDate, String title, String city, List<String> pois) {
        String payload = source + "|" + locale + "|" + travelDate + "|" + title + "|" + city + "|" + String.join(";", pois);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return source + "-" + HexFormat.of().formatHex(hash, 0, 12);
        } catch (NoSuchAlgorithmException exception) {
            String encoded = URLEncoder.encode(payload, StandardCharsets.UTF_8);
            return source + "-" + encoded.substring(0, Math.min(60, encoded.length()));
        }
    }

    /**
     * 读取文本字段并去除空白。
     * Reads a text field and trims whitespace.
     */
    private String text(JsonNode node, String field) {
        return trimToLength(node.path(field).asText(""), 1024).trim();
    }

    /**
     * 取第一个非空字符串。
     * Returns first non-blank value.
     */
    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }
        return "";
    }

    /**
     * 将 locale 统一为 zh-CN 或 en-US。
     * Normalizes locale to zh-CN or en-US.
     */
    private String normalizeLocale(String locale) {
        if (!StringUtils.hasText(locale)) {
            return "zh-CN";
        }
        return locale.toLowerCase(Locale.ROOT).startsWith("zh") ? "zh-CN" : "en-US";
    }

    /**
     * 空字符串兜底替换。
     * Applies fallback when value is blank.
     */
    private String defaultIfBlank(String value, String fallback) {
        return StringUtils.hasText(value) ? value : fallback;
    }

    /**
     * 截断文本以适配数据库字段长度。
     * Trims text to fit database column length.
     */
    private String trimToLength(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (trimmed.length() <= maxLength) {
            return trimmed;
        }
        return trimmed.substring(0, maxLength);
    }

    /**
     * 压缩异常文本，避免状态输出过长。
     * Compacts exception text for status display.
     */
    private String compactError(Exception exception) {
        String message = exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage();
        return trimToLength(message, 180);
    }

    private record SourceTarget(String sourceName, PoiCrawlerProperties.SourceProperties properties) {
    }

    @ConfigurationProperties(prefix = "lifeos.poi.crawler")
    /**
     * POI 爬虫配置项。
     * Configuration properties for POI crawler runtime.
     */
    public record PoiCrawlerProperties(
            boolean enabled,
            boolean scheduleEnabled,
            String scheduleCron,
            int retentionDays,
            int maxItemsPerSource,
            int requestTimeoutSeconds,
            SourceProperties xiaohongshu,
            SourceProperties mafengwo
    ) {

        /**
         * 应用默认配置值。
         * Applies default values for crawler properties.
         */
        public PoiCrawlerProperties {
            if (scheduleCron == null || scheduleCron.isBlank()) {
                scheduleCron = "0 */30 * * * *";
            }
            if (retentionDays <= 0) {
                retentionDays = 14;
            }
            if (maxItemsPerSource <= 0) {
                maxItemsPerSource = 30;
            }
            if (requestTimeoutSeconds <= 0) {
                requestTimeoutSeconds = 12;
            }
            if (xiaohongshu == null) {
                xiaohongshu = new SourceProperties(false, "", "", "", "zh-CN");
            }
            if (mafengwo == null) {
                mafengwo = new SourceProperties(false, "", "", "", "zh-CN");
            }
        }

        /**
         * 单来源连接配置。
         * Per-source connection properties.
         */
        public record SourceProperties(
                boolean enabled,
                String endpoint,
                String authHeaderName,
                String authHeaderValue,
                String locale
        ) {

            /**
             * 应用单来源默认值。
             * Applies defaults for one source endpoint.
             */
            public SourceProperties {
                if (endpoint == null) {
                    endpoint = "";
                }
                if (authHeaderName == null) {
                    authHeaderName = "";
                }
                if (authHeaderValue == null) {
                    authHeaderValue = "";
                }
                if (locale == null || locale.isBlank()) {
                    locale = "zh-CN";
                }
            }
        }
    }
}
