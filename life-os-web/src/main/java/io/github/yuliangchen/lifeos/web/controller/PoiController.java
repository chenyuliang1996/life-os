package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.FestivalPoiCard;
import io.github.yuliangchen.lifeos.web.poi.PoiDiscoveryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/poi")
public class PoiController {

    private final PoiDiscoveryService poiDiscoveryService;

    public PoiController(PoiDiscoveryService poiDiscoveryService) {
        this.poiDiscoveryService = poiDiscoveryService;
    }

    @GetMapping("/festivals")
    public List<FestivalPoiCard> festivals(@RequestParam(defaultValue = "zh-CN") String locale,
                                           @RequestParam(required = false) String query,
                                           @RequestParam(required = false) String travelers,
                                           @RequestParam(required = false) String budget,
                                           @RequestParam(required = false) String timeWindow) {
        return poiDiscoveryService.discover(locale, query, travelers, budget, timeWindow);
    }
}
