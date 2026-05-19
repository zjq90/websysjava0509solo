package com.petclinic.controller;

import com.petclinic.common.Result;
import com.petclinic.entity.Department;
import com.petclinic.entity.Medicine;
import com.petclinic.entity.Schedule;
import com.petclinic.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * 医院管理Controller
 */
@RestController
@RequestMapping("/hospital")
@Tag(name = "医院管理", description = "科室、排班、药品管理")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    // ==================== 科室管理 ====================

    @GetMapping("/departments")
    @Operation(summary = "获取所有科室")
    public Result<List<Department>> getAllDepartments() {
        return Result.success(hospitalService.getAllDepartments());
    }

    @GetMapping("/departments/{id}")
    @Operation(summary = "根据ID获取科室")
    public Result<Department> getDepartmentById(@PathVariable Long id) {
        return Result.success(hospitalService.getDepartmentById(id));
    }

    @PostMapping("/departments")
    @Operation(summary = "创建科室")
    public Result<Department> createDepartment(@RequestBody Department department) {
        return Result.success(hospitalService.createDepartment(department));
    }

    @PutMapping("/departments/{id}")
    @Operation(summary = "更新科室")
    public Result<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return Result.success(hospitalService.updateDepartment(id, department));
    }

    @DeleteMapping("/departments/{id}")
    @Operation(summary = "删除科室")
    public Result<Void> deleteDepartment(@PathVariable Long id) {
        hospitalService.deleteDepartment(id);
        return Result.success();
    }

    // ==================== 排班管理 ====================

    @GetMapping("/schedules")
    @Operation(summary = "获取排班列表")
    public Result<List<Schedule>> getSchedules(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return Result.success(hospitalService.getSchedulesByDateRange(startDate, endDate));
        }
        return Result.success(hospitalService.getSchedulesByDate(LocalDate.now()));
    }

    @GetMapping("/schedules/{id}")
    @Operation(summary = "根据ID获取排班")
    public Result<Schedule> getScheduleById(@PathVariable Long id) {
        return Result.success(hospitalService.getScheduleById(id));
    }

    @PostMapping("/schedules")
    @Operation(summary = "创建排班")
    public Result<Schedule> createSchedule(@RequestBody Schedule schedule) {
        return Result.success(hospitalService.createSchedule(schedule));
    }

    @PostMapping("/schedules/batch")
    @Operation(summary = "批量创建排班")
    public Result<List<Schedule>> batchCreateSchedule(@RequestBody List<Schedule> schedules) {
        return Result.success(hospitalService.batchCreateSchedule(schedules));
    }

    @PostMapping("/schedules/import")
    @Operation(summary = "批量导入排班（Excel）")
    public Result<String> importSchedule(@RequestParam("file") MultipartFile file) throws Exception {
        return Result.success(hospitalService.importScheduleFromExcel(file));
    }

    @PutMapping("/schedules/{id}")
    @Operation(summary = "更新排班")
    public Result<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        return Result.success(hospitalService.updateSchedule(id, schedule));
    }

    @DeleteMapping("/schedules/{id}")
    @Operation(summary = "删除排班")
    public Result<Void> deleteSchedule(@PathVariable Long id) {
        hospitalService.deleteSchedule(id);
        return Result.success();
    }

    // ==================== 药品管理 ====================

    @GetMapping("/medicines")
    @Operation(summary = "获取所有药品")
    public Result<List<Medicine>> getAllMedicines() {
        return Result.success(hospitalService.getAllMedicines());
    }

    @GetMapping("/medicines/warning")
    @Operation(summary = "获取库存预警药品")
    public Result<List<Medicine>> getWarningMedicines() {
        return Result.success(hospitalService.getWarningMedicines());
    }

    @GetMapping("/medicines/{id}")
    @Operation(summary = "根据ID获取药品")
    public Result<Medicine> getMedicineById(@PathVariable Long id) {
        return Result.success(hospitalService.getMedicineById(id));
    }

    @PostMapping("/medicines")
    @Operation(summary = "创建药品")
    public Result<Medicine> createMedicine(@RequestBody Medicine medicine) {
        return Result.success(hospitalService.createMedicine(medicine));
    }

    @PutMapping("/medicines/{id}")
    @Operation(summary = "更新药品")
    public Result<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        return Result.success(hospitalService.updateMedicine(id, medicine));
    }

    @DeleteMapping("/medicines/{id}")
    @Operation(summary = "删除药品")
    public Result<Void> deleteMedicine(@PathVariable Long id) {
        hospitalService.deleteMedicine(id);
        return Result.success();
    }
}
