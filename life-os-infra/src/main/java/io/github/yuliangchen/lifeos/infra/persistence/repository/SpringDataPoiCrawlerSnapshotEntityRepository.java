package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.PoiCrawlerSnapshotEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/**
 * POI 抓取快照的 Spring Data 仓储。
 * Spring Data repository for {@link PoiCrawlerSnapshotEntity}.
 */
public interface SpringDataPoiCrawlerSnapshotEntityRepository extends JpaRepository<PoiCrawlerSnapshotEntity, String> {

    /**
     * 按 locale 查询最新抓取快照。
     * Finds latest crawler snapshots by locale.
     */
    List<PoiCrawlerSnapshotEntity> findByLocaleOrderByCrawledAtDesc(String locale, Pageable pageable);

    /**
     * 查询全局最新抓取快照。
     * Finds latest crawler snapshots globally.
     */
    List<PoiCrawlerSnapshotEntity> findByOrderByCrawledAtDesc(Pageable pageable);

    /**
     * 统计某来源记录总量。
     * Counts stored records for a given source.
     */
    long countBySource(String source);

    /**
     * 删除超过保留期的数据。
     * Deletes crawler snapshots older than threshold.
     */
    long deleteByCrawledAtBefore(Instant instant);
}
