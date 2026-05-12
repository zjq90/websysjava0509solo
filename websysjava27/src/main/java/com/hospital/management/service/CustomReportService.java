package com.hospital.management.service;

import com.hospital.management.entity.CustomReport;
import com.hospital.management.repository.CustomReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 自定义报表服务层
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class CustomReportService {

    @Autowired
    private CustomReportRepository customReportRepository;

    /**
     * 查询所有报表
     */
    public List<CustomReport> findAll() {
        return customReportRepository.findAll();
    }

    /**
     * 根据ID查询报表
     */
    public Optional<CustomReport> findById(Long id) {
        return customReportRepository.findById(id);
    }

    /**
     * 新增报表
     */
    public CustomReport save(CustomReport customReport) {
        return customReportRepository.save(customReport);
    }

    /**
     * 更新报表
     */
    public CustomReport update(CustomReport customReport) {
        return customReportRepository.save(customReport);
    }

    /**
     * 删除报表
     */
    public void deleteById(Long id) {
        customReportRepository.deleteById(id);
    }

    /**
     * 根据报表类型查询
     */
    public List<CustomReport> findByReportType(String reportType) {
        return customReportRepository.findByReportType(reportType);
    }

    /**
     * 查询所有启用的报表
     */
    public List<CustomReport> findEnabledReports() {
        return customReportRepository.findByEnabledTrue();
    }

    /**
     * 查询系统预设报表
     */
    public List<CustomReport> findSystemReports() {
        return customReportRepository.findByIsSystemTrue();
    }

    /**
     * 根据报表名称模糊查询
     */
    public List<CustomReport> findByReportNameContaining(String reportName) {
        return customReportRepository.findByReportNameContaining(reportName);
    }
}
