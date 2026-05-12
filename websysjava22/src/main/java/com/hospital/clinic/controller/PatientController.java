package com.hospital.clinic.controller;

import com.hospital.clinic.common.Result;
import com.hospital.clinic.entity.Patient;
import com.hospital.clinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者Controller
 * 患者管理相关接口
 */
@RestController
@RequestMapping("/api/patient")
@Tag(name = "患者管理", description = "患者管理相关接口")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * 查询所有患者
     */
    @GetMapping
    @Operation(summary = "查询所有患者", description = "获取所有患者列表")
    public Result<List<Patient>> findAll() {
        return Result.success(patientService.findAll());
    }

    /**
     * 根据ID查询患者
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询患者", description = "根据患者ID获取患者信息")
    public Result<Patient> findById(@Parameter(description = "患者ID") @PathVariable Long id) {
        return Result.success(patientService.findById(id));
    }

    /**
     * 根据姓名模糊查询
     */
    @GetMapping("/search")
    @Operation(summary = "根据姓名查询患者", description = "根据患者姓名模糊查询")
    public Result<List<Patient>> findByName(@Parameter(description = "患者姓名") @RequestParam String name) {
        return Result.success(patientService.findByName(name));
    }

    /**
     * 新增患者
     */
    @PostMapping
    @Operation(summary = "新增患者", description = "新增患者信息")
    public Result<Patient> save(@RequestBody Patient patient) {
        return Result.success(patientService.save(patient));
    }

    /**
     * 更新患者
     */
    @PutMapping
    @Operation(summary = "更新患者", description = "更新患者信息")
    public Result<Patient> update(@RequestBody Patient patient) {
        return Result.success(patientService.update(patient));
    }

    /**
     * 删除患者
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除患者", description = "根据ID删除患者信息")
    public Result<Void> deleteById(@Parameter(description = "患者ID") @PathVariable Long id) {
        patientService.deleteById(id);
        return Result.success();
    }
}
