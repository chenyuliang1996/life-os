package io.github.yuliangchen.lifeos.infra;

import io.agentscope.core.embedding.EmbeddingModel;
import io.agentscope.core.embedding.ollama.OllamaTextEmbedding;
import io.agentscope.core.embedding.openai.OpenAITextEmbedding;
import io.agentscope.core.message.TextBlock;
import io.agentscope.core.rag.model.Document;
import io.agentscope.core.rag.model.DocumentMetadata;
import io.agentscope.core.rag.store.PgVectorStore;
import io.agentscope.core.rag.store.dto.SearchDocumentDto;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeDocument;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeQuery;
import io.github.yuliangchen.lifeos.domain.model.KnowledgeSnippet;
import io.github.yuliangchen.lifeos.domain.model.RagRuntimeStatus;
import io.github.yuliangchen.lifeos.domain.service.VectorKnowledgeStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgentScopePgVectorKnowledgeStore implements VectorKnowledgeStore {

    private static final Logger log = LoggerFactory.getLogger(AgentScopePgVectorKnowledgeStore.class);
    private static final String GLOBAL_USER_ID = "system-seed";

    private final RagRuntimeStatus status;
    private final EmbeddingModel embeddingModel;
    private final PgVectorStore pgVectorStore;
    private final VectorRagProperties properties;

    public AgentScopePgVectorKnowledgeStore(VectorRagProperties properties,
                                            org.springframework.core.env.Environment environment) {
        this.properties = properties;

        String jdbcUrl = environment.getProperty("spring.datasource.url", "");
        String username = environment.getProperty("spring.datasource.username", "");
        String password = environment.getProperty("spring.datasource.password", "");

        EmbeddingModel resolvedEmbeddingModel = null;
        PgVectorStore resolvedPgVectorStore = null;
        RagRuntimeStatus resolvedStatus;

        if (!jdbcUrl.startsWith("jdbc:postgresql:")) {
            resolvedStatus = new RagRuntimeStatus(
                    true,
                    false,
                    "text-plus-vector-fallback",
                    "pgvector",
                    properties.provider(),
                    properties.modelName(),
                    properties.dimensions(),
                    "Vector retrieval is enabled in config but inactive because the datasource is not PostgreSQL."
            );
        } else {
            EmbeddingModel model = createEmbeddingModel(properties);
            if (model == null) {
                resolvedStatus = new RagRuntimeStatus(
                        true,
                        false,
                        "text-plus-vector-fallback",
                        "pgvector",
                        properties.provider(),
                        properties.modelName(),
                        properties.dimensions(),
                        "Vector retrieval is enabled but no embedding provider credentials are available."
                );
            } else {
                try {
                    resolvedEmbeddingModel = model;
                    resolvedPgVectorStore = PgVectorStore.builder()
                            .jdbcUrl(jdbcUrl)
                            .username(username)
                            .password(password)
                            .schema(properties.schema())
                            .tableName(properties.tableName())
                            .dimensions(properties.dimensions())
                            .distanceType(PgVectorStore.DistanceType.valueOf(properties.distanceType().toUpperCase()))
                            .connectionTimeoutMs(properties.connectionTimeoutMs())
                            .build();
                    resolvedStatus = new RagRuntimeStatus(
                            true,
                            true,
                            "hybrid-text-plus-vector",
                            "pgvector",
                            properties.provider(),
                            model.getModelName(),
                            model.getDimensions(),
                            "AgentScope PgVectorStore is active and documents will be indexed for semantic retrieval."
                    );
                } catch (Exception exception) {
                    log.warn("Failed to initialize pgvector retrieval. Falling back to text mode.", exception);
                    resolvedEmbeddingModel = null;
                    resolvedPgVectorStore = null;
                    resolvedStatus = new RagRuntimeStatus(
                            true,
                            false,
                            "text-plus-vector-fallback",
                            "pgvector",
                            properties.provider(),
                            properties.modelName(),
                            properties.dimensions(),
                            "PgVector initialization failed, so the app keeps using text retrieval."
                    );
                }
            }
        }

        this.embeddingModel = resolvedEmbeddingModel;
        this.pgVectorStore = resolvedPgVectorStore;
        this.status = resolvedStatus;
    }

    @Override
    public RagRuntimeStatus status() {
        return status;
    }

    @Override
    public void upsert(KnowledgeDocument document) {
        if (!status.vectorReady() || embeddingModel == null || pgVectorStore == null) {
            return;
        }

        try {
            pgVectorStore.delete(document.id()).onErrorResume(throwable -> reactor.core.publisher.Mono.just(false)).block();
            List<Document> indexedDocuments = toIndexedDocuments(document);
            if (!indexedDocuments.isEmpty()) {
                pgVectorStore.add(indexedDocuments).block();
            }
        } catch (Exception exception) {
            log.warn("Failed to upsert document {} into pgvector index.", document.id(), exception);
        }
    }

    @Override
    public List<KnowledgeSnippet> search(KnowledgeQuery query, int limit) {
        if (!status.vectorReady() || embeddingModel == null || pgVectorStore == null) {
            return List.of();
        }

        try {
            TextBlock textBlock = TextBlock.builder().text(query.query()).build();
            double[] vector = embeddingModel.embed(textBlock).block();
            SearchDocumentDto dto = SearchDocumentDto.builder()
                    .queryEmbedding(vector)
                    .limit(limit)
                    .scoreThreshold(properties.scoreThreshold())
                    .build();

            return pgVectorStore.search(dto)
                    .blockOptional()
                    .orElse(List.of())
                    .stream()
                    .filter(document -> isVisibleToUser(document.getMetadata().getPayloadValue("userId"), query.userId()))
                    .map(document -> new KnowledgeSnippet(
                            document.getMetadata().getDocId(),
                            String.valueOf(document.getMetadata().getPayloadValue("title")),
                            document.getMetadata().getContentText()
                    ))
                    .toList();
        } catch (Exception exception) {
            log.warn("Failed to search pgvector store. Falling back to text retrieval.", exception);
            return List.of();
        }
    }

    private List<Document> toIndexedDocuments(KnowledgeDocument document) {
        List<String> chunks = chunkDocument(document);
        List<Document> indexedDocuments = new ArrayList<>(chunks.size());

        for (int index = 0; index < chunks.size(); index++) {
            String chunk = chunks.get(index);
            TextBlock textBlock = TextBlock.builder()
                    .text(chunk)
                    .build();
            DocumentMetadata metadata = DocumentMetadata.builder()
                    .docId(document.id())
                    .chunkId(document.id() + "#chunk-" + index)
                    .content(textBlock)
                    .payload(Map.of(
                            "title", document.title(),
                            "summary", document.summary(),
                            "locale", document.locale(),
                            "userId", document.userId(),
                            "sourceType", document.sourceType(),
                            "tags", String.join(",", document.tags()),
                            "chunkIndex", index
                    ))
                    .build();
            Document indexedDocument = new Document(metadata);
            indexedDocument.setEmbedding(embeddingModel.embed(textBlock).block());
            indexedDocuments.add(indexedDocument);
        }

        return indexedDocuments;
    }

    private List<String> chunkDocument(KnowledgeDocument document) {
        // Chunking keeps retrieval granular without introducing a second pipeline / 分块让召回更细，同时不必额外引入另一套离线切片流水线。
        String combined = (document.summary() + "\n\n" + document.content()).trim();
        if (!StringUtils.hasText(combined)) {
            return List.of();
        }

        List<String> chunks = new ArrayList<>();
        String normalized = combined.replace("\r\n", "\n");
        String[] paragraphs = normalized.split("\\n\\s*\\n");
        StringBuilder current = new StringBuilder();

        for (String rawParagraph : paragraphs) {
            String paragraph = rawParagraph.trim();
            if (!StringUtils.hasText(paragraph)) {
                continue;
            }

            if (paragraph.length() > properties.chunkSize()) {
                flushCurrent(chunks, current);
                chunks.addAll(splitLongParagraph(paragraph));
                continue;
            }

            if (current.length() == 0) {
                current.append(paragraph);
                continue;
            }

            if (current.length() + 2 + paragraph.length() <= properties.chunkSize()) {
                current.append("\n\n").append(paragraph);
            } else {
                flushCurrent(chunks, current);
                current.append(paragraph);
            }
        }

        flushCurrent(chunks, current);

        if (chunks.isEmpty()) {
            chunks.add(combined.substring(0, Math.min(combined.length(), properties.chunkSize())));
        }
        if (chunks.size() > properties.maxChunks()) {
            return chunks.subList(0, properties.maxChunks());
        }
        return List.copyOf(chunks);
    }

    private List<String> splitLongParagraph(String paragraph) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < paragraph.length() && chunks.size() < properties.maxChunks()) {
            int end = Math.min(paragraph.length(), start + properties.chunkSize());
            chunks.add(paragraph.substring(start, end));
            if (end == paragraph.length()) {
                break;
            }
            start = Math.max(end - properties.chunkOverlap(), start + 1);
        }
        return chunks;
    }

    private void flushCurrent(List<String> chunks, StringBuilder current) {
        if (current.length() == 0 || chunks.size() >= properties.maxChunks()) {
            current.setLength(0);
            return;
        }
        chunks.add(current.toString());
        current.setLength(0);
    }

    private boolean isVisibleToUser(Object payloadUserId, String queryUserId) {
        if (payloadUserId == null) {
            return true;
        }
        String owner = String.valueOf(payloadUserId);
        return GLOBAL_USER_ID.equals(owner) || owner.equals(queryUserId);
    }

    private EmbeddingModel createEmbeddingModel(VectorRagProperties properties) {
        if ("openai".equalsIgnoreCase(properties.provider()) && StringUtils.hasText(properties.openaiApiKey())) {
            OpenAITextEmbedding.Builder builder = OpenAITextEmbedding.builder()
                    .apiKey(properties.openaiApiKey())
                    .modelName(properties.modelName())
                    .dimensions(properties.dimensions());
            if (StringUtils.hasText(properties.openaiBaseUrl())) {
                builder.baseUrl(properties.openaiBaseUrl());
            }
            return builder.build();
        }

        if ("ollama".equalsIgnoreCase(properties.provider())
                && StringUtils.hasText(properties.ollamaBaseUrl())
                && StringUtils.hasText(properties.modelName())) {
            return OllamaTextEmbedding.builder()
                    .baseUrl(properties.ollamaBaseUrl())
                    .modelName(properties.modelName())
                    .dimensions(properties.dimensions())
                    .build();
        }

        return null;
    }

    @ConfigurationProperties(prefix = "lifeos.rag.vector")
    public record VectorRagProperties(
            String provider,
            String modelName,
            int dimensions,
            String schema,
            String tableName,
            String distanceType,
            long connectionTimeoutMs,
            Double scoreThreshold,
            int chunkSize,
            int chunkOverlap,
            int maxChunks,
            String openaiApiKey,
            String openaiBaseUrl,
            String ollamaBaseUrl
    ) {

        public VectorRagProperties {
            if (provider == null || provider.isBlank()) {
                provider = "openai";
            }
            if (modelName == null || modelName.isBlank()) {
                modelName = "text-embedding-3-small";
            }
            if (dimensions <= 0) {
                dimensions = 1536;
            }
            if (schema == null || schema.isBlank()) {
                schema = "public";
            }
            if (tableName == null || tableName.isBlank()) {
                tableName = "life_os_knowledge_vectors";
            }
            if (distanceType == null || distanceType.isBlank()) {
                distanceType = "cosine";
            }
            if (connectionTimeoutMs <= 0) {
                connectionTimeoutMs = 10000L;
            }
            if (scoreThreshold == null) {
                scoreThreshold = 0.2d;
            }
            if (chunkSize <= 0) {
                chunkSize = 700;
            }
            if (chunkOverlap < 0) {
                chunkOverlap = 120;
            }
            if (maxChunks <= 0) {
                maxChunks = 8;
            }
            if (openaiApiKey == null || openaiApiKey.isBlank()) {
                openaiApiKey = System.getenv("OPENAI_API_KEY");
            }
            if (openaiBaseUrl == null) {
                openaiBaseUrl = System.getenv("OPENAI_BASE_URL");
            }
            if (ollamaBaseUrl == null || ollamaBaseUrl.isBlank()) {
                ollamaBaseUrl = System.getenv("OLLAMA_BASE_URL");
            }
        }
    }
}
