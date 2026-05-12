package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.Hospitalization;
import com.hospital.service.HospitalizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 住院记录Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/hospitalization")
@CrossOrigin
@Tag(name = "住院管理", description = "住院登记、出院办理等功能")
public class HospitalizationController {

    @Autowired
    private HospitalizationService hospitalizationService;

    @GetMapping
    @Operation(summary = "查询所有住院记录")
    public Result<List<Hospitalization>> findAll() {
        return Result.success(hospitalizationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询住院记录")
    public Result<Hospitalization> findById(@PathVariable Long id) {
        return hospitalizationService.findById(id)
                .map(Result::success)
                .orElse(Result.error("住院记录不存在"));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "根据患者ID查询住院记录")
    public Result<List<Hospitalization>> findByPatientId(@PathVariable Long patientId) {
        return Result.success(hospitalizationService.findByPatientId(patientId));
    }

    @GetMapping("/hospitalized")
    @Operation(summary = "查询所有在院患者")
    public Result<List<Hospitalization>> findAllHospitalized() {
        return Result.success(hospitalizationService.findAllHospitalized());
    }

    @PostMapping("/admission")
    @Operation(summary = "办理入院登记")
    public Result<Hospitalization> admission(@RequestBody Hospitalization hospitalization) {
        try {
            return Result.success(hospitalizationService.admission(hospitalization));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/discharge/{id}")
    @Operation(summary = "办理出院")
    public Result<Hospitalization> discharge(@PathVariable Long id, @RequestParam String dischargeDiagnosis) {
        Hospitalization result = hospitalizationService.discharge(id, dischargeDiagnosis);
        if (result == null) {
            return Result.error("住院记录不存在");
        }
        return Result.success(result);
    }

    @PutMapping
    @Operation(summary = "修改住院记录")
    public Result<Hospitalization> update(@RequestBody Hospitalization hospitalization) {
        return Result.success(hospitalizationService.save(hospitalization));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除住院记录")
    public Result<Void> deleteById(@PathVariable Long id) {
        hospitalizationService.deleteById(id);
        return Result.success();
    }
}
