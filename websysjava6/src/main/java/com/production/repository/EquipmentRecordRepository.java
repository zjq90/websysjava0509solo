package com.production.repository;

import com.production.entity.EquipmentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备生产记录数据访问层
 */
@Repository
public interface EquipmentRecordRepository extends JpaRepository<EquipmentRecord, Long> {

    /**
     * 根据设备ID查询（按时间倒序）
     */
    List<EquipmentRecord> findByEquipmentIdOrderByRecordTimeDesc(Long equipmentId);

    /**
     * 根据设备ID分页查询（按时间倒序）
     */
    Page<EquipmentRecord> findByEquipmentId(Long equipmentId, Pageable pageable);

    /**
     * 根据生产计划ID查询
     */
    List<EquipmentRecord> findByProductionPlanIdOrderByRecordTimeDesc(Long planId);

    /**
     * 根据加工记录ID查询
     */
    List<EquipmentRecord> findByProcessingRecordIdOrderByRecordTimeDesc(Long recordId);

    /**
     * 查询设备的最新记录
     */
    @Query("SELECT e FROM EquipmentRecord e WHERE e.equipment.id = :equipmentId ORDER BY e.recordTime DESC")
    List<EquipmentRecord> findLatestByEquipmentId(@Param("equipmentId") Long equipmentId);

    /**
     * 查询指定时间范围内的设备记录
     */
    @Query("SELECT e FROM EquipmentRecord e WHERE e.equipment.id = :equipmentId AND e.recordTime BETWEEN :startTime AND :endTime ORDER BY e.recordTime")
    List<EquipmentRecord> findByTimeRange(@Param("equipmentId") Long equipmentId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询故障记录
     */
    @Query("SELECT e FROM EquipmentRecord e WHERE e.runStatus = 'FAULT' OR e.alarmMessage IS NOT NULL ORDER BY e.recordTime DESC")
    List<EquipmentRecord> findAllFault();
}