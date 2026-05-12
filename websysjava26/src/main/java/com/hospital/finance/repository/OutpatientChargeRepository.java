package com.hospital.finance.repository;

import com.hospital.finance.entity.OutpatientCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 门诊收费数据访问层
 */
@Repository
public interface OutpatientChargeRepository extends JpaRepository<OutpatientCharge, Long> {

    /**
     * 根据收费单号查询
     */
    OutpatientCharge findByChargeNo(String chargeNo);

    /**
     * 根据患者ID查询
     */
    List<OutpatientCharge> findByPatientId(Long patientId);

    /**
     * 根据状态查询
     */
    List<OutpatientCharge> findByStatus(String status);

    /**
     * 查询指定时间范围内的收费记录
     */
    List<OutpatientCharge> findByChargeTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定时间范围内的收费金额
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OutpatientCharge o WHERE o.chargeTime BETWEEN ?1 AND ?2")
    java.math.BigDecimal sumAmountByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
}