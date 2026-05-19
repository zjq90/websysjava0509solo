package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Medicine;
import com.pethospital.entity.MedicationReminder;
import com.pethospital.entity.Prescription;
import com.pethospital.service.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 药品购买Controller
 * 提供处方药购买、非处方药自选、用药提醒等API接口
 * 
 * @author Pet Hospital Team
 */
@RestController
@RequestMapping("/medicines")
@Tag(name = "药品购买", description = "药品浏览、处方验证、用药提醒等接口")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    /**
     * 获取药品分类列表
     */
    @GetMapping("/categories")
    @Operation(summary = "获取药品分类", description = "获取所有药品分类信息")
    public Result<List<Map<String, Object>>> getMedicineCategories() {
        return Result.success(medicineService.getMedicineCategories());
    }

    /**
     * 获取药品列表
     */
    @GetMapping
    @Operation(summary = "获取药品列表", description = "根据分类或关键词搜索药品")
    public Result<List<Medicine>> getMedicines(
            @Parameter(description = "药品分类") @RequestParam(required = false) String category,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        return Result.success(medicineService.getMedicines(category, keyword));
    }

    /**
     * 获取药品详情
     */
    @GetMapping("/{medicineId}")
    @Operation(summary = "获取药品详情", description = "根据药品ID获取药品详细信息")
    public Result<Medicine> getMedicineById(
            @Parameter(description = "药品ID") @PathVariable Long medicineId) {
        return Result.success(medicineService.getMedicineById(medicineId));
    }

    /**
     * 获取用户处方列表
     */
    @GetMapping("/prescriptions/user/{userId}")
    @Operation(summary = "获取处方列表", description = "获取用户的所有处方记录")
    public Result<List<Prescription>> getUserPrescriptions(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(medicineService.getUserPrescriptions(userId));
    }

    /**
     * 获取处方详情
     */
    @GetMapping("/prescriptions/{prescriptionId}")
    @Operation(summary = "获取处方详情", description = "根据处方ID获取处方详细信息")
    public Result<Prescription> getPrescriptionById(
            @Parameter(description = "处方ID") @PathVariable Long prescriptionId) {
        return Result.success(medicineService.getPrescriptionById(prescriptionId));
    }

    /**
     * 验证处方
     */
    @PostMapping("/prescriptions/{prescriptionId}/validate")
    @Operation(summary = "验证处方", description = "验证处方有效性并标记为已使用")
    public Result<Boolean> validatePrescription(
            @Parameter(description = "处方ID") @PathVariable Long prescriptionId) {
        return Result.success(medicineService.validatePrescription(prescriptionId));
    }

    /**
     * 添加用药提醒
     */
    @PostMapping("/reminders")
    @Operation(summary = "添加用药提醒", description = "创建新的用药提醒")
    public Result<MedicationReminder> addMedicationReminder(@RequestBody MedicationReminder reminder) {
        return Result.success(medicineService.addMedicationReminder(reminder));
    }

    /**
     * 获取用户用药提醒
     */
    @GetMapping("/reminders/user/{userId}")
    @Operation(summary = "获取用药提醒", description = "获取用户的所有用药提醒")
    public Result<List<MedicationReminder>> getUserMedicationReminders(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(medicineService.getUserMedicationReminders(userId));
    }

    /**
     * 更新用药提醒
     */
    @PutMapping("/reminders/{reminderId}")
    @Operation(summary = "更新用药提醒", description = "修改用药提醒信息")
    public Result<MedicationReminder> updateMedicationReminder(
            @Parameter(description = "提醒ID") @PathVariable Long reminderId,
            @RequestBody MedicationReminder reminder) {
        reminder.setId(reminderId);
        return Result.success(medicineService.updateMedicationReminder(reminder));
    }

    /**
     * 删除用药提醒
     */
    @DeleteMapping("/reminders/{reminderId}")
    @Operation(summary = "删除用药提醒", description = "删除指定的用药提醒")
    public Result<Void> deleteMedicationReminder(
            @Parameter(description = "提醒ID") @PathVariable Long reminderId) {
        medicineService.deleteMedicationReminder(reminderId);
        return Result.success();
    }

    /**
     * 多设备同步提醒
     */
    @PostMapping("/reminders/sync")
    @Operation(summary = "同步提醒到多设备", description = "将用药提醒同步到多个设备")
    public Result<Map<String, Object>> syncRemindersToDevices(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "设备ID列表") @RequestParam String deviceIds) {
        return Result.success(medicineService.syncRemindersToDevices(userId, deviceIds));
    }
}
