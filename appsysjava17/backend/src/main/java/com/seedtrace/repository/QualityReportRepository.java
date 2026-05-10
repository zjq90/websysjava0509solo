package com.seedtrace.repository;

import com.seedtrace.entity.QualityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质检报告数据访问接口
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Repository
public interface QualityReportRepository extends JpaRepository<QualityReport, Long> {
    List<QualityReport> findByBatchIdOrderByInspectionDateDesc(Long batchId);
    Optional<QualityReport> findByReportNo(String reportNo);
}
