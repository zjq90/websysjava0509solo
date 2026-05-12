package com.lims.service;

import com.lims.entity.TestReport;
import com.lims.repository.TestReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 检验检查报告业务逻辑层
 * 实现报告审核与发布、患者查询功能
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Service
public class TestReportService {

    @Autowired
    private TestReportRepository testReportRepository;

    /**
     * 查询所有报告
     */
    public List<TestReport> findAll() {
        return testReportRepository.findAll();
    }

    /**
     * 根据ID查询报告
     */
    public Optional<TestReport> findById(Long id) {
        return testReportRepository.findById(id);
    }

    /**
     * 根据报告编号查询
     */
    public TestReport findByReportNo(String reportNo) {
        return testReportRepository.findByReportNo(reportNo);
    }

    /**
     * 根据状态查询
     */
    public List<TestReport> findByStatus(String status) {
        return testReportRepository.findByStatus(status);
    }

    /**
     * 根据申请ID查询报告
     */
    public List<TestReport> findByApplicationId(Long applicationId) {
        return testReportRepository.findByApplicationId(applicationId);
    }

    /**
     * 新增报告
     */
    public TestReport save(TestReport testReport) {
        return testReportRepository.save(testReport);
    }

    /**
     * 更新报告
     */
    public TestReport update(TestReport testReport) {
        return testReportRepository.save(testReport);
    }

    /**
     * 提交一级审核
     */
    public TestReport submitFirstAudit(Long id) {
        Optional<TestReport> optional = testReportRepository.findById(id);
        if (optional.isPresent()) {
            TestReport report = optional.get();
            report.setStatus("FIRST_AUDIT");
            return testReportRepository.save(report);
        }
        return null;
    }

    /**
     * 一级审核通过
     */
    public TestReport firstAuditPass(Long id, Long auditorId, String opinion) {
        Optional<TestReport> optional = testReportRepository.findById(id);
        if (optional.isPresent()) {
            TestReport report = optional.get();
            report.setFirstAuditorId(auditorId);
            report.setFirstAuditOpinion(opinion);
            report.setFirstAuditTime(LocalDateTime.now());
            report.setStatus("SECOND_AUDIT");
            return testReportRepository.save(report);
        }
        return null;
    }

    /**
     * 一级审核驳回
     */
    public TestReport firstAuditReject(Long id, Long auditorId, String opinion) {
        Optional<TestReport> optional = testReportRepository.findById(id);
        if (optional.isPresent()) {
            TestReport report = optional.get();
            report.setFirstAuditorId(auditorId);
            report.setFirstAuditOpinion(opinion);
            report.setFirstAuditTime(LocalDateTime.now());
            report.setStatus("REJECTED");
            return testReportRepository.save(report);
        }
        return null;
    }

    /**
     * 二级审核通过并发布
     */
    public TestReport secondAuditPassAndPublish(Long id, Long auditorId, String opinion) {
        Optional<TestReport> optional = testReportRepository.findById(id);
        if (optional.isPresent()) {
            TestReport report = optional.get();
            report.setSecondAuditorId(auditorId);
            report.setSecondAuditOpinion(opinion);
            report.setSecondAuditTime(LocalDateTime.now());
            report.setPublishedBy(auditorId);
            report.setPublishedTime(LocalDateTime.now());
            report.setStatus("PUBLISHED");
            return testReportRepository.save(report);
        }
        return null;
    }

    /**
     * 患者查看报告
     */
    public TestReport patientViewReport(Long id) {
        Optional<TestReport> optional = testReportRepository.findById(id);
        if (optional.isPresent()) {
            TestReport report = optional.get();
            report.setPatientViewed(true);
            report.setPatientViewTime(LocalDateTime.now());
            return testReportRepository.save(report);
        }
        return null;
    }

    /**
     * 删除报告
     */
    public void deleteById(Long id) {
        testReportRepository.deleteById(id);
    }
}
