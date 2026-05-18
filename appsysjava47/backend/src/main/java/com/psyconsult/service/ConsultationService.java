package com.psyconsult.service;

import com.psyconsult.entity.ChatMessage;
import com.psyconsult.entity.Consultation;
import com.psyconsult.entity.EmotionRecord;
import com.psyconsult.repository.ChatMessageRepository;
import com.psyconsult.repository.ConsultationRepository;
import com.psyconsult.repository.EmotionRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final EmotionRecordRepository emotionRecordRepository;

    public ConsultationService(ConsultationRepository consultationRepository,
                               ChatMessageRepository chatMessageRepository,
                               EmotionRecordRepository emotionRecordRepository) {
        this.consultationRepository = consultationRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.emotionRecordRepository = emotionRecordRepository;
    }

    @Transactional
    public Consultation createConsultation(Long userId, Long counselorId, String type, String topic) {
        Consultation consultation = new Consultation();
        consultation.setUserId(userId);
        consultation.setCounselorId(counselorId);
        consultation.setType(type);
        consultation.setTopic(topic);
        consultation.setStatus("IN_PROGRESS");
        consultation.setStartTime(LocalDateTime.now());

        return consultationRepository.save(consultation);
    }

    public List<Consultation> getUserConsultations(Long userId) {
        return consultationRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("咨询记录不存在"));
    }

    @Transactional
    public ChatMessage sendMessage(Long consultationId, Long senderId, String senderType, 
                                   String messageType, String content, String mediaUrl) {
        ChatMessage message = new ChatMessage();
        message.setConsultationId(consultationId);
        message.setSenderId(senderId);
        message.setSenderType(senderType);
        message.setType(messageType);
        message.setContent(content);
        message.setMediaUrl(mediaUrl);
        message.setIsRead(false);

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatMessages(Long consultationId) {
        return chatMessageRepository.findByConsultationIdOrderByCreateTimeAsc(consultationId);
    }

    @Transactional
    public Consultation endConsultation(Long consultationId, String notes, Integer emotionScore, String keywords) {
        Consultation consultation = getConsultationById(consultationId);
        consultation.setStatus("COMPLETED");
        consultation.setEndTime(LocalDateTime.now());
        consultation.setNotes(notes);
        consultation.setEmotionScore(emotionScore);
        consultation.setKeywords(keywords);

        if (emotionScore != null) {
            EmotionRecord emotionRecord = new EmotionRecord();
            emotionRecord.setUserId(consultation.getUserId());
            emotionRecord.setConsultationId(consultationId);
            emotionRecord.setScore(emotionScore);
            emotionRecord.setKeywords(keywords);
            emotionRecordRepository.save(emotionRecord);
        }

        return consultationRepository.save(consultation);
    }

    public List<EmotionRecord> getEmotionTrend(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return emotionRecordRepository.findByUserIdAndCreateTimeBetweenOrderByCreateTimeAsc(userId, startDate, endDate);
    }
}
