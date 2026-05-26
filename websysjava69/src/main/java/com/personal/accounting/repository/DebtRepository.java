package com.personal.accounting.repository;

import com.personal.accounting.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByUserId(Long userId);
    List<Debt> findByUserIdAndIsPaidOff(Long userId, Boolean isPaidOff);
    
    @Query("SELECT COALESCE(SUM(d.remainingAmount), 0) FROM Debt d WHERE d.userId = :userId AND d.isPaidOff = false")
    BigDecimal getTotalRemainingDebtByUserId(Long userId);
    
    List<Debt> findByNextPaymentDateBetweenAndIsPaidOffFalse(LocalDate startDate, LocalDate endDate);
    
    List<Debt> findByUserIdInAndIsPaidOffFalse(List<Long> userIds);
}
