package com.hospital.management.repository;

import com.hospital.management.entity.OperationMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 运营指标数据访问层
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface OperationMetricsRepository extends JpaRepository<OperationMetrics, Long> {

    /**
     * 根据日期范围查询运营指标
     */
    List<OperationMetrics> findByStatDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据科室ID和日期范围查询运营指标
     */
    List<OperationMetrics> findByDepartmentIdAndStatDateBetween(Long departmentId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询某日期的所有科室运营指标
     */
    List<OperationMetrics> findByStatDate(LocalDate statDate);

    /**
     * 统计指定日期范围内的门诊总量
     */
    @Query("SELECT SUM(o.outpatientCount) FROM OperationMetrics o WHERE o.statDate BETWEEN :startDate AND :endDate")
    Integer sumOutpatientCountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内的住院总量
     */
    @Query("SELECT SUM(o.inpatientCount) FROM OperationMetrics o WHERE o.statDate BETWEEN :startDate AND :endDate")
    Integer sumInpatientCountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内的手术总量
     */
    @Query("SELECT SUM(o.surgeryCount) FROM OperationMetrics o WHERE o.statDate BETWEEN :startDate AND :endDate")
    Integer sumSurgeryCountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 计算指定日期范围内的平均床位使用率
     */
    @Query("SELECT AVG(o.bedUsageRate) FROM OperationMetrics o WHERE o.statDate BETWEEN :startDate AND :endDate")
    Double avgBedUsageRateByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 计算指定日期范围内的平均住院日
     */
    @Query("SELECT AVG(o.avgHospitalizationDays) FROM OperationMetrics o WHERE o.statDate BETWEEN :startDate AND :endDate")
    Double avgHospitalizationDaysByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 按科室分组统计运营数据
     */
    @Query("SELECT o.departmentName, SUM(o.outpatientCount), SUM(o.inpatientCount), SUM(o.totalIncome) " +
           "FROM OperationMetrics o WHERE o.statDate BETWEEN :startDate AND :endDate " +
           "GROUP BY o.departmentName ORDER BY SUM(o.totalIncome) DESC")
    List<Object[]> aggregateByDepartment(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
