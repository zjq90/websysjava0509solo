package com.hospital.management.repository;

import com.hospital.management.entity.CostBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 成本效益数据访问层
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface CostBenefitRepository extends JpaRepository<CostBenefit, Long> {

    /**
     * 根据日期范围查询成本效益数据
     */
    List<CostBenefit> findByStatDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据科室ID和日期范围查询成本效益数据
     */
    List<CostBenefit> findByDepartmentIdAndStatDateBetween(Long departmentId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询某日期的所有科室成本效益数据
     */
    List<CostBenefit> findByStatDate(LocalDate statDate);

    /**
     * 统计指定日期范围内的总成本
     */
    @Query("SELECT SUM(c.totalCost) FROM CostBenefit c WHERE c.statDate BETWEEN :startDate AND :endDate")
    Double sumTotalCostByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内的总收入
     */
    @Query("SELECT SUM(c.totalIncome) FROM CostBenefit c WHERE c.statDate BETWEEN :startDate AND :endDate")
    Double sumTotalIncomeByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内的总利润
     */
    @Query("SELECT SUM(c.profit) FROM CostBenefit c WHERE c.statDate BETWEEN :startDate AND :endDate")
    Double sumProfitByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 计算指定日期范围内的平均利润率
     */
    @Query("SELECT AVG(c.profitMargin) FROM CostBenefit c WHERE c.statDate BETWEEN :startDate AND :endDate")
    Double avgProfitMarginByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 按科室分组统计成本效益数据
     */
    @Query("SELECT c.departmentName, SUM(c.totalCost), SUM(c.totalIncome), SUM(c.profit), AVG(c.profitMargin) " +
           "FROM CostBenefit c WHERE c.statDate BETWEEN :startDate AND :endDate " +
           "GROUP BY c.departmentName ORDER BY SUM(c.profit) DESC")
    List<Object[]> aggregateByDepartment(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
