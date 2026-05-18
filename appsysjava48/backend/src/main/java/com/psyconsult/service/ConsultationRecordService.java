package com.psyconsult.service;

import com.psyconsult.entity.ConsultationRecord;
import com.psyconsult.entity.CrisisAlert;
import com.psyconsult.repository.ConsultationRecordRepository;
import com.psyconsult.repository.CrisisAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationRecordService {

    @Autowired
    private ConsultationRecordRepository recordRepository;

    @Autowired
    private CrisisAlertRepository crisisAlertRepository;

    @Autowired
    private SensitiveWordService sensitiveWordService;

    public List<ConsultationRecord> getRecordsByCounselor(Long counselorId) {
        return recordRepository.findByCounselorId(counselorId);
    }

    public List<ConsultationRecord> getRecordsByUser(Long userId) {
        return recordRepository.findByUserId(userId);
    }

    public List<ConsultationRecord> getRecordsByCounselorAndTag(Long counselorId, String tag) {
        return recordRepository.findByCounselorIdAndTagContaining(counselorId, tag);
    }

    @Transactional
    public ConsultationRecord createRecord(ConsultationRecord record) {
        ConsultationRecord savedRecord = recordRepository.save(record);
        
        checkAndCreateCrisisAlert(savedRecord);
        
        return savedRecord;
    }

    @Transactional
    public ConsultationRecord updateRecord(Long id, ConsultationRecord record) {
        Optional<ConsultationRecord> existingOpt = recordRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("咨询记录不存在");
        }
        ConsultationRecord existing = existingOpt.get();
        existing.setChiefComplaint(record.getChiefComplaint());
        existing.setInterventionMeasures(record.getInterventionMeasures());
        existing.setFollowUpSuggestions(record.getFollowUpSuggestions());
        existing.setConsultationSummary(record.getConsultationSummary());
        existing.setTags(record.getTags());
        existing.setMoodRating(record.getMoodRating());
        existing.setRiskLevel(record.getRiskLevel());
        existing.setNextAppointment(record.getNextAppointment());
        existing.setCounselorNotes(record.getCounselorNotes());
        existing.setStatus(record.getStatus());
        
        ConsultationRecord savedRecord = recordRepository.save(existing);
        
        checkAndCreateCrisisAlert(savedRecord);
        
        return savedRecord;
    }

    private void checkAndCreateCrisisAlert(ConsultationRecord record) {
        String content = record.getChiefComplaint() + " " + 
                         record.getConsultationSummary() + " " + 
                         record.getCounselorNotes();
        
        if (sensitiveWordService.containsCrisisWords(content)) {
            CrisisAlert alert = new CrisisAlert();
            alert.setRecordId(record.getId());
            alert.setUserId(record.getUserId());
            alert.setCounselorId(record.getCounselorId());
            alert.setTriggerWords(sensitiveWordService.extractTriggerWords(content));
            alert.setContent("咨询记录中检测到危机关键词：" + alert.getTriggerWords());
            alert.setAlertLevel(sensitiveWordService.determineAlertLevel(content));
            alert.setStatus("pending");
            crisisAlertRepository.save(alert);
        }
    }

    public Optional<ConsultationRecord> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    @Transactional
    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }
}
