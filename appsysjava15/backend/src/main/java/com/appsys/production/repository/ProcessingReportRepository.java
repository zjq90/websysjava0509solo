package com.appsys.production.repository;

import com.appsys.production.entity.ProcessingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessingReportRepository extends JpaRepository<ProcessingReport, Long> {
    Optional<ProcessingReport> findByBatchId(Long batchId);
    Optional<ProcessingReport> findByReportNo(String reportNo);
    Optional<ProcessingReport> findByBatchNo(String batchNo);
}
