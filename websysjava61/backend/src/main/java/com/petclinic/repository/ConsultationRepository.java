package com.petclinic.repository;

import com.petclinic.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 问诊记录Repository
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    /**
     * 根据宠物主人ID查询问诊记录
     */
    List<Consultation> findByPetOwnerIdOrderByCreateTimeDesc(Long petOwnerId);

    /**
     * 根据医生ID查询问诊记录
     */
    List<Consultation> findByDoctorIdOrderByCreateTimeDesc(Long doctorId);

    /**
     * 根据时间范围查询问诊记录
     */
    List<Consultation> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计今日接诊数量
     */
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.createTime >= :startTime AND c.createTime < :endTime")
    Long countByDateRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计今日急诊数量
     */
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.isEmergency = 1 AND c.createTime >= :startTime AND c.createTime < :endTime")
    Long countEmergencyByDateRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计今日收入
     */
    @Query("SELECT COALESCE(SUM(c.fee), 0) FROM Consultation c WHERE c.status = 2 AND c.createTime >= :startTime AND c.createTime < :endTime")
    Double sumFeeByDateRange(LocalDateTime startTime, LocalDateTime endTime);
}
