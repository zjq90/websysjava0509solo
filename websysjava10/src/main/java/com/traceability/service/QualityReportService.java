package com.traceability.service;

import com.traceability.entity.QualityReport;
import com.traceability.repository.QualityReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 质检报告业务逻辑服务类
 */
@Service
@Transactional
public class QualityReportService {

    @Autowired
    private QualityReportRepository qualityReportRepository;

    public List<QualityReport> findAll() {
        return qualityReportRepository.findAll();
    }

    public Page<QualityReport> findAll(Pageable pageable) {
        return qualityReportRepository.findAll(pageable);
    }

    public Optional<QualityReport> findById(Long id) {
        return qualityReportRepository.findById(id);
    }

    public List<QualityReport> findByBatchNo(String batchNo) {
        return qualityReportRepository.findByBatchNo(batchNo);
    }

    public Optional<QualityReport> findByReportNo(String reportNo) {
        return qualityReportRepository.findByReportNo(reportNo);
    }

    public QualityReport save(QualityReport qualityReport) {
        if (qualityReport.getReportNo() == null || qualityReport.getReportNo().isEmpty()) {
            qualityReport.setReportNo("RPT" + System.currentTimeMillis());
        }
        return qualityReportRepository.save(qualityReport);
    }

    public QualityReport update(QualityReport qualityReport) {
        return qualityReportRepository.save(qualityReport);
    }

    public void deleteById(Long id) {
        qualityReportRepository.deleteById(id);
    }
}
