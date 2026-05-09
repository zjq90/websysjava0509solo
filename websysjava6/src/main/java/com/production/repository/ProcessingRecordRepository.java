package com.production.repository;

import com.production.entity.ProcessingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 加工流程记录数据访问层
 */
@Repository
public interface ProcessingRecordRepository extends JpaRepository<ProcessingRecord, Long> {

    /**
     * 根据记录编号查询
     */
    Optional<ProcessingRecord> findByRecordCode(String recordCode);

    /**
     * 根据生产计划ID查询
     */
    List<ProcessingRecord> findByProductionPlanIdOrderByProcessOrder(Long planId);

    /**
     * 根据生产计划ID分页查询
     */
    Page<ProcessingRecord> findByProductionPlanId(Long planId, Pageable pageable);

    /**
     * 根据工序类型查询
     */
    List<ProcessingRecord> findByProcessType(String processType);

    /**
     * 根据状态查询
     */
    List<ProcessingRecord> findByStatus(String status);

    /**
     * 根据设备ID查询
     */
    List<ProcessingRecord> findByEquipmentId(Long equipmentId);

    /**
     * 检查记录编号是否存在
     */
    boolean existsByRecordCode(String recordCode);

    /**
     * 查询生产计划的最新工序
     */
    @Query("SELECT p FROM ProcessingRecord p WHERE p.productionPlan.id = :planId ORDER BY p.processOrder DESC")
    List<ProcessingRecord> findLatestByPlanId(@Param("planId") Long planId);
}