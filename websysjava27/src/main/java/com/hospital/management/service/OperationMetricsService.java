package com.hospital.management.service;

import com.hospital.management.entity.OperationMetrics;
import com.hospital.management.repository.OperationMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 运营指标服务层
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class OperationMetricsService {

    @Autowired
    private OperationMetricsRepository operationMetricsRepository;

    /**
     * 查询所有运营指标
     */
    public List<OperationMetrics> findAll() {
        return operationMetricsRepository.findAll();
    }

    /**
     * 根据ID查询运营指标
     */
    public Optional<OperationMetrics> findById(Long id) {
        return operationMetricsRepository.findById(id);
    }

    /**
     * 新增运营指标
     */
    public OperationMetrics save(OperationMetrics operationMetrics) {
        return operationMetricsRepository.save(operationMetrics);
    }

    /**
     * 更新运营指标
     */
    public OperationMetrics update(OperationMetrics operationMetrics) {
        return operationMetricsRepository.save(operationMetrics);
    }

    /**
     * 删除运营指标
     */
    public void deleteById(Long id) {
        operationMetricsRepository.deleteById(id);
    }

    /**
     * 根据日期范围查询运营指标
     */
    public List<OperationMetrics> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return operationMetricsRepository.findByStatDateBetween(startDate, endDate);
    }

    /**
     * 根据科室ID和日期范围查询运营指标
     */
    public List<OperationMetrics> findByDepartmentAndDateRange(Long departmentId, LocalDate startDate, LocalDate endDate) {
        return operationMetricsRepository.findByDepartmentIdAndStatDateBetween(departmentId, startDate, endDate);
    }

    /**
     * 获取运营指标统计汇总
     */
    public Map<String, Object> getStatisticsSummary(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        Integer totalOutpatients = operationMetricsRepository.sumOutpatientCountByDateRange(startDate, endDate);
        Integer totalInpatients = operationMetricsRepository.sumInpatientCountByDateRange(startDate, endDate);
        Integer totalSurgeries = operationMetricsRepository.sumSurgeryCountByDateRange(startDate, endDate);
        Double avgBedUsageRate = operationMetricsRepository.avgBedUsageRateByDateRange(startDate, endDate);
        Double avgHospitalizationDays = operationMetricsRepository.avgHospitalizationDaysByDateRange(startDate, endDate);
        
        summary.put("totalOutpatients", totalOutpatients != null ? totalOutpatients : 0);
        summary.put("totalInpatients", totalInpatients != null ? totalInpatients : 0);
        summary.put("totalSurgeries", totalSurgeries != null ? totalSurgeries : 0);
        summary.put("avgBedUsageRate", avgBedUsageRate != null ? String.format("%.2f", avgBedUsageRate) : "0.00");
        summary.put("avgHospitalizationDays", avgHospitalizationDays != null ? String.format("%.2f", avgHospitalizationDays) : "0.00");
        
        return summary;
    }

    /**
     * 按科室分组统计
     */
    public List<Object[]> aggregateByDepartment(LocalDate startDate, LocalDate endDate) {
        return operationMetricsRepository.aggregateByDepartment(startDate, endDate);
    }
}
