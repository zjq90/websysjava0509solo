package com.hospital.repository;

import com.hospital.entity.FeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 费用记录Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long> {

    FeeRecord findByFeeNo(String feeNo);

    List<FeeRecord> findByHospitalizationId(Long hospitalizationId);

    List<FeeRecord> findByPatientId(Long patientId);

    List<FeeRecord> findByPaymentStatus(String paymentStatus);

    List<FeeRecord> findByFeeType(String feeType);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FeeRecord f WHERE f.hospitalizationId = ?1")
    BigDecimal sumAmountByHospitalizationId(Long hospitalizationId);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FeeRecord f WHERE f.hospitalizationId = ?1 AND f.paymentStatus = '已缴费'")
    BigDecimal sumPaidAmountByHospitalizationId(Long hospitalizationId);
}
