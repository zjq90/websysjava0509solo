package com.appsys.finance.repository;

import com.appsys.finance.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    Optional<SalesOrder> findByOrderNo(String orderNo);
    
    List<SalesOrder> findByEmployeeId(Long employeeId);
    
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM SalesOrder s WHERE s.employee.id = :employeeId AND s.createTime BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalAmountByEmployeeAndDateRange(
        @Param("employeeId") Long employeeId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT COALESCE(SUM(s.paidAmount), 0) FROM SalesOrder s WHERE s.employee.id = :employeeId AND s.createTime BETWEEN :startDate AND :endDate")
    BigDecimal sumPaidAmountByEmployeeAndDateRange(
        @Param("employeeId") Long employeeId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT COALESCE(SUM(s.commissionAmount), 0) FROM SalesOrder s WHERE s.employee.id = :employeeId AND s.createTime BETWEEN :startDate AND :endDate")
    BigDecimal sumCommissionAmountByEmployeeAndDateRange(
        @Param("employeeId") Long employeeId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM SalesOrder s WHERE s.createTime BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalAmountByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
