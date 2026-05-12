package com.hospital.management.repository;

import com.hospital.management.entity.MedicalQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 医疗质量数据访问层
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface MedicalQualityRepository extends JpaRepository<MedicalQuality, Long> {

    /**
     * 根据日期范围查询医疗质量数据
     */
    List<MedicalQuality> findByStatDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 根据科室ID和日期范围查询医疗质量数据
     */
    List<MedicalQuality> findByDepartmentIdAndStatDateBetween(Long departmentId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询某日期的所有科室医疗质量数据
     */
    List<MedicalQuality> findByStatDate(LocalDate statDate);

    /**
     * 计算指定日期范围内的平均病历合格率
     */
    @Query("SELECT AVG(m.recordQualificationRate) FROM MedicalQuality m WHERE m.statDate BETWEEN :startDate AND :endDate")
    Double avgRecordQualificationRateByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 计算指定日期范围内的平均合理用药率
     */
    @Query("SELECT AVG(m.rationalDrugUseRate) FROM MedicalQuality m WHERE m.statDate BETWEEN :startDate AND :endDate")
    Double avgRationalDrugUseRateByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 计算指定日期范围内的平均院内感染率
     */
    @Query("SELECT AVG(m.nosocomialInfectionRate) FROM MedicalQuality m WHERE m.statDate BETWEEN :startDate AND :endDate")
    Double avgNosocomialInfectionRateByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内的不良事件总数
     */
    @Query("SELECT SUM(m.adverseEventCount) FROM MedicalQuality m WHERE m.statDate BETWEEN :startDate AND :endDate")
    Integer sumAdverseEventCountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 按科室分组统计医疗质量数据
     */
    @Query("SELECT m.departmentName, AVG(m.recordQualificationRate), AVG(m.rationalDrugUseRate), AVG(m.nosocomialInfectionRate) " +
           "FROM MedicalQuality m WHERE m.statDate BETWEEN :startDate AND :endDate " +
           "GROUP BY m.departmentName")
    List<Object[]> aggregateByDepartment(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
