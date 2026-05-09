package com.inventory.repository;

import com.inventory.entity.EnvRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 环境记录Repository接口
 */
@Repository
public interface EnvRecordRepository extends JpaRepository<EnvRecord, Long> {

    /**
     * 根据仓库ID查询环境记录（按时间倒序）
     */
    List<EnvRecord> findByWarehouseIdOrderByRecordTimeDesc(Long warehouseId);

    /**
     * 根据仓库ID和时间范围查询
     */
    List<EnvRecord> findByWarehouseIdAndRecordTimeBetweenOrderByRecordTimeAsc(Long warehouseId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询某个仓库最新的环境记录
     */
    @Query("SELECT e FROM EnvRecord e WHERE e.warehouseId = :warehouseId ORDER BY e.recordTime DESC")
    List<EnvRecord> findLatestByWarehouseId(@Param("warehouseId") Long warehouseId);

    /**
     * 查询异常状态的环境记录
     */
    List<EnvRecord> findByStatusNot(Integer status);

    /**
     * 查询某个时间范围内的异常记录
     */
    List<EnvRecord> findByWarehouseIdAndStatusNotAndRecordTimeBetween(Long warehouseId, Integer status, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 删除某个仓库指定时间之前的记录（用于数据清理）
     */
    void deleteByWarehouseIdAndRecordTimeBefore(Long warehouseId, LocalDateTime beforeTime);
}
