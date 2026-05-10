package com.seedtrace.repository;

import com.seedtrace.entity.FieldManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 田间管理记录数据访问接口
 * 
 * <p>提供田间管理记录的CRUD操作。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Repository
public interface FieldManagementRepository extends JpaRepository<FieldManagement, Long> {

    /**
     * 根据批次ID查询田间管理记录
     * 按操作日期升序排列
     * 
     * @param batchId 批次ID
     * @return 田间管理记录列表
     */
    List<FieldManagement> findByBatchIdOrderByOperationDateAsc(Long batchId);

    /**
     * 根据批次ID和操作类型查询记录
     * 
     * @param batchId 批次ID
     * @param operationType 操作类型
     * @return 田间管理记录列表
     */
    List<FieldManagement> findByBatchIdAndOperationType(Long batchId, String operationType);

    /**
     * 根据批次ID查询所有记录，按操作日期排序
     * 
     * @param batchId 批次ID
     * @return 田间管理记录列表
     */
    @Query("SELECT f FROM FieldManagement f WHERE f.batchId = ?1 ORDER BY f.operationDate ASC, f.createdAt ASC")
    List<FieldManagement> findAllByBatchIdOrdered(Long batchId);
}
