package com.petclinic.service;

import com.petclinic.entity.ConsultationRecord;
import com.petclinic.entity.DiagnosisSuggestion;
import com.petclinic.entity.InsuranceClaim;
import com.petclinic.repository.ConsultationRecordRepository;
import com.petclinic.repository.DiagnosisSuggestionRepository;
import com.petclinic.repository.InsuranceClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 问诊管理Service
 * 包含问诊记录、AI诊断建议、保险理赔等功能
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Service
public class ConsultationService {

    @Autowired
    private ConsultationRecordRepository consultationRecordRepository;

    @Autowired
    private DiagnosisSuggestionRepository diagnosisSuggestionRepository;

    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;

    // ==================== 问诊记录管理 ====================

    public List<ConsultationRecord> findAllConsultations() {
        return consultationRecordRepository.findAll();
    }

    public Optional<ConsultationRecord> findConsultationById(Long id) {
        return consultationRecordRepository.findById(id);
    }

    public ConsultationRecord saveConsultation(ConsultationRecord record) {
        if (record.getConsultationNo() == null) {
            record.setConsultationNo(generateConsultationNo());
        }
        return consultationRecordRepository.save(record);
    }

    public ConsultationRecord updateConsultation(Long id, ConsultationRecord record) {
        Optional<ConsultationRecord> optional = consultationRecordRepository.findById(id);
        if (optional.isPresent()) {
            ConsultationRecord existing = optional.get();
            existing.setPetName(record.getPetName());
            existing.setPetType(record.getPetType());
            existing.setPetAge(record.getPetAge());
            existing.setOwnerName(record.getOwnerName());
            existing.setOwnerPhone(record.getOwnerPhone());
            existing.setChiefComplaint(record.getChiefComplaint());
            existing.setSymptomDetail(record.getSymptomDetail());
            existing.setDiagnosisResult(record.getDiagnosisResult());
            existing.setTreatmentPlan(record.getTreatmentPlan());
            existing.setDoctorAdvice(record.getDoctorAdvice());
            existing.setDoctorName(record.getDoctorName());
            existing.setConsultationFee(record.getConsultationFee());
            existing.setStatus(record.getStatus());
            return consultationRecordRepository.save(existing);
        }
        return null;
    }

    public void deleteConsultationById(Long id) {
        consultationRecordRepository.deleteById(id);
    }

    public List<ConsultationRecord> findConsultationsByStatus(String status) {
        return consultationRecordRepository.findByStatus(status);
    }

    public List<ConsultationRecord> searchConsultationsByPetName(String petName) {
        return consultationRecordRepository.findByPetNameContaining(petName);
    }

    private String generateConsultationNo() {
        return "CZ" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    // ==================== AI诊断建议管理 ====================

    public List<DiagnosisSuggestion> findAllSuggestions() {
        return diagnosisSuggestionRepository.findAll();
    }

    public Optional<DiagnosisSuggestion> findSuggestionById(Long id) {
        return diagnosisSuggestionRepository.findById(id);
    }

    public List<DiagnosisSuggestion> findSuggestionsByConsultationId(Long consultationId) {
        return diagnosisSuggestionRepository.findByConsultationId(consultationId);
    }

    public DiagnosisSuggestion generateAIDiagnosis(Long consultationId) {
        Optional<ConsultationRecord> consultation = consultationRecordRepository.findById(consultationId);
        if (!consultation.isPresent()) {
            return null;
        }

        ConsultationRecord record = consultation.get();
        DiagnosisSuggestion suggestion = new DiagnosisSuggestion();
        suggestion.setConsultationId(consultationId);
        suggestion.setConsultationNo(record.getConsultationNo());
        suggestion.setAiModelVersion("PetClinicAI-v1.0");

        String symptom = record.getSymptomDetail() != null ? record.getSymptomDetail() : record.getChiefComplaint();
        if (symptom != null && symptom.contains("发烧")) {
            suggestion.setPossibleDiseases("[\"感冒\", \"病毒感染\", \"炎症\"]");
            suggestion.setSuggestion("建议测量体温，如超过39.5度请及时就医，可适当补充水分，观察精神状态。");
            suggestion.setRecommendedTests("血常规、C反应蛋白");
            suggestion.setRecommendedTreatment("退烧药、消炎药");
            suggestion.setConfidence(new BigDecimal("85.50"));
        } else if (symptom != null && symptom.contains("呕吐")) {
            suggestion.setPossibleDiseases("[\"肠胃炎\", \"食物中毒\", \"寄生虫感染\"]");
            suggestion.setSuggestion("建议禁食12小时，观察呕吐情况，如持续呕吐或伴有血便请及时就医。");
            suggestion.setRecommendedTests("粪便检查、血常规");
            suggestion.setRecommendedTreatment("止吐药、益生菌");
            suggestion.setConfidence(new BigDecimal("82.00"));
        } else {
            suggestion.setPossibleDiseases("[\"常规检查\", \"亚健康状态\"]");
            suggestion.setSuggestion("建议定期体检，注意观察宠物饮食和精神状态，保持良好的生活习惯。");
            suggestion.setRecommendedTests("常规体检");
            suggestion.setRecommendedTreatment("保健建议");
            suggestion.setConfidence(new BigDecimal("70.00"));
        }

        suggestion.setRiskWarning("AI诊断仅供参考，最终诊断以专业兽医为准。");
        suggestion.setStatus("GENERATED");

        return diagnosisSuggestionRepository.save(suggestion);
    }

    public DiagnosisSuggestion reviewSuggestion(Long id, String status, String reviewedBy) {
        Optional<DiagnosisSuggestion> optional = diagnosisSuggestionRepository.findById(id);
        if (optional.isPresent()) {
            DiagnosisSuggestion suggestion = optional.get();
            suggestion.setStatus(status);
            suggestion.setReviewedBy(reviewedBy);
            suggestion.setReviewedTime(LocalDateTime.now());
            return diagnosisSuggestionRepository.save(suggestion);
        }
        return null;
    }

    // ==================== 保险理赔管理 ====================

    public List<InsuranceClaim> findAllClaims() {
        return insuranceClaimRepository.findAll();
    }

    public Optional<InsuranceClaim> findClaimById(Long id) {
        return insuranceClaimRepository.findById(id);
    }

    public InsuranceClaim createClaim(InsuranceClaim claim) {
        claim.setClaimNo(generateClaimNo());
        if (claim.getTotalFee() != null) {
            BigDecimal claimRate = new BigDecimal("0.70");
            claim.setClaimAmount(claim.getTotalFee().multiply(claimRate));
            claim.setSelfPayAmount(claim.getTotalFee().subtract(claim.getClaimAmount()));
        }
        return insuranceClaimRepository.save(claim);
    }

    public InsuranceClaim updateClaimStatus(Long id, String status, String reviewedBy, String rejectReason) {
        Optional<InsuranceClaim> optional = insuranceClaimRepository.findById(id);
        if (optional.isPresent()) {
            InsuranceClaim claim = optional.get();
            claim.setStatus(status);
            claim.setReviewedBy(reviewedBy);
            claim.setReviewedTime(LocalDateTime.now());
            if ("REJECTED".equals(status)) {
                claim.setRejectReason(rejectReason);
            }
            if ("COMPLETED".equals(status)) {
                claim.setCompletedTime(LocalDateTime.now());
            }
            return insuranceClaimRepository.save(claim);
        }
        return null;
    }

    public List<InsuranceClaim> findClaimsByStatus(String status) {
        return insuranceClaimRepository.findByStatus(status);
    }

    private String generateClaimNo() {
        return "LP" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
