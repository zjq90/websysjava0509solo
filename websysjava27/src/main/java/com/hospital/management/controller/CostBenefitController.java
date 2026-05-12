package com.hospital.management.controller;

import com.hospital.management.entity.CostBenefit;
import com.hospital.management.service.CostBenefitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 成本效益控制器
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/cost-benefit")
@Tag(name = "成本效益管理", description = "医院成本效益的增删改查及统计分析")
@CrossOrigin(origins = "*")
public class CostBenefitController {

    @Autowired
    private CostBenefitService costBenefitService;

    /**
     * 查询所有成本效益数据
     */
    @GetMapping
    @Operation(summary = "查询所有成本效益数据")
    public List<CostBenefit> findAll() {
        return costBenefitService.findAll();
    }

    /**
     * 根据ID查询成本效益数据
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询成本效益数据")
    public ResponseEntity<CostBenefit> findById(@PathVariable Long id) {
        Optional<CostBenefit> benefit = costBenefitService.findById(id);
        return benefit.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * 新增成本效益数据
     */
    @PostMapping
    @Operation(summary = "新增成本效益数据")
    public CostBenefit save(@RequestBody CostBenefit costBenefit) {
        return costBenefitService.save(costBenefit);
    }

    /**
     * 更新成本效益数据
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新成本效益数据")
    public ResponseEntity<CostBenefit> update(@PathVariable Long id, @RequestBody CostBenefit costBenefit) {
        if (!costBenefitService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        costBenefit.setId(id);
        return ResponseEntity.ok(costBenefitService.update(costBenefit));
    }

    /**
     * 删除成本效益数据
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除成本效益数据")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!costBenefitService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        costBenefitService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据日期范围查询成本效益数据
     */
    @GetMapping("/date-range")
    @Operation(summary = "根据日期范围查询成本效益数据")
    public List<CostBenefit> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return costBenefitService.findByDateRange(startDate, endDate);
    }

    /**
     * 根据科室ID和日期范围查询成本效益数据
     */
    @GetMapping("/department-date-range")
    @Operation(summary = "根据科室ID和日期范围查询成本效益数据")
    public List<CostBenefit> findByDepartmentAndDateRange(
            @RequestParam Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return costBenefitService.findByDepartmentAndDateRange(departmentId, startDate, endDate);
    }

    /**
     * 获取成本效益统计汇总
     */
    @GetMapping("/statistics-summary")
    @Operation(summary = "获取成本效益统计汇总")
    public Map<String, Object> getStatisticsSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return costBenefitService.getStatisticsSummary(startDate, endDate);
    }

    /**
     * 按科室分组统计
     */
    @GetMapping("/aggregate-by-department")
    @Operation(summary = "按科室分组统计成本效益数据")
    public List<Object[]> aggregateByDepartment(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return costBenefitService.aggregateByDepartment(startDate, endDate);
    }
}
