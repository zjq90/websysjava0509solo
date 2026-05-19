package com.pethospital.repository;

import com.pethospital.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 病历记录数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByPetIdOrderByVisitTimeDesc(Long petId);

    List<MedicalRecord> findByUserIdOrderByCreateTimeDesc(Long userId);
}
