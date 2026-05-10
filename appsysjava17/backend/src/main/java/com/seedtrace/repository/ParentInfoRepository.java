package com.seedtrace.repository;

import com.seedtrace.entity.ParentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 亲本信息数据访问接口
 * 
 * <p>提供亲本信息的CRUD操作。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Repository
public interface ParentInfoRepository extends JpaRepository<ParentInfo, Long> {

    /**
     * 根据批次ID查询亲本信息
     * 
     * @param batchId 批次ID
     * @return 亲本信息（可选）
     */
    Optional<ParentInfo> findByBatchId(Long batchId);

    /**
     * 根据批次ID列表批量查询亲本信息
     * 
     * @param batchIds 批次ID列表
     * @return 亲本信息列表
     */
    List<ParentInfo> findByBatchIdIn(List<Long> batchIds);
}
