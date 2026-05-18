package com.heritage.repository;

import com.heritage.entity.Report;
import com.heritage.enums.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByStatus(AuditStatus status);

    List<Report> findByReportType(String reportType);

    List<Report> findByReporterId(Long reporterId);

    List<Report> findByTargetIdAndReportType(Long targetId, String reportType);
}
