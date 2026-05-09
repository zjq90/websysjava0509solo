package com.appsys.production.repository;

import com.appsys.production.entity.QualityInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualityInspectionRepository extends JpaRepository<QualityInspection, Long> {
    List<QualityInspection> findByBatchId(Long batchId);
    Optional<QualityInspection> findByBatchIdAndStageCode(Long batchId, String stageCode);
}
