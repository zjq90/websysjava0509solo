package com.hospital.finance.service;

import com.hospital.finance.entity.FinancialReport;
import com.hospital.finance.entity.InpatientCharge;
import com.hospital.finance.entity.OutpatientCharge;
import com.hospital.finance.repository.FinancialReportRepository;
import com.hospital.finance.repository.InpatientChargeRepository;
import com.hospital.finance.repository.OutpatientChargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 财务报表服务层
 */
@Service
public class FinancialReportService {

    @Autowired
    private FinancialReportRepository reportRepository;

    @Autowired
    private OutpatientChargeRepository outpatientRepository;

    @Autowired
    private InpatientChargeRepository inpatientRepository;

    /**
     * 生成报表编号
     */
    private String generateReportNo(String reportType, LocalDate reportDate) {
        String prefix = "日报".equals(reportType) ? "D" : ("月报".equals(reportType) ? "M" : "Y");
        return prefix + reportDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 生成日报表
     */
    @Transactional
    public FinancialReport generateDailyReport(LocalDate reportDate) {
        LocalDateTime start = LocalDateTime.of(reportDate, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(reportDate, LocalTime.MAX);

        List<OutpatientCharge> outpatientCharges = outpatientRepository.findByChargeTimeBetween(start, end);
        List<InpatientCharge> inpatientCharges = inpatientRepository.findByChargeTimeBetween(start, end);

        FinancialReport report = new FinancialReport();
        report.setReportNo(generateReportNo("日报", reportDate));
        report.setReportType("日报");
        report.setReportDate(reportDate);
        report.setStatus("待审核");

        BigDecimal outpatientTotal = outpatientCharges.stream()
                .filter(c -> "已缴费".equals(c.getStatus()))
                .map(OutpatientCharge::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal inpatientTotal = inpatientCharges.stream()
                .filter(c -> "已结算".equals(c.getStatus()))
                .map(InpatientCharge::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        report.setOutpatientCount((int) outpatientCharges.stream().filter(c -> "已缴费".equals(c.getStatus())).count());
        report.setOutpatientAmount(outpatientTotal);
        report.setInpatientCount((int) inpatientCharges.stream().filter(c -> "已结算".equals(c.getStatus())).count());
        report.setInpatientAmount(inpatientTotal);
        report.setTotalAmount(outpatientTotal.add(inpatientTotal));

        BigDecimal outpatientInsurance = outpatientCharges.stream()
                .filter(c -> "已缴费".equals(c.getStatus()))
                .map(OutpatientCharge::getInsuranceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal inpatientInsurance = inpatientCharges.stream()
                .filter(c -> "已结算".equals(c.getStatus()))
                .map(InpatientCharge::getInsuranceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        report.setInsuranceAmount(outpatientInsurance.add(inpatientInsurance));
        report.setSelfPayAmount(report.getTotalAmount().subtract(report.getInsuranceAmount()));

        return reportRepository.save(report);
    }

    /**
     * 生成月报表
     */
    @Transactional
    public FinancialReport generateMonthlyReport(LocalDate reportDate) {
        LocalDate firstDay = reportDate.withDayOfMonth(1);
        LocalDate lastDay = reportDate.withDayOfMonth(reportDate.lengthOfMonth());

        LocalDateTime start = LocalDateTime.of(firstDay, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(lastDay, LocalTime.MAX);

        List<OutpatientCharge> outpatientCharges = outpatientRepository.findByChargeTimeBetween(start, end);
        List<InpatientCharge> inpatientCharges = inpatientRepository.findByChargeTimeBetween(start, end);

        FinancialReport report = new FinancialReport();
        report.setReportNo(generateReportNo("月报", reportDate));
        report.setReportType("月报");
        report.setReportDate(reportDate);
        report.setStatus("待审核");

        BigDecimal outpatientTotal = outpatientCharges.stream()
                .filter(c -> "已缴费".equals(c.getStatus()))
                .map(OutpatientCharge::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal inpatientTotal = inpatientCharges.stream()
                .filter(c -> "已结算".equals(c.getStatus()))
                .map(InpatientCharge::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        report.setOutpatientCount((int) outpatientCharges.stream().filter(c -> "已缴费".equals(c.getStatus())).count());
        report.setOutpatientAmount(outpatientTotal);
        report.setInpatientCount((int) inpatientCharges.stream().filter(c -> "已结算".equals(c.getStatus())).count());
        report.setInpatientAmount(inpatientTotal);
        report.setTotalAmount(outpatientTotal.add(inpatientTotal));

        BigDecimal outpatientInsurance = outpatientCharges.stream()
                .filter(c -> "已缴费".equals(c.getStatus()))
                .map(OutpatientCharge::getInsuranceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal inpatientInsurance = inpatientCharges.stream()
                .filter(c -> "已结算".equals(c.getStatus()))
                .map(InpatientCharge::getInsuranceAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        report.setInsuranceAmount(outpatientInsurance.add(inpatientInsurance));
        report.setSelfPayAmount(report.getTotalAmount().subtract(report.getInsuranceAmount()));

        return reportRepository.save(report);
    }

    /**
     * 审核报表
     */
    @Transactional
    public FinancialReport auditReport(Long reportId, String auditor) {
        return reportRepository.findById(reportId).map(report -> {
            report.setStatus("已审核");
            report.setAuditor(auditor);
            report.setAuditTime(LocalDateTime.now());
            return reportRepository.save(report);
        }).orElse(null);
    }

    /**
     * 结账
     */
    @Transactional
    public FinancialReport closeReport(Long reportId) {
        return reportRepository.findById(reportId).map(report -> {
            report.setStatus("已结账");
            return reportRepository.save(report);
        }).orElse(null);
    }

    /**
     * 根据ID查询
     */
    public java.util.Optional<FinancialReport> findById(Long id) {
        return reportRepository.findById(id);
    }

    /**
     * 查询所有报表
     */
    public List<FinancialReport> findAll() {
        return reportRepository.findAll();
    }

    /**
     * 根据类型查询
     */
    public List<FinancialReport> findByReportType(String reportType) {
        return reportRepository.findByReportType(reportType);
    }
}