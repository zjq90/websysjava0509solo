package com.hospital.repository;

import com.hospital.entity.DepositPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepositPaymentRepository extends JpaRepository<DepositPayment, Long> {
    List<DepositPayment> findByUserIdOrderByCreateTimeDesc(Long userId);
    Page<DepositPayment> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);
    List<DepositPayment> findByAdmissionIdOrderByCreateTimeDesc(Long admissionId);
    Optional<DepositPayment> findByOrderNo(String orderNo);
    
    @Query("SELECT COALESCE(SUM(dp.amount), 0) FROM DepositPayment dp WHERE dp.userId = :userId AND dp.status = 2")
    BigDecimal sumPaidAmountByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COALESCE(SUM(dp.amount), 0) FROM DepositPayment dp WHERE dp.admissionId = :admissionId AND dp.status = 2")
    BigDecimal sumPaidAmountByAdmissionId(@Param("admissionId") Long admissionId);
}
