package com.petclinic.repository;

import com.petclinic.entity.ConsultationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问诊记录Repository
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Repository
public interface ConsultationRecordRepository extends JpaRepository<ConsultationRecord, Long> {

    /**
     * 根据问诊状态查询
     */
    List<ConsultationRecord> findByStatus(String status);

    /**
     * 根据宠物名称查询
     */
    List<ConsultationRecord> findByPetNameContaining(String petName);

    /**
     * 根据宠主姓名查询
     */
    List<ConsultationRecord> findByOwnerNameContaining(String ownerName);

    /**
     * 根据医生姓名查询
     */
    List<ConsultationRecord> findByDoctorNameContaining(String doctorName);

    /**
     * 根据问诊编号查询
     */
    ConsultationRecord findByConsultationNo(String consultationNo);

    /**
     * 查询未理赔的记录
     */
    List<ConsultationRecord> findByClaimed(Boolean claimed);
}
