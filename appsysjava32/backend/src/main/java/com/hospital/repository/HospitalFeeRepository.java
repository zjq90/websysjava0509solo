package com.hospital.repository;

import com.hospital.entity.HospitalFee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HospitalFeeRepository extends JpaRepository<HospitalFee, Long>, JpaSpecificationExecutor<HospitalFee> {
    List<HospitalFee> findByUserIdOrderByFeeDateDesc(Long userId);
    Page<HospitalFee> findByUserIdOrderByFeeDateDesc(Long userId, Pageable pageable);
    List<HospitalFee> findByAdmissionIdOrderByFeeDateDesc(Long admissionId);
    List<HospitalFee> findByUserIdAndFeeDate(Long userId, LocalDate feeDate);
    
    @Query("SELECT COALESCE(SUM(hf.amount), 0) FROM HospitalFee hf WHERE hf.userId = :userId")
    BigDecimal sumTotalAmountByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COALESCE(SUM(hf.amount), 0) FROM HospitalFee hf WHERE hf.admissionId = :admissionId")
    BigDecimal sumTotalAmountByAdmissionId(@Param("admissionId") Long admissionId);
}
