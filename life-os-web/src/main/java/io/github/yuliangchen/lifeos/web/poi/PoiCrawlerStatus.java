package io.github.yuliangchen.lifeos.web.poi;

import java.time.Instant;
import java.util.Map;

/**
 * POI 爬虫运行状态响应模型。
 * Response model describing POI crawler runtime status.
 */
public record PoiCrawlerStatus(
        boolean enabled,
        boolean scheduleEnabled,
        String scheduleCron,
        Instant lastRunAt,
        Instant lastSuccessAt,
        String lastError,
        int retentionDays,
        long storedTotal,
        Map<String, Long> storedBySource,
        Map<String, Integer> lastRunFetched
) {
}
