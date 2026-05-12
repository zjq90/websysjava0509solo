package com.hospital.finance.repository;

import com.hospital.finance.entity.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 财务报表数据访问层
 */
@Repository
public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long> {

    /**
     * 根据报表编号查询
     */
    FinancialReport findByReportNo(String reportNo);

    /**
     * 根据报表类型和日期查询
     */
    FinancialReport findByReportTypeAndReportDate(String reportType, LocalDate reportDate);

    /**
     * 根据报表类型查询
     */
    List<FinancialReport> findByReportType(String reportType);

    /**
     * 根据状态查询
     */
    List<FinancialReport> findByStatus(String status);
}