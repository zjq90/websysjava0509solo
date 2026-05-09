package com.appsys.production.repository;

import com.appsys.production.entity.ProductionBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductionBatchRepository extends JpaRepository<ProductionBatch, Long> {
    Optional<ProductionBatch> findByBatchNo(String batchNo);
    boolean existsByBatchNo(String batchNo);
}
