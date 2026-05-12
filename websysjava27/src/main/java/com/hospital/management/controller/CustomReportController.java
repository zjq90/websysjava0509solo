package com.hospital.management.controller;

import com.hospital.management.entity.CustomReport;
import com.hospital.management.service.CustomReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 自定义报表控制器
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/custom-reports")
@Tag(name = "自定义报表管理", description = "医院自定义报表的增删改查")
@CrossOrigin(origins = "*")
public class CustomReportController {

    @Autowired
    private CustomReportService customReportService;

    /**
     * 查询所有报表
     */
    @GetMapping
    @Operation(summary = "查询所有报表")
    public List<CustomReport> findAll() {
        return customReportService.findAll();
    }

    /**
     * 根据ID查询报表
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询报表")
    public ResponseEntity<CustomReport> findById(@PathVariable Long id) {
        Optional<CustomReport> report = customReportService.findById(id);
        return report.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * 新增报表
     */
    @PostMapping
    @Operation(summary = "新增报表")
    public CustomReport save(@RequestBody CustomReport customReport) {
        return customReportService.save(customReport);
    }

    /**
     * 更新报表
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新报表")
    public ResponseEntity<CustomReport> update(@PathVariable Long id, @RequestBody CustomReport customReport) {
        if (!customReportService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        customReport.setId(id);
        return ResponseEntity.ok(customReportService.update(customReport));
    }

    /**
     * 删除报表
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除报表")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!customReportService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        customReportService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据报表类型查询
     */
    @GetMapping("/type/{reportType}")
    @Operation(summary = "根据报表类型查询")
    public List<CustomReport> findByReportType(@PathVariable String reportType) {
        return customReportService.findByReportType(reportType);
    }

    /**
     * 查询所有启用的报表
     */
    @GetMapping("/enabled")
    @Operation(summary = "查询所有启用的报表")
    public List<CustomReport> findEnabledReports() {
        return customReportService.findEnabledReports();
    }

    /**
     * 查询系统预设报表
     */
    @GetMapping("/system")
    @Operation(summary = "查询系统预设报表")
    public List<CustomReport> findSystemReports() {
        return customReportService.findSystemReports();
    }

    /**
     * 根据报表名称模糊查询
     */
    @GetMapping("/search")
    @Operation(summary = "根据报表名称模糊查询")
    public List<CustomReport> findByReportNameContaining(@RequestParam String reportName) {
        return customReportService.findByReportNameContaining(reportName);
    }
}
