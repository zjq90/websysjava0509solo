package com.pethospital.repository;

import com.pethospital.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问诊数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByUserIdOrderByCreateTimeDesc(Long userId);
}
