package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.VitalSigns;
import com.hospital.service.VitalSignsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生命体征Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/vital-signs")
@CrossOrigin
@Tag(name = "生命体征管理", description = "患者生命体征数据录入和查询")
public class VitalSignsController {

    @Autowired
    private VitalSignsService vitalSignsService;

    @GetMapping
    @Operation(summary = "查询所有生命体征记录")
    public Result<List<VitalSigns>> findAll() {
        return Result.success(vitalSignsService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询生命体征")
    public Result<VitalSigns> findById(@PathVariable Long id) {
        return vitalSignsService.findById(id)
                .map(Result::success)
                .orElse(Result.error("记录不存在"));
    }

    @GetMapping("/hospitalization/{hospitalizationId}")
    @Operation(summary = "根据住院ID查询生命体征")
    public Result<List<VitalSigns>> findByHospitalizationId(@PathVariable Long hospitalizationId) {
        return Result.success(vitalSignsService.findByHospitalizationId(hospitalizationId));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "根据患者ID查询生命体征")
    public Result<List<VitalSigns>> findByPatientId(@PathVariable Long patientId) {
        return Result.success(vitalSignsService.findByPatientId(patientId));
    }

    @PostMapping
    @Operation(summary = "录入生命体征")
    public Result<VitalSigns> save(@RequestBody VitalSigns vitalSigns) {
        return Result.success(vitalSignsService.save(vitalSigns));
    }

    @PutMapping
    @Operation(summary = "修改生命体征")
    public Result<VitalSigns> update(@RequestBody VitalSigns vitalSigns) {
        return Result.success(vitalSignsService.save(vitalSigns));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除生命体征记录")
    public Result<Void> deleteById(@PathVariable Long id) {
        vitalSignsService.deleteById(id);
        return Result.success();
    }
}
