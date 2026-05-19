package com.petclinic.controller;

import com.petclinic.common.Result;
import com.petclinic.entity.CreditRecord;
import com.petclinic.entity.Doctor;
import com.petclinic.entity.PetOwner;
import com.petclinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理Controller
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "宠物主人、医生审核、信用分管理")
public class UserController {

    @Autowired
    private UserService userService;

    // ==================== 宠物主人管理 ====================

    @GetMapping("/owners")
    @Operation(summary = "获取所有宠物主人")
    public Result<List<PetOwner>> getAllPetOwners() {
        return Result.success(userService.getAllPetOwners());
    }

    @GetMapping("/owners/{id}")
    @Operation(summary = "根据ID获取宠物主人")
    public Result<PetOwner> getPetOwnerById(@PathVariable Long id) {
        return Result.success(userService.getPetOwnerById(id));
    }

    @GetMapping("/owners/audit-status/{status}")
    @Operation(summary = "根据实名认证状态获取宠物主人列表")
    public Result<List<PetOwner>> getPetOwnersByRealNameStatus(@PathVariable Integer status) {
        return Result.success(userService.getPetOwnersByRealNameStatus(status));
    }

    @PostMapping("/owners")
    @Operation(summary = "创建宠物主人")
    public Result<PetOwner> createPetOwner(@RequestBody PetOwner petOwner) {
        return Result.success(userService.createPetOwner(petOwner));
    }

    @PutMapping("/owners/{id}")
    @Operation(summary = "更新宠物主人")
    public Result<PetOwner> updatePetOwner(@PathVariable Long id, @RequestBody PetOwner petOwner) {
        return Result.success(userService.updatePetOwner(id, petOwner));
    }

    @PutMapping("/owners/{id}/audit")
    @Operation(summary = "实名认证审核")
    public Result<PetOwner> auditRealName(@PathVariable Long id, @RequestParam Integer status,
                                           @RequestParam(required = false, defaultValue = "1") Long operatorId) {
        return Result.success(userService.auditRealName(id, status, operatorId));
    }

    @DeleteMapping("/owners/{id}")
    @Operation(summary = "删除宠物主人")
    public Result<Void> deletePetOwner(@PathVariable Long id) {
        userService.deletePetOwner(id);
        return Result.success();
    }

    // ==================== 医生审核管理 ====================

    @GetMapping("/doctors")
    @Operation(summary = "获取所有医生")
    public Result<List<Doctor>> getAllDoctors() {
        return Result.success(userService.getAllDoctors());
    }

    @GetMapping("/doctors/{id}")
    @Operation(summary = "根据ID获取医生")
    public Result<Doctor> getDoctorById(@PathVariable Long id) {
        return Result.success(userService.getDoctorById(id));
    }

    @GetMapping("/doctors/audit-status/{status}")
    @Operation(summary = "根据审核状态获取医生列表")
    public Result<List<Doctor>> getDoctorsByAuditStatus(@PathVariable Integer status) {
        return Result.success(userService.getDoctorsByAuditStatus(status));
    }

    @PostMapping("/doctors")
    @Operation(summary = "创建医生（提交资质审核）")
    public Result<Doctor> createDoctor(@RequestBody Doctor doctor) {
        return Result.success(userService.createDoctor(doctor));
    }

    @PutMapping("/doctors/{id}")
    @Operation(summary = "更新医生信息")
    public Result<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return Result.success(userService.updateDoctor(id, doctor));
    }

    @PutMapping("/doctors/{id}/audit")
    @Operation(summary = "医生资质审核")
    public Result<Doctor> auditDoctor(@PathVariable Long id,
                                       @RequestParam Integer auditStatus,
                                       @RequestParam(required = false) String auditRemark,
                                       @RequestParam(required = false, defaultValue = "1") Long operatorId) {
        return Result.success(userService.auditDoctor(id, auditStatus, auditRemark, operatorId));
    }

    @DeleteMapping("/doctors/{id}")
    @Operation(summary = "删除医生")
    public Result<Void> deleteDoctor(@PathVariable Long id) {
        userService.deleteDoctor(id);
        return Result.success();
    }

    // ==================== 信用分管理 ====================

    @PutMapping("/credit/adjust")
    @Operation(summary = "调整用户信用分")
    public Result<CreditRecord> adjustCreditScore(
            @RequestParam Long userId,
            @RequestParam Integer userType,
            @RequestParam Integer scoreChange,
            @RequestParam String reason,
            @RequestParam(required = false) Long businessId,
            @RequestParam(required = false, defaultValue = "1") Long operatorId) {
        return Result.success(userService.adjustCreditScore(userId, userType, scoreChange, reason, businessId, operatorId));
    }

    @GetMapping("/credit/records")
    @Operation(summary = "获取用户信用记录")
    public Result<List<CreditRecord>> getCreditRecords(
            @RequestParam Long userId,
            @RequestParam Integer userType) {
        return Result.success(userService.getCreditRecords(userId, userType));
    }
}
