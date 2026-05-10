package com.seedtrace.repository;

import com.seedtrace.entity.TraceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 溯源查询日志数据访问接口
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Repository
public interface TraceLogRepository extends JpaRepository<TraceLog, Long> {
    List<TraceLog> findByBatchIdOrderByQueryTimeDesc(Long batchId);
    List<TraceLog> findByBatchCodeOrderByQueryTimeDesc(String batchCode);
    long countByBatchId(Long batchId);
}
