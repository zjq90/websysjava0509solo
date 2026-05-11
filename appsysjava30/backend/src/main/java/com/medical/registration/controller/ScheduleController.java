package com.medical.registration.controller;

import com.medical.registration.common.Result;
import com.medical.registration.dto.ScheduleVO;
import com.medical.registration.entity.Department;
import com.medical.registration.entity.Doctor;
import com.medical.registration.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "号源管理", description = "科室、医生、号源查询接口")
@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Operation(summary = "获取所有科室")
    @GetMapping("/departments")
    public Result<List<Department>> getDepartments() {
        return Result.success(scheduleService.getAllDepartments());
    }
    
    @Operation(summary = "获取科室下的医生")
    @GetMapping("/departments/{deptCode}/doctors")
    public Result<List<Doctor>> getDoctorsByDept(@PathVariable String deptCode) {
        return Result.success(scheduleService.getDoctorsByDept(deptCode));
    }
    
    @Operation(summary = "获取所有医生")
    @GetMapping("/doctors")
    public Result<List<Doctor>> getAllDoctors() {
        return Result.success(scheduleService.getAllDoctors());
    }
    
    @Operation(summary = "按科室和日期查询号源")
    @GetMapping("/departments/{deptCode}/date/{date}")
    public Result<List<ScheduleVO>> getSchedulesByDeptAndDate(
            @PathVariable String deptCode,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return Result.success(scheduleService.getSchedulesByDeptAndDate(deptCode, date));
    }
    
    @Operation(summary = "按医生和日期范围查询号源")
    @GetMapping("/doctors/{doctorId}")
    public Result<List<ScheduleVO>> getSchedulesByDoctor(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(scheduleService.getSchedulesByDoctorAndDateRange(doctorId, startDate, endDate));
    }
    
    @Operation(summary = "按医生和具体日期查询号源")
    @GetMapping("/doctors/{doctorId}/date/{date}")
    public Result<List<ScheduleVO>> getSchedulesByDoctorAndDate(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return Result.success(scheduleService.getSchedulesByDoctorAndDate(doctorId, date));
    }
}
