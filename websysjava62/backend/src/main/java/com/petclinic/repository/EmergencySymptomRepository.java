package com.petclinic.repository;

import com.petclinic.entity.EmergencySymptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 紧急症状库Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface EmergencySymptomRepository extends JpaRepository<EmergencySymptom, Long> {

    /**
     * 根据状态查询紧急症状列表
     */
    List<EmergencySymptom> findByStatus(String status);

    /**
     * 根据严重程度查询
     */
    List<EmergencySymptom> findBySeverityLevel(String severityLevel);

    /**
     * 根据宠物类型查询
     */
    List<EmergencySymptom> findByPetTypeContaining(String petType);

    /**
     * 根据症状名称模糊查询
     */
    List<EmergencySymptom> findBySymptomNameContaining(String symptomName);
}
