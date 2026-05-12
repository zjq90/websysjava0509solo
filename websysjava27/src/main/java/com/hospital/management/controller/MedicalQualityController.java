package com.hospital.management.controller;

import com.hospital.management.entity.MedicalQuality;
import com.hospital.management.service.MedicalQualityService;
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
 * 医疗质量控制器
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/medical-quality")
@Tag(name = "医疗质量管理", description = "医院医疗质量的增删改查及统计分析")
@CrossOrigin(origins = "*")
public class MedicalQualityController {

    @Autowired
    private MedicalQualityService medicalQualityService;

    /**
     * 查询所有医疗质量数据
     */
    @GetMapping
    @Operation(summary = "查询所有医疗质量数据")
    public List<MedicalQuality> findAll() {
        return medicalQualityService.findAll();
    }

    /**
     * 根据ID查询医疗质量数据
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询医疗质量数据")
    public ResponseEntity<MedicalQuality> findById(@PathVariable Long id) {
        Optional<MedicalQuality> quality = medicalQualityService.findById(id);
        return quality.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * 新增医疗质量数据
     */
    @PostMapping
    @Operation(summary = "新增医疗质量数据")
    public MedicalQuality save(@RequestBody MedicalQuality medicalQuality) {
        return medicalQualityService.save(medicalQuality);
    }

    /**
     * 更新医疗质量数据
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新医疗质量数据")
    public ResponseEntity<MedicalQuality> update(@PathVariable Long id, @RequestBody MedicalQuality medicalQuality) {
        if (!medicalQualityService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        medicalQuality.setId(id);
        return ResponseEntity.ok(medicalQualityService.update(medicalQuality));
    }

    /**
     * 删除医疗质量数据
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除医疗质量数据")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!medicalQualityService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        medicalQualityService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据日期范围查询医疗质量数据
     */
    @GetMapping("/date-range")
    @Operation(summary = "根据日期范围查询医疗质量数据")
    public List<MedicalQuality> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return medicalQualityService.findByDateRange(startDate, endDate);
    }

    /**
     * 根据科室ID和日期范围查询医疗质量数据
     */
    @GetMapping("/department-date-range")
    @Operation(summary = "根据科室ID和日期范围查询医疗质量数据")
    public List<MedicalQuality> findByDepartmentAndDateRange(
            @RequestParam Long departmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return medicalQualityService.findByDepartmentAndDateRange(departmentId, startDate, endDate);
    }

    /**
     * 获取医疗质量统计汇总
     */
    @GetMapping("/statistics-summary")
    @Operation(summary = "获取医疗质量统计汇总")
    public Map<String, Object> getStatisticsSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return medicalQualityService.getStatisticsSummary(startDate, endDate);
    }

    /**
     * 按科室分组统计
     */
    @GetMapping("/aggregate-by-department")
    @Operation(summary = "按科室分组统计医疗质量数据")
    public List<Object[]> aggregateByDepartment(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return medicalQualityService.aggregateByDepartment(startDate, endDate);
    }
}
