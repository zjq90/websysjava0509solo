package com.hospital.clinic.controller;

import com.hospital.clinic.common.Result;
import com.hospital.clinic.entity.Doctor;
import com.hospital.clinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生Controller
 * 医生相关API接口
 */
@RestController
@RequestMapping("/api/doctor")
@Tag(name = "医生管理", description = "医生管理相关接口")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 查询所有医生
     */
    @GetMapping
    @Operation(summary = "查询所有医生", description = "获取所有医生列表")
    public Result<List<Doctor>> findAll() {
        return Result.success(doctorService.findAll());
    }

    /**
     * 根据ID查询医生
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询医生", description = "根据医生ID获取医生信息")
    public Result<Doctor> findById(@Parameter(description = "医生ID") @PathVariable Long id) {
        return Result.success(doctorService.findById(id));
    }

    /**
     * 根据医生工号查询
     */
    @GetMapping("/no/{doctorNo}")
    @Operation(summary = "根据工号查询医生", description = "根据医生工号获取医生信息")
    public Result<Doctor> findByDoctorNo(@Parameter(description = "医生工号") @PathVariable String doctorNo) {
        return Result.success(doctorService.findByDoctorNo(doctorNo));
    }

    /**
     * 根据姓名模糊查询
     */
    @GetMapping("/search")
    @Operation(summary = "根据姓名查询医生", description = "根据医生姓名模糊查询")
    public Result<List<Doctor>> findByName(@Parameter(description = "医生姓名") @RequestParam String name) {
        return Result.success(doctorService.findByName(name));
    }

    /**
     * 新增医生
     */
    @PostMapping
    @Operation(summary = "新增医生", description = "新增医生信息")
    public Result<Doctor> save(@RequestBody Doctor doctor) {
        return Result.success(doctorService.save(doctor));
    }

    /**
     * 更新医生
     */
    @PutMapping
    @Operation(summary = "更新医生", description = "更新医生信息")
    public Result<Doctor> update(@RequestBody Doctor doctor) {
        return Result.success(doctorService.update(doctor));
    }

    /**
     * 删除医生
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除医生", description = "根据ID删除医生信息")
    public Result<Void> deleteById(@Parameter(description = "医生ID") @PathVariable Long id) {
        doctorService.deleteById(id);
        return Result.success();
    }
}
