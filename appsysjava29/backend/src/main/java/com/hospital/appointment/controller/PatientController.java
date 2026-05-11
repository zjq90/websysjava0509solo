package com.hospital.appointment.controller;

import com.hospital.appointment.common.Result;
import com.hospital.appointment.entity.Patient;
import com.hospital.appointment.security.UserPrincipal;
import com.hospital.appointment.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 就诊人控制器
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/patients")
@Tag(name = "就诊人管理", description = "就诊人增删改查接口")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("")
    @Operation(summary = "获取就诊人列表", description = "获取当前用户的就诊人列表")
    public Result<List<Patient>> getMyPatients(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(patientService.getUserPatients(userPrincipal.getUserId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取就诊人详情", description = "根据ID获取就诊人详情")
    public Result<Patient> getPatient(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(patientService.getPatientById(userPrincipal.getUserId(), id));
    }

    @PostMapping("")
    @Operation(summary = "添加就诊人", description = "添加新的就诊人")
    public Result<Patient> addPatient(
            @RequestBody Patient patient,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(patientService.addPatient(userPrincipal.getUserId(), patient));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新就诊人", description = "更新就诊人信息")
    public Result<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(patientService.updatePatient(userPrincipal.getUserId(), id, patient));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除就诊人", description = "删除指定的就诊人")
    public Result<Void> deletePatient(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        patientService.deletePatient(userPrincipal.getUserId(), id);
        return Result.success();
    }

    @PostMapping("/{id}/default")
    @Operation(summary = "设置默认就诊人", description = "设置指定的就诊人为默认")
    public Result<Patient> setDefaultPatient(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(patientService.setDefaultPatient(userPrincipal.getUserId(), id));
    }
}
