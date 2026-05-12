package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.Patient;
import com.hospital.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者Controller
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/patient")
@CrossOrigin
@Tag(name = "患者管理", description = "患者信息的增删改查")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    @Operation(summary = "查询所有患者")
    public Result<List<Patient>> findAll() {
        return Result.success(patientService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询患者")
    public Result<Patient> findById(@PathVariable Long id) {
        return patientService.findById(id)
                .map(Result::success)
                .orElse(Result.error("患者不存在"));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索患者")
    public Result<List<Patient>> search(@RequestParam String keyword) {
        return Result.success(patientService.search(keyword));
    }

    @PostMapping
    @Operation(summary = "新增患者")
    public Result<Patient> save(@RequestBody Patient patient) {
        return Result.success(patientService.save(patient));
    }

    @PutMapping
    @Operation(summary = "修改患者")
    public Result<Patient> update(@RequestBody Patient patient) {
        return Result.success(patientService.save(patient));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除患者")
    public Result<Void> deleteById(@PathVariable Long id) {
        patientService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询患者")
    public Result<List<Patient>> findByStatus(@PathVariable String status) {
        return Result.success(patientService.findByStatus(status));
    }
}
