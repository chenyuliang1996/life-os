package io.github.yuliangchen.lifeos.infra.persistence.repository;

import io.github.yuliangchen.lifeos.infra.persistence.entity.KnowledgeDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataKnowledgeDocumentEntityRepository extends JpaRepository<KnowledgeDocumentEntity, String> {

    List<KnowledgeDocumentEntity> findAllByOrderByUpdatedAtDesc();

    List<KnowledgeDocumentEntity> findByUserIdInOrderByUpdatedAtDesc(List<String> userIds);
}
