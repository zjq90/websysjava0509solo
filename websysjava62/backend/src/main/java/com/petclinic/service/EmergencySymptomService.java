package com.petclinic.service;

import com.petclinic.entity.EmergencySymptom;
import com.petclinic.repository.EmergencySymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 紧急症状库Service
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Service
public class EmergencySymptomService {

    @Autowired
    private EmergencySymptomRepository emergencySymptomRepository;

    /**
     * 查询所有紧急症状
     */
    public List<EmergencySymptom> findAll() {
        return emergencySymptomRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public Optional<EmergencySymptom> findById(Long id) {
        return emergencySymptomRepository.findById(id);
    }

    /**
     * 根据状态查询
     */
    public List<EmergencySymptom> findByStatus(String status) {
        return emergencySymptomRepository.findByStatus(status);
    }

    /**
     * 根据严重程度查询
     */
    public List<EmergencySymptom> findBySeverityLevel(String severityLevel) {
        return emergencySymptomRepository.findBySeverityLevel(severityLevel);
    }

    /**
     * 新增紧急症状
     */
    public EmergencySymptom save(EmergencySymptom symptom) {
        return emergencySymptomRepository.save(symptom);
    }

    /**
     * 更新紧急症状
     */
    public EmergencySymptom update(Long id, EmergencySymptom symptom) {
        Optional<EmergencySymptom> optional = emergencySymptomRepository.findById(id);
        if (optional.isPresent()) {
            EmergencySymptom existing = optional.get();
            existing.setSymptomName(symptom.getSymptomName());
            existing.setDescription(symptom.getDescription());
            existing.setEmergencyMeasure(symptom.getEmergencyMeasure());
            existing.setSeverityLevel(symptom.getSeverityLevel());
            existing.setPetType(symptom.getPetType());
            existing.setRevisedBy(symptom.getRevisedBy());
            existing.setRevisedTime(LocalDateTime.now());
            existing.setStatus(symptom.getStatus());
            return emergencySymptomRepository.save(existing);
        }
        return null;
    }

    /**
     * 删除紧急症状
     */
    public void deleteById(Long id) {
        emergencySymptomRepository.deleteById(id);
    }

    /**
     * 根据症状名称模糊查询
     */
    public List<EmergencySymptom> searchBySymptomName(String symptomName) {
        return emergencySymptomRepository.findBySymptomNameContaining(symptomName);
    }
}
