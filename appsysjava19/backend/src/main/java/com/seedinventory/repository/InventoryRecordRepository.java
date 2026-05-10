package com.seedinventory.repository;

import com.seedinventory.entity.InventoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 出入库记录数据访问层
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Repository
public interface InventoryRecordRepository extends JpaRepository<InventoryRecord, Long> {
    
    /**
     * 根据批次号查询记录
     */
    List<InventoryRecord> findByBatchNoOrderByRecordTimeDesc(String batchNo);
    
    /**
     * 根据记录类型查询
     */
    List<InventoryRecord> findByRecordTypeOrderByRecordTimeDesc(String recordType);
    
    /**
     * 根据仓库ID查询
     */
    List<InventoryRecord> findByWarehouseIdOrderByRecordTimeDesc(Long warehouseId);
    
    /**
     * 根据时间范围查询
     */
    @Query("SELECT r FROM InventoryRecord r WHERE r.recordTime BETWEEN :start AND :end ORDER BY r.recordTime DESC")
    List<InventoryRecord> findByTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    /**
     * 判断记录编号是否存在
     */
    boolean existsByRecordNo(String recordNo);
}
