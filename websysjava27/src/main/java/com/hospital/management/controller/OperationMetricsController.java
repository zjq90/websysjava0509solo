package com.hospital.management.controller;

import com.hospital.management.entity.OperationMetrics;
import com.hospital.management.service.OperationMetricsService;
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
 * 运营指标控制器
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/operation-metrics")
@Tag(name = "运营指标管理", description = "医院运营指标的增删改查及统计分析")
@CrossOrigin(origins = "*")
public class OperationMetricsController {

    @Autowired
    private OperationMetricsService operationMetricsService;

    /**
     * 查询所有运营指标
     */
    @GetMapping
    @Operation(summary = "查询所有运营指标")
    public List<OperationMetrics> findAll() {
        return operationMetricsService.findAll();
    }

    /**
     * 根据ID查询运营指标
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询运营指标")
    public ResponseEntity<OperationMetrics> findById(@PathVariable Long id) {
        Optional<OperationMetrics> metrics = operationMetricsService.findById(id);
        return metrics.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * 新增运营指标
     */
    @PostMapping
    @Operation(summary = "新增运营指标")
    public OperationMetrics save(@RequestBody OperationMetrics operationMetrics) {
        return operationMetricsService.save(operationMetrics);
    }

    /**
     * 更新运营指标
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新运营指标")
    public ResponseEntity<OperationMetrics> update(@PathVariable Long id, @RequestBody OperationMetrics operationMetrics) {
        if (!operationMetricsService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        operationMetrics.setId(id);
        return ResponseEntity.ok(operationMetricsService.update(operationMetrics));
    }

    /**
     * 删除运营指标
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除运营指标")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!operationMetricsService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        operationMetricsService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据日期范围查询运营指标
     */
    @GetMapping("/date-range")
    @Operation(summary = "根据日期范围查询运营指标")
    public List<OperationMetrics> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return operationMetricsService.findByDateRange(startDate, endDate);
    }

    /**
     * 根据科室ID和日期范围查询运营指标
     */
    @GetMapping("/department-date-range")
    @Operation(summary = "根据科室ID和日期范围查询运营指标")
    public List<OperationMetrics> findByDepartmentAndDateRange(
            @RequestParam Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return operationMetricsService.findByDepartmentAndDateRange(departmentId, startDate, endDate);
    }

    /**
     * 获取运营指标统计汇总
     */
    @GetMapping("/statistics-summary")
    @Operation(summary = "获取运营指标统计汇总")
    public Map<String, Object> getStatisticsSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return operationMetricsService.getStatisticsSummary(startDate, endDate);
    }

    /**
     * 按科室分组统计
     */
    @GetMapping("/aggregate-by-department")
    @Operation(summary = "按科室分组统计运营数据")
    public List<Object[]> aggregateByDepartment(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return operationMetricsService.aggregateByDepartment(startDate, endDate);
    }
}
