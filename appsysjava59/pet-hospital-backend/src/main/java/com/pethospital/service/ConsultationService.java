package com.pethospital.service;

import com.pethospital.entity.Consultation;
import com.pethospital.entity.ConsultationMessage;
import com.pethospital.repository.ConsultationMessageRepository;
import com.pethospital.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private ConsultationMessageRepository consultationMessageRepository;

    public List<Consultation> getDoctorPendingConsultations(Long doctorId) {
        return consultationRepository.findDoctorConsultationsOrdered(
                doctorId,
                Arrays.asList("PENDING", "IN_PROGRESS")
        );
    }

    public List<Consultation> getDoctorConsultations(Long doctorId) {
        return consultationRepository.findByDoctorId(doctorId);
    }

    public Optional<Consultation> getConsultationById(Long id) {
        return consultationRepository.findById(id);
    }

    public List<ConsultationMessage> getConsultationMessages(Long consultationId) {
        return consultationMessageRepository.findByConsultationIdOrderByCreateTimeAsc(consultationId);
    }

    @Transactional
    public ConsultationMessage sendMessage(Long consultationId, String senderType, Long senderId, String content, String messageType) {
        ConsultationMessage message = new ConsultationMessage();
        message.setConsultationId(consultationId);
        message.setSenderType(senderType);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setIsRead(false);
        return consultationMessageRepository.save(message);
    }

    @Transactional
    public Consultation updateConsultationStatus(Long id, String status) {
        Optional<Consultation> consultationOpt = consultationRepository.findById(id);
        if (consultationOpt.isPresent()) {
            Consultation consultation = consultationOpt.get();
            consultation.setStatus(status);
            if ("IN_PROGRESS".equals(status) && consultation.getStartTime() == null) {
                consultation.setStartTime(LocalDateTime.now());
            }
            if ("COMPLETED".equals(status)) {
                consultation.setEndTime(LocalDateTime.now());
            }
            return consultationRepository.save(consultation);
        }
        return null;
    }

    @Transactional
    public Consultation createConsultation(Consultation consultation) {
        consultation.setStatus("PENDING");
        consultation.setCreateTime(LocalDateTime.now());
        return consultationRepository.save(consultation);
    }

    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }
}
