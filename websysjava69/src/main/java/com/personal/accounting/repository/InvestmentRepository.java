package com.personal.accounting.repository;

import com.personal.accounting.entity.Investment;
import com.personal.accounting.enums.InvestmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByUserId(Long userId);
    List<Investment> findByUserIdAndInvestmentType(Long userId, InvestmentType investmentType);
    
    @Query("SELECT COALESCE(SUM(i.marketValue), 0) FROM Investment i WHERE i.userId = :userId")
    BigDecimal getTotalInvestmentValueByUserId(Long userId);
    
    @Query("SELECT COALESCE(SUM(i.profitLoss), 0) FROM Investment i WHERE i.userId = :userId")
    BigDecimal getTotalInvestmentProfitByUserId(Long userId);
    
    List<Investment> findByUserIdIn(List<Long> userIds);
}
