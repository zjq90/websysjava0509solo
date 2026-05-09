package com.production.service;

import com.production.entity.ProductionPlan;
import com.production.repository.ProductionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 生产计划业务逻辑层
 */
@Service
@Transactional
public class ProductionPlanService {

    @Autowired
    private ProductionPlanRepository productionPlanRepository;

    /**
     * 分页查询所有生产计划
     */
    public Page<ProductionPlan> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return productionPlanRepository.findAll(pageable);
    }

    /**
     * 根据状态分页查询生产计划
     */
    public Page<ProductionPlan> findByStatusPage(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        if (status == null || status.isEmpty()) {
            return productionPlanRepository.findAll(pageable);
        }
        return productionPlanRepository.findByStatus(status, pageable);
    }

    /**
     * 查询所有生产计划
     */
    public List<ProductionPlan> findAll() {
        return productionPlanRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
    }

    /**
     * 根据ID查询
     */
    public Optional<ProductionPlan> findById(Long id) {
        return productionPlanRepository.findById(id);
    }

    /**
     * 根据计划编号查询
     */
    public Optional<ProductionPlan> findByPlanCode(String planCode) {
        return productionPlanRepository.findByPlanCode(planCode);
    }

    /**
     * 保存生产计划
     */
    public ProductionPlan save(ProductionPlan plan) {
        if (plan.getId() == null) {
            // 新增计划，检查编号是否已存在
            if (productionPlanRepository.existsByPlanCode(plan.getPlanCode())) {
                throw new RuntimeException("计划编号已存在：" + plan.getPlanCode());
            }
            if (plan.getStatus() == null) {
                plan.setStatus("PENDING");
            }
        }
        plan.setUpdateTime(LocalDateTime.now());
        return productionPlanRepository.save(plan);
    }

    /**
     * 删除生产计划
     */
    public void deleteById(Long id) {
        productionPlanRepository.deleteById(id);
    }

    /**
     * 更新计划状态
     */
    public ProductionPlan updateStatus(Long id, String status) {
        Optional<ProductionPlan> planOpt = productionPlanRepository.findById(id);
        if (planOpt.isPresent()) {
            ProductionPlan plan = planOpt.get();
            plan.setStatus(status);
            plan.setUpdateTime(LocalDateTime.now());
            // 开始执行时记录开始时间
            if ("RUNNING".equals(status) && plan.getActualStartTime() == null) {
                plan.setActualStartTime(LocalDateTime.now());
            }
            // 完成时记录结束时间
            if ("COMPLETED".equals(status)) {
                plan.setActualEndTime(LocalDateTime.now());
            }
            return productionPlanRepository.save(plan);
        }
        return null;
    }

    /**
     * 查询所有活跃计划（用于排产可视化）
     */
    public List<ProductionPlan> findAllActive() {
        return productionPlanRepository.findAllActive();
    }

    /**
     * 查询指定日期范围的计划
     */
    public List<ProductionPlan> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return productionPlanRepository.findByDateRange(startDate, endDate);
    }

    /**
     * 查询指定生产线的计划
     */
    public List<ProductionPlan> findByProductionLine(String line) {
        return productionPlanRepository.findByProductionLine(line);
    }

    /**
     * 根据状态查询
     */
    public List<ProductionPlan> findByStatus(String status) {
        return productionPlanRepository.findByStatus(status);
    }
}