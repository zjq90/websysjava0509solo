package com.hospital.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 检验检查报告实体类
 * 存储检验、检查报告信息
 * 
 * @author hospital
 * @version 1.0.0
 */
@Entity
@Table(name = "biz_report")
public class Report {

    /**
     * 报告ID（主键）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 报告编号
     */
    @Column(unique = true, nullable = false, length = 50)
    private String reportNo;

    /**
     * 报告类型：LAB-检验报告，EXAM-检查报告
     */
    @Column(nullable = false, length = 20)
    private String reportType;

    /**
     * 报告名称
     */
    @Column(nullable = false, length = 200)
    private String reportName;

    /**
     * 患者用户ID
     */
    @Column(nullable = false)
    private Long patientId;

    /**
     * 申请医生ID
     */
    private Long doctorId;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 关联预约ID
     */
    private Long appointmentId;

    /**
     * 检查项目名称
     */
    @Column(length = 500)
    private String examItems;

    /**
     * 检查日期
     */
    private LocalDateTime examDate;

    /**
     * 报告生成时间
     */
    @Column(nullable = false)
    private LocalDateTime reportDate;

    /**
     * 审核医生
     */
    @Column(length = 100)
    private String auditDoctor;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 报告摘要
     */
    @Column(columnDefinition = "TEXT")
    private String summary;

    /**
     * 报告结论
     */
    @Column(columnDefinition = "TEXT")
    private String conclusion;

    /**
     * 报告建议
     */
    @Column(columnDefinition = "TEXT")
    private String suggestion;

    /**
     * PDF文件路径
     */
    @Column(length = 500)
    private String pdfPath;

    /**
     * 报告JSON数据
     */
    @Column(columnDefinition = "TEXT")
    private String reportData;

    /**
     * 来源系统：LIS-检验系统，PACS-影像系统，HIS-医院信息系统
     */
    @Column(length = 20)
    private String sourceSystem;

    /**
     * 来源系统的报告ID
     */
    @Column(length = 50)
    private String sourceReportId;

    /**
     * 是否异常：0-正常，1-异常
     */
    private Integer isAbnormal = 0;

    /**
     * 状态：GENERATING-生成中，READY-已就绪，ARCHIVED-已归档
     */
    @Column(length = 20)
    private String status = "READY";

    /**
     * 是否发送通知
     */
    private Integer notified = 0;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getExamItems() {
        return examItems;
    }

    public void setExamItems(String examItems) {
        this.examItems = examItems;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public String getAuditDoctor() {
        return auditDoctor;
    }

    public void setAuditDoctor(String auditDoctor) {
        this.auditDoctor = auditDoctor;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceReportId() {
        return sourceReportId;
    }

    public void setSourceReportId(String sourceReportId) {
        this.sourceReportId = sourceReportId;
    }

    public Integer getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Integer isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getNotified() {
        return notified;
    }

    public void setNotified(Integer notified) {
        this.notified = notified;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
