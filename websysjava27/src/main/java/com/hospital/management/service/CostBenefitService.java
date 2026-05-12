package com.hospital.management.service;

import com.hospital.management.entity.CostBenefit;
import com.hospital.management.repository.CostBenefitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 成本效益服务层
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class CostBenefitService {

    @Autowired
    private CostBenefitRepository costBenefitRepository;

    /**
     * 查询所有成本效益数据
     */
    public List<CostBenefit> findAll() {
        return costBenefitRepository.findAll();
    }

    /**
     * 根据ID查询成本效益数据
     */
    public Optional<CostBenefit> findById(Long id) {
        return costBenefitRepository.findById(id);
    }

    /**
     * 新增成本效益数据
     */
    public CostBenefit save(CostBenefit costBenefit) {
        return costBenefitRepository.save(costBenefit);
    }

    /**
     * 更新成本效益数据
     */
    public CostBenefit update(CostBenefit costBenefit) {
        return costBenefitRepository.save(costBenefit);
    }

    /**
     * 删除成本效益数据
     */
    public void deleteById(Long id) {
        costBenefitRepository.deleteById(id);
    }

    /**
     * 根据日期范围查询成本效益数据
     */
    public List<CostBenefit> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return costBenefitRepository.findByStatDateBetween(startDate, endDate);
    }

    /**
     * 根据科室ID和日期范围查询成本效益数据
     */
    public List<CostBenefit> findByDepartmentAndDateRange(Long departmentId, LocalDate startDate, LocalDate endDate) {
        return costBenefitRepository.findByDepartmentIdAndStatDateBetween(departmentId, startDate, endDate);
    }

    /**
     * 获取成本效益统计汇总
     */
    public Map<String, Object> getStatisticsSummary(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        Double totalCost = costBenefitRepository.sumTotalCostByDateRange(startDate, endDate);
        Double totalIncome = costBenefitRepository.sumTotalIncomeByDateRange(startDate, endDate);
        Double totalProfit = costBenefitRepository.sumProfitByDateRange(startDate, endDate);
        Double avgProfitMargin = costBenefitRepository.avgProfitMarginByDateRange(startDate, endDate);
        
        summary.put("totalCost", totalCost != null ? String.format("%.2f", totalCost) : "0.00");
        summary.put("totalIncome", totalIncome != null ? String.format("%.2f", totalIncome) : "0.00");
        summary.put("totalProfit", totalProfit != null ? String.format("%.2f", totalProfit) : "0.00");
        summary.put("avgProfitMargin", avgProfitMargin != null ? String.format("%.2f", avgProfitMargin) : "0.00");
        
        return summary;
    }

    /**
     * 按科室分组统计
     */
    public List<Object[]> aggregateByDepartment(LocalDate startDate, LocalDate endDate) {
        return costBenefitRepository.aggregateByDepartment(startDate, endDate);
    }
}
