package com.traceability.repository;

import com.traceability.entity.QualityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质检报告数据访问接口
 */
@Repository
public interface QualityReportRepository extends JpaRepository<QualityReport, Long>, JpaSpecificationExecutor<QualityReport> {

    List<QualityReport> findByBatchNo(String batchNo);

    Optional<QualityReport> findByReportNo(String reportNo);
}
