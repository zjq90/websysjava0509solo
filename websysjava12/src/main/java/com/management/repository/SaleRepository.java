package com.management.repository;

import com.management.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售数据访问接口
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(s.profit) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal sumProfitByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT FUNCTION('MONTH', s.saleDate), SUM(s.totalAmount) " +
            "FROM Sale s WHERE FUNCTION('YEAR', s.saleDate) = :year " +
            "GROUP BY FUNCTION('MONTH', s.saleDate) ORDER BY FUNCTION('MONTH', s.saleDate)")
    List<Object[]> findMonthlySales(@Param("year") int year);

    @Query("SELECT p.category, SUM(si.amount) " +
            "FROM SaleItem si " +
            "JOIN si.sale s " +
            "JOIN si.product p " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.category")
    List<Object[]> findSalesByCategory(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
