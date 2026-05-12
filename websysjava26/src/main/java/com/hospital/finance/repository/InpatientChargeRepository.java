package com.hospital.finance.repository;

import com.hospital.finance.entity.InpatientCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 住院收费数据访问层
 */
@Repository
public interface InpatientChargeRepository extends JpaRepository<InpatientCharge, Long> {

    /**
     * 根据收费单号查询
     */
    InpatientCharge findByChargeNo(String chargeNo);

    /**
     * 根据患者ID查询
     */
    List<InpatientCharge> findByPatientId(Long patientId);

    /**
     * 根据住院号查询
     */
    InpatientCharge findByAdmissionNo(String admissionNo);

    /**
     * 根据状态查询
     */
    List<InpatientCharge> findByStatus(String status);

    /**
     * 查询指定时间范围内的收费记录
     */
    List<InpatientCharge> findByChargeTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定时间范围内的收费金额
     */
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM InpatientCharge i WHERE i.chargeTime BETWEEN ?1 AND ?2")
    java.math.BigDecimal sumAmountByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
}