package io.github.yuliangchen.lifeos.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "poi_crawler_snapshots", indexes = {
        @Index(name = "idx_poi_crawler_locale_crawled", columnList = "locale, crawled_at"),
        @Index(name = "idx_poi_crawler_source_crawled", columnList = "source, crawled_at"),
        @Index(name = "idx_poi_crawler_date_crawled", columnList = "travel_date, crawled_at")
})
/**
 * POI 抓取快照实体，记录三方来源同步结果。
 * Persistence entity storing normalized POI crawler snapshots.
 */
public class PoiCrawlerSnapshotEntity {

    @Id
    @Column(nullable = false, length = 96)
    private String id;

    @Column(name = "crawled_at", nullable = false)
    private Instant crawledAt;

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @Column(nullable = false, length = 24)
    private String source;

    @Column(nullable = false, length = 16)
    private String locale;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 128)
    private String city;

    @Column(nullable = false, length = 480)
    private String vibe;

    @Column(name = "external_url", length = 1024)
    private String externalUrl;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    @Column(name = "video_url", length = 1024)
    private String videoUrl;

    @Lob
    @Column(name = "pois_json", nullable = false, columnDefinition = "TEXT")
    private String poisJson;

    /**
     * 返回快照主键。
     * Returns snapshot primary id.
     */
    public String getId() {
        return id;
    }

    /**
     * 设置快照主键。
     * Sets snapshot primary id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 返回抓取时间。
     * Returns crawler ingestion timestamp.
     */
    public Instant getCrawledAt() {
        return crawledAt;
    }

    /**
     * 设置抓取时间。
     * Sets crawler ingestion timestamp.
     */
    public void setCrawledAt(Instant crawledAt) {
        this.crawledAt = crawledAt;
    }

    /**
     * 返回行程日期。
     * Returns normalized travel date.
     */
    public LocalDate getTravelDate() {
        return travelDate;
    }

    /**
     * 设置行程日期。
     * Sets normalized travel date.
     */
    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    /**
     * 返回来源标识。
     * Returns crawler source name.
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置来源标识。
     * Sets crawler source name.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 返回 locale。
     * Returns locale code.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * 设置 locale。
     * Sets locale code.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * 返回标题。
     * Returns normalized item title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题。
     * Sets normalized item title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 返回城市。
     * Returns destination city.
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市。
     * Sets destination city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 返回行程氛围描述。
     * Returns vibe/summary text.
     */
    public String getVibe() {
        return vibe;
    }

    /**
     * 设置行程氛围描述。
     * Sets vibe/summary text.
     */
    public void setVibe(String vibe) {
        this.vibe = vibe;
    }

    /**
     * 返回外部链接。
     * Returns source external url.
     */
    public String getExternalUrl() {
        return externalUrl;
    }

    /**
     * 设置外部链接。
     * Sets source external url.
     */
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    /**
     * 返回封面图链接。
     * Returns image url.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置封面图链接。
     * Sets image url.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 返回视频链接。
     * Returns video url.
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * 设置视频链接。
     * Sets video url.
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * 返回 POI 列表 JSON。
     * Returns POI list JSON payload.
     */
    public String getPoisJson() {
        return poisJson;
    }

    /**
     * 设置 POI 列表 JSON。
     * Sets POI list JSON payload.
     */
    public void setPoisJson(String poisJson) {
        this.poisJson = poisJson;
    }
}
