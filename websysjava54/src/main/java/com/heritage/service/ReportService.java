package com.heritage.service;

import com.heritage.entity.Report;
import com.heritage.enums.AuditStatus;
import com.heritage.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }

    public Report save(Report report) {
        return reportRepository.save(report);
    }

    public void deleteById(Long id) {
        reportRepository.deleteById(id);
    }

    public List<Report> findPendingReports() {
        return reportRepository.findByStatus(AuditStatus.PENDING);
    }

    public List<Report> findByType(String reportType) {
        return reportRepository.findByReportType(reportType);
    }

    @Transactional
    public Report handleReport(Long reportId, AuditStatus status, String remark, Long handlerId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("举报不存在"));
        report.setStatus(status);
        report.setHandleRemark(remark);
        report.setHandlerId(handlerId);
        report.setHandleTime(LocalDateTime.now());
        return reportRepository.save(report);
    }

    public Report createReport(Report report) {
        report.setStatus(AuditStatus.PENDING);
        return reportRepository.save(report);
    }
}
