package com.production.repository;

import com.production.entity.ProductionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 生产计划数据访问层
 */
@Repository
public interface ProductionPlanRepository extends JpaRepository<ProductionPlan, Long> {

    /**
     * 根据计划编号查询
     */
    Optional<ProductionPlan> findByPlanCode(String planCode);

    /**
     * 根据产品ID查询
     */
    List<ProductionPlan> findByProductId(Long productId);

    /**
     * 根据状态查询
     */
    List<ProductionPlan> findByStatus(String status);

    /**
     * 根据状态分页查询
     */
    Page<ProductionPlan> findByStatus(String status, Pageable pageable);

    /**
     * 根据批次号查询
     */
    List<ProductionPlan> findByBatchNumber(String batchNumber);

    /**
     * 检查计划编号是否存在
     */
    boolean existsByPlanCode(String planCode);

    /**
     * 查询指定日期范围内的生产计划
     */
    @Query("SELECT p FROM ProductionPlan p WHERE p.planStartDate <= :endDate AND p.planEndDate >= :startDate ORDER BY p.priority DESC, p.planStartDate ASC")
    List<ProductionPlan> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 查询指定生产线的计划
     */
    @Query("SELECT p FROM ProductionPlan p WHERE p.productionLine = :line AND p.status IN ('PENDING', 'RUNNING', 'PAUSED') ORDER BY p.planStartDate ASC")
    List<ProductionPlan> findByProductionLine(@Param("line") String line);

    /**
     * 查询所有进行中的计划
     */
    @Query("SELECT p FROM ProductionPlan p WHERE p.status IN ('PENDING', 'RUNNING', 'PAUSED') ORDER BY p.priority DESC, p.planStartDate ASC")
    List<ProductionPlan> findAllActive();
}