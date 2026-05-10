package com.seedtrace.repository;

import com.seedtrace.entity.SalesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 销售信息数据访问接口
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Repository
public interface SalesInfoRepository extends JpaRepository<SalesInfo, Long> {
    List<SalesInfo> findByBatchIdOrderBySalesDateDesc(Long batchId);
}
