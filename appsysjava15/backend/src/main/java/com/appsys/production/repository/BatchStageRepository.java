package com.appsys.production.repository;

import com.appsys.production.entity.BatchStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchStageRepository extends JpaRepository<BatchStage, Long> {
    List<BatchStage> findByBatchIdOrderBySortOrderAsc(Long batchId);
    Optional<BatchStage> findByBatchIdAndStageCode(Long batchId, String stageCode);
    List<BatchStage> findByStatusAndEndTimeIsNull(String status);
}
