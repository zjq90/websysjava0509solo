package com.hospital.service;

import com.hospital.entity.NotificationSetting;
import com.hospital.entity.Report;
import com.hospital.repository.NotificationSettingRepository;
import com.hospital.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报告服务类
 * 处理检验检查报告业务
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private NotificationSettingRepository notificationSettingRepository;

    @Autowired
    private MessageService messageService;

    /**
     * 获取患者报告列表
     * 
     * @param patientId 患者ID
     * @return 报告列表
     */
    public List<Report> getPatientReports(Long patientId) {
        return reportRepository.findByPatientIdOrderByReportDateDesc(patientId);
    }

    /**
     * 按类型获取患者报告
     * 
     * @param patientId 患者ID
     * @param reportType 报告类型（LAB-检验，EXAM-检查）
     * @return 报告列表
     */
    public List<Report> getPatientReportsByType(Long patientId, String reportType) {
        return reportRepository.findByPatientIdAndReportTypeOrderByReportDateDesc(patientId, reportType);
    }

    /**
     * 根据ID获取报告详情
     * 
     * @param id 报告ID
     * @param userId 用户ID
     * @return 报告详情
     */
    public Report getReportById(Long id, Long userId) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("报告不存在"));

        if (!report.getPatientId().equals(userId)) {
            throw new RuntimeException("无权查看此报告");
        }

        return report;
    }

    /**
     * 创建报告（模拟LIS/PACS系统推送）
     * 
     * @param report 报告信息
     * @return 创建的报告
     */
    @Transactional(rollbackFor = Exception.class)
    public Report createReport(Report report) {
        report = reportRepository.save(report);

        NotificationSetting setting = notificationSettingRepository.findByUserId(report.getPatientId())
                .orElse(null);
        if (setting == null || setting.getReportReady() == 1) {
            String content = String.format("您好，您的%s报告已出，请及时查看。",
                    report.getReportType().equals("LAB") ? "检验" : "检查");
            messageService.createMessage(
                    report.getPatientId(),
                    "报告就绪通知",
                    content,
                    "REPORT_READY",
                    "REPORT",
                    report.getId()
            );
        }

        return report;
    }

    /**
     * 模拟下载报告
     * 
     * @param id 报告ID
     * @param userId 用户ID
     * @return 下载链接
     */
    public String downloadReport(Long id, Long userId) {
        Report report = getReportById(id, userId);
        
        return "http://localhost:8080/api/reports/" + id + "/pdf";
    }
}
