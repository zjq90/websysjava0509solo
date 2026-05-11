package com.hospital.controller;

import com.hospital.annotation.OperationLog;
import com.hospital.common.Result;
import com.hospital.dto.ReportVO;
import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.entity.Report;
import com.hospital.entity.User;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.UserRepository;
import com.hospital.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 报告控制器
 * 处理检验检查报告相关请求
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/patient/reports")
@Tag(name = "报告管理", description = "检验检查报告相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 获取报告列表
     */
    @GetMapping
    @Operation(summary = "获取报告列表", description = "获取当前用户的所有检验检查报告")
    public Result<List<ReportVO>> getMyReports(@AuthenticationPrincipal Long userId) {
        List<Report> reports = reportService.getPatientReports(userId);
        List<ReportVO> voList = reports.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 按类型获取报告列表
     */
    @GetMapping("/type/{reportType}")
    @Operation(summary = "按类型获取报告", description = "按报告类型获取列表：LAB-检验，EXAM-检查")
    public Result<List<ReportVO>> getMyReportsByType(
            @AuthenticationPrincipal Long userId,
            @PathVariable String reportType) {
        List<Report> reports = reportService.getPatientReportsByType(userId, reportType);
        List<ReportVO> voList = reports.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 获取报告详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取报告详情", description = "根据ID获取报告详细信息")
    @OperationLog(value = "查看报告", operationType = "REPORT_VIEW", module = "REPORT")
    public Result<ReportVO> getReportById(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        Report report = reportService.getReportById(id, userId);
        return Result.success(convertToVO(report));
    }

    /**
     * 下载报告
     */
    @GetMapping("/{id}/download")
    @Operation(summary = "下载报告", description = "获取报告PDF下载链接")
    @OperationLog(value = "下载报告", operationType = "REPORT_DOWNLOAD", module = "REPORT")
    public Result<String> downloadReport(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        String downloadUrl = reportService.downloadReport(id, userId);
        return Result.success("下载链接", downloadUrl);
    }

    /**
     * 创建报告（模拟LIS/PACS推送）
     */
    @PostMapping("/create")
    @Operation(summary = "创建报告", description = "模拟LIS/PACS系统推送报告（测试用）")
    public Result<ReportVO> createReport(@RequestBody Report report) {
        Report saved = reportService.createReport(report);
        return Result.success(convertToVO(saved));
    }

    /**
     * 将Report转换为ReportVO
     */
    private ReportVO convertToVO(Report report) {
        ReportVO vo = new ReportVO();
        vo.setId(report.getId());
        vo.setReportNo(report.getReportNo());
        vo.setType(report.getReportType());
        vo.setReportType(report.getReportType());
        vo.setTitle(report.getReportName());
        vo.setReportName(report.getReportName());
        vo.setPatientId(report.getPatientId());
        vo.setDoctorId(report.getDoctorId());
        vo.setDeptId(report.getDeptId());
        vo.setAppointmentId(report.getAppointmentId());
        vo.setExamItems(report.getExamItems());
        vo.setExamDate(report.getExamDate());
        vo.setReportDate(report.getReportDate());
        vo.setAuditDoctor(report.getAuditDoctor());
        vo.setAuditTime(report.getAuditTime());
        vo.setSummary(report.getSummary());
        vo.setConclusion(report.getConclusion());
        vo.setSuggestion(report.getSuggestion());
        vo.setPdfPath(report.getPdfPath());
        vo.setReportData(report.getReportData());
        vo.setSourceSystem(report.getSourceSystem());
        vo.setIsAbnormal(report.getIsAbnormal());
        vo.setStatus(report.getStatus());
        vo.setCreateTime(report.getCreateTime());
        vo.setHospital("智慧医院");
        
        User patient = userRepository.findById(report.getPatientId()).orElse(null);
        if (patient != null) {
            vo.setPatientName(patient.getName());
        }
        
        if (report.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(report.getDoctorId()).orElse(null);
            if (doctor != null) {
                vo.setDoctorName(doctor.getDoctorName());
            }
        }
        
        if (report.getDeptId() != null) {
            Department dept = departmentRepository.findById(report.getDeptId()).orElse(null);
            if (dept != null) {
                vo.setDepartmentName(dept.getDeptName());
            }
        }
        
        return vo;
    }
}
