package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Holiday;
import com.pethospital.entity.Schedule;
import com.pethospital.entity.ScheduleChangeRequest;
import com.pethospital.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
@Tag(name = "排班管理", description = "医生排班、调班、休息日管理等接口")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "获取医生排班")
    public Result<List<Schedule>> getDoctorSchedule(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(scheduleService.getDoctorSchedule(doctorId, startDate, endDate));
    }

    @GetMapping
    @Operation(summary = "获取所有排班")
    public Result<List<Schedule>> getAllSchedule(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(scheduleService.getAllSchedule(startDate, endDate));
    }

    @PostMapping
    @Operation(summary = "添加排班")
    public Result<Schedule> addSchedule(@RequestBody Schedule schedule) {
        return Result.success(scheduleService.addSchedule(schedule));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除排班")
    public Result<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return Result.success();
    }

    @GetMapping("/change-request/doctor/{doctorId}")
    @Operation(summary = "获取医生调班申请")
    public Result<List<ScheduleChangeRequest>> getChangeRequestsByDoctor(@PathVariable Long doctorId) {
        return Result.success(scheduleService.getChangeRequestsByDoctor(doctorId));
    }

    @GetMapping("/change-request/pending")
    @Operation(summary = "获取待审批调班申请")
    public Result<List<ScheduleChangeRequest>> getPendingChangeRequests() {
        return Result.success(scheduleService.getPendingChangeRequests());
    }

    @PostMapping("/change-request")
    @Operation(summary = "提交调班申请")
    public Result<ScheduleChangeRequest> createChangeRequest(@RequestBody ScheduleChangeRequest request) {
        return Result.success(scheduleService.createChangeRequest(request));
    }

    @PutMapping("/change-request/{id}/approve")
    @Operation(summary = "审批通过调班申请")
    public Result<ScheduleChangeRequest> approveChangeRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> params) {
        Long approverId = Long.valueOf(params.get("approverId").toString());
        String notes = params.get("notes") != null ? params.get("notes").toString() : null;
        ScheduleChangeRequest request = scheduleService.approveChangeRequest(id, approverId, notes);
        if (request != null) {
            return Result.success(request);
        }
        return Result.error("申请不存在");
    }

    @PutMapping("/change-request/{id}/reject")
    @Operation(summary = "驳回调班申请")
    public Result<ScheduleChangeRequest> rejectChangeRequest(
            @PathVariable Long id,
            @RequestBody Map<String, String> params) {
        Long approverId = Long.valueOf(params.get("approverId").toString());
        String notes = params.get("notes") != null ? params.get("notes").toString() : null;
        ScheduleChangeRequest request = scheduleService.rejectChangeRequest(id, approverId, notes);
        if (request != null) {
            return Result.success(request);
        }
        return Result.error("申请不存在");
    }

    @GetMapping("/holiday/doctor/{doctorId}")
    @Operation(summary = "获取医生休息日")
    public Result<List<Holiday>> getDoctorHolidays(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(scheduleService.getDoctorHolidays(doctorId, startDate, endDate));
    }

    @PostMapping("/holiday")
    @Operation(summary = "添加休息日")
    public Result<Holiday> addHoliday(@RequestBody Holiday holiday) {
        return Result.success(scheduleService.addHoliday(holiday));
    }

    @DeleteMapping("/holiday/{id}")
    @Operation(summary = "删除休息日")
    public Result<Void> deleteHoliday(@PathVariable Long id) {
        scheduleService.deleteHoliday(id);
        return Result.success();
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有排班列表")
    public Result<List<Schedule>> getAllSchedules() {
        return Result.success(scheduleService.getAllSchedules());
    }

    @GetMapping("/holiday/all")
    @Operation(summary = "获取所有休息日")
    public Result<List<Holiday>> getAllHolidays() {
        return Result.success(scheduleService.getAllHolidays());
    }
}
