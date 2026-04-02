package io.github.yuliangchen.lifeos.infra;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.repository.KnowledgeDocumentRepository;
import io.github.yuliangchen.lifeos.infra.persistence.JsonValueCodec;
import io.github.yuliangchen.lifeos.infra.persistence.entity.KnowledgeDocumentEntity;
import io.github.yuliangchen.lifeos.infra.persistence.repository.SpringDataKnowledgeDocumentEntityRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
@ConditionalOnProperty(name = "lifeos.persistence.mode", havingValue = "database", matchIfMissing = true)
public class DatabaseKnowledgeDocumentRepository implements KnowledgeDocumentRepository {

    public static final String GLOBAL_USER_ID = "system-seed";

    private final SpringDataKnowledgeDocumentEntityRepository repository;
    private final JsonValueCodec jsonValueCodec;

    public DatabaseKnowledgeDocumentRepository(SpringDataKnowledgeDocumentEntityRepository repository, JsonValueCodec jsonValueCodec) {
        this.repository = repository;
        this.jsonValueCodec = jsonValueCodec;
    }

    @Override
    public KnowledgeDocument save(KnowledgeDocument document) {
        return toDomain(repository.save(toEntity(document)));
    }

    @Override
    public List<KnowledgeDocument> findAll() {
        return repository.findAllByOrderByUpdatedAtDesc().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<KnowledgeDocument> findAllForUser(String userId) {
        return repository.findAllByOrderByUpdatedAtDesc().stream()
                .filter(entity -> {
                    String owner = normalizeUserId(entity.getUserId());
                    return GLOBAL_USER_ID.equals(owner) || owner.equals(userId);
                })
                .map(this::toDomain)
                .toList();
    }

    private KnowledgeDocumentEntity toEntity(KnowledgeDocument document) {
        KnowledgeDocumentEntity entity = new KnowledgeDocumentEntity();
        entity.setId(document.id());
        entity.setUserId(document.userId());
        entity.setTitle(document.title());
        entity.setSourceType(document.sourceType());
        entity.setTagsJson(jsonValueCodec.write(document.tags()));
        entity.setSummary(document.summary());
        entity.setContent(document.content());
        entity.setLocale(document.locale());
        entity.setUpdatedAt(document.updatedAt());
        return entity;
    }

    private KnowledgeDocument toDomain(KnowledgeDocumentEntity entity) {
        return new KnowledgeDocument(
                entity.getId(),
                normalizeUserId(entity.getUserId()),
                entity.getTitle(),
                entity.getSourceType(),
                jsonValueCodec.read(entity.getTagsJson(), new TypeReference<List<String>>() {
                }, List.of()),
                entity.getSummary(),
                entity.getContent(),
                entity.getLocale(),
                entity.getUpdatedAt()
        );
    }

    private String normalizeUserId(String userId) {
        return userId == null || userId.isBlank() ? GLOBAL_USER_ID : userId;
    }
}
