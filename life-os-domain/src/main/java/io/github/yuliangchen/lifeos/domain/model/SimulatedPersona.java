package io.github.yuliangchen.lifeos.domain.model;

import java.util.Map;

public record SimulatedPersona(
        String id,
        String locale,
        String displayName,
        String userId,
        String threadId,
        String prompt,
        String description,
        String mbtiType,
        String temperament,
        String decisionLens,
        Map<String, String> preferences
) {
}
