package com.traceability.repository;

import com.traceability.entity.SeedBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 种子批次数据访问接口
 * 提供种子批次的CRUD操作和自定义查询
 */
@Repository
public interface SeedBatchRepository extends JpaRepository<SeedBatch, Long>, JpaSpecificationExecutor<SeedBatch> {

    Optional<SeedBatch> findByBatchNo(String batchNo);

    List<SeedBatch> findBySeedNameContaining(String seedName);

    List<SeedBatch> findByStatus(String status);

    boolean existsByBatchNo(String batchNo);
}
