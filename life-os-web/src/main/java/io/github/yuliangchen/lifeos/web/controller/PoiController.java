package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.FestivalPoiCard;
import io.github.yuliangchen.lifeos.web.poi.PoiCrawlerService;
import io.github.yuliangchen.lifeos.web.poi.PoiCrawlerStatus;
import io.github.yuliangchen.lifeos.web.poi.PoiDiscoveryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/poi")
/**
 * POI 控制器，提供节日场景推荐点位。
 * POI controller exposing festival-oriented destination cards.
 */
public class PoiController {

    private final PoiDiscoveryService poiDiscoveryService;
    private final PoiCrawlerService poiCrawlerService;

    /**
     * 构造 POI 控制器。
     * Constructs POI controller.
     */
    public PoiController(PoiDiscoveryService poiDiscoveryService,
                         PoiCrawlerService poiCrawlerService) {
        this.poiDiscoveryService = poiDiscoveryService;
        this.poiCrawlerService = poiCrawlerService;
    }

    @GetMapping("/festivals")
    /**
     * 获取节日 POI 推荐卡片。
     * Returns festival POI recommendation cards.
     */
    public List<FestivalPoiCard> festivals(@RequestParam(defaultValue = "zh-CN") String locale,
                                           @RequestParam(required = false) String query,
                                           @RequestParam(required = false) String travelers,
                                           @RequestParam(required = false) String budget,
                                           @RequestParam(required = false) String timeWindow) {
        return poiDiscoveryService.discover(locale, query, travelers, budget, timeWindow);
    }

    @GetMapping("/crawler/status")
    /**
     * 返回 POI 爬虫状态。
     * Returns POI crawler status snapshot.
     */
    public PoiCrawlerStatus crawlerStatus() {
        return poiCrawlerService.status();
    }

    @PostMapping("/crawler/run")
    /**
     * 手动触发一次 POI 抓取任务。
     * Triggers one manual POI crawler run.
     */
    public PoiCrawlerStatus runCrawler() {
        return poiCrawlerService.runCrawlerNow();
    }
}
