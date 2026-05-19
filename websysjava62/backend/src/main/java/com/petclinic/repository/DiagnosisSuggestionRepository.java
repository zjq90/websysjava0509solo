package com.petclinic.repository;

import com.petclinic.entity.DiagnosisSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AI诊断建议Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface DiagnosisSuggestionRepository extends JpaRepository<DiagnosisSuggestion, Long> {

    /**
     * 根据问诊ID查询建议
     */
    List<DiagnosisSuggestion> findByConsultationId(Long consultationId);

    /**
     * 根据问诊编号查询
     */
    List<DiagnosisSuggestion> findByConsultationNo(String consultationNo);

    /**
     * 根据状态查询
     */
    List<DiagnosisSuggestion> findByStatus(String status);
}
