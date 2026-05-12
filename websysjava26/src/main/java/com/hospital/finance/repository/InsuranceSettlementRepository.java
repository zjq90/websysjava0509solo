package com.hospital.finance.repository;

import com.hospital.finance.entity.InsuranceSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 医保结算数据访问层
 */
@Repository
public interface InsuranceSettlementRepository extends JpaRepository<InsuranceSettlement, Long> {

    /**
     * 根据结算单号查询
     */
    InsuranceSettlement findBySettlementNo(String settlementNo);

    /**
     * 根据患者ID查询
     */
    List<InsuranceSettlement> findByPatientId(Long patientId);

    /**
     * 根据收费记录ID查询
     */
    InsuranceSettlement findByChargeId(Long chargeId);

    /**
     * 根据状态查询
     */
    List<InsuranceSettlement> findByStatus(String status);

    /**
     * 查询指定时间范围内的结算记录
     */
    List<InsuranceSettlement> findBySettlementTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}