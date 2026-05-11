package com.medical.appointment.repository;

import com.medical.appointment.entity.SymptomKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomKnowledgeRepository extends JpaRepository<SymptomKnowledge, Long> {
    
    List<SymptomKnowledge> findByIsActiveTrue();
    
    List<SymptomKnowledge> findByDepartment_IdAndIsActiveTrue(Long departmentId);
    
    @Query("SELECT s FROM SymptomKnowledge s WHERE s.isActive = true AND (s.symptom LIKE %:keyword% OR s.synonyms LIKE %:keyword%) ORDER BY s.matchScore DESC")
    List<SymptomKnowledge> searchByKeyword(String keyword);
    
    @Query("SELECT DISTINCT s FROM SymptomKnowledge s WHERE s.isActive = true")
    List<SymptomKnowledge> findAllActive();
}
