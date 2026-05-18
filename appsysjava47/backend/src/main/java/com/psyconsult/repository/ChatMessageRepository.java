package com.psyconsult.repository;

import com.psyconsult.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConsultationIdOrderByCreateTimeAsc(Long consultationId);
    List<ChatMessage> findByConsultationIdAndIsReadFalse(Long consultationId);
}
