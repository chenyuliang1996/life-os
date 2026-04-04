package io.github.yuliangchen.lifeos.domain.model;

import java.util.List;

public record FestivalPoiCard(
        String id,
        String date,
        String name,
        String city,
        List<String> pois,
        String vibe,
        String source,
        String imageUrl,
        String videoUrl
) {
}
