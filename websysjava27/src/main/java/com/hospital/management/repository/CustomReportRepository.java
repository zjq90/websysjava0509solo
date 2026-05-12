package com.hospital.management.repository;

import com.hospital.management.entity.CustomReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 自定义报表数据访问层
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface CustomReportRepository extends JpaRepository<CustomReport, Long> {

    /**
     * 根据报表类型查询报表
     */
    List<CustomReport> findByReportType(String reportType);

    /**
     * 查询所有启用的报表
     */
    List<CustomReport> findByEnabledTrue();

    /**
     * 查询系统预设报表
     */
    List<CustomReport> findByIsSystemTrue();

    /**
     * 根据创建人查询报表
     */
    List<CustomReport> findByCreator(String creator);

    /**
     * 根据报表名称模糊查询
     */
    List<CustomReport> findByReportNameContaining(String reportName);
}
