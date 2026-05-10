package com.seedtrace.repository;

import com.seedtrace.entity.SeedBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 种子批次数据访问接口
 * 
 * <p>提供种子批次的CRUD操作和自定义查询方法。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Repository
public interface SeedBatchRepository extends JpaRepository<SeedBatch, Long> {

    /**
     * 根据批次编号查询批次信息
     * 
     * @param batchCode 批次编号
     * @return 批次信息（可选）
     */
    Optional<SeedBatch> findByBatchCode(String batchCode);

    /**
     * 根据批次编号判断是否存在
     * 用于验证批次编号唯一性
     * 
     * @param batchCode 批次编号
     * @return 是否存在
     */
    boolean existsByBatchCode(String batchCode);

    /**
     * 根据状态查询批次列表
     * 
     * @param status 状态
     * @return 批次列表
     */
    List<SeedBatch> findByStatus(String status);

    /**
     * 根据种子名称模糊查询
     * 
     * @param seedName 种子名称关键字
     * @return 批次列表
     */
    @Query("SELECT s FROM SeedBatch s WHERE s.seedName LIKE %?1%")
    List<SeedBatch> findBySeedNameContaining(String seedName);

    /**
     * 查询所有活跃的批次
     * 按创建时间倒序排列
     * 
     * @return 活跃批次列表
     */
    @Query("SELECT s FROM SeedBatch s WHERE s.status = 'ACTIVE' ORDER BY s.createdAt DESC")
    List<SeedBatch> findAllActiveBatches();
}
