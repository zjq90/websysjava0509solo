package com.seedtrace.repository;

import com.seedtrace.entity.ProcessingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 加工信息数据访问接口
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Repository
public interface ProcessingInfoRepository extends JpaRepository<ProcessingInfo, Long> {
    List<ProcessingInfo> findByBatchIdOrderByProcessDateAsc(Long batchId);
    List<ProcessingInfo> findByBatchIdAndProcessStep(Long batchId, String processStep);
}
