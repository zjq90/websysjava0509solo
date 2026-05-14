package com.photostudio.controller;

import com.photostudio.entity.Schedule;
import com.photostudio.entity.Schedule.ScheduleStatus;
import com.photostudio.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 排班管理控制器
 * 提供排班管理相关的REST API接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "排班管理", description = "排班信息的增删改查及冲突检测")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    @Operation(summary = "获取所有排班", description = "获取系统中所有排班信息")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取排班", description = "根据排班ID获取详细信息")
    public ResponseEntity<Schedule> getScheduleById(
            @Parameter(description = "排班ID") @PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "根据员工获取排班", description = "获取指定员工的排班列表")
    public ResponseEntity<List<Schedule>> getSchedulesByEmployee(
            @Parameter(description = "员工ID") @PathVariable Long employeeId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByEmployee(employeeId));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "根据日期获取排班", description = "获取指定日期的排班列表")
    public ResponseEntity<List<Schedule>> getSchedulesByDate(
            @Parameter(description = "日期") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getSchedulesByDate(date));
    }

    @GetMapping("/range")
    @Operation(summary = "根据日期范围获取排班", description = "获取指定日期范围内的排班列表")
    public ResponseEntity<List<Schedule>> getSchedulesByDateRange(
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.getSchedulesByDateRange(startDate, endDate));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取排班", description = "获取指定状态的排班列表")
    public ResponseEntity<List<Schedule>> getSchedulesByStatus(
            @Parameter(description = "排班状态") @PathVariable ScheduleStatus status) {
        return ResponseEntity.ok(scheduleService.getSchedulesByStatus(status));
    }

    @GetMapping("/unreminded")
    @Operation(summary = "获取未提醒的排班", description = "获取需要提醒的排班列表")
    public ResponseEntity<List<Schedule>> getUnremindedSchedules() {
        return ResponseEntity.ok(scheduleService.getUnremindedSchedules());
    }

    @PostMapping
    @Operation(summary = "创建排班", description = "创建新的排班信息")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleService.createSchedule(schedule));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新排班", description = "更新排班信息")
    public ResponseEntity<Schedule> updateSchedule(
            @Parameter(description = "排班ID") @PathVariable Long id,
            @RequestBody Schedule schedule) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, schedule));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除排班", description = "删除排班信息")
    public ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "排班ID") @PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "完成排班", description = "标记排班为已完成")
    public ResponseEntity<Schedule> completeSchedule(
            @Parameter(description = "排班ID") @PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.completeSchedule(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消排班", description = "取消排班")
    public ResponseEntity<Schedule> cancelSchedule(
            @Parameter(description = "排班ID") @PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.cancelSchedule(id));
    }

    @PutMapping("/{id}/remind")
    @Operation(summary = "标记已提醒", description = "标记排班已发送提醒")
    public ResponseEntity<Schedule> markAsReminded(
            @Parameter(description = "排班ID") @PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.markAsReminded(id));
    }

    @PostMapping("/check-conflict")
    @Operation(summary = "检查排班冲突", description = "检查员工在指定时间是否有排班冲突")
    public ResponseEntity<Map<String, Boolean>> checkConflict(@RequestBody Map<String, Object> request) {
        Long employeeId = Long.parseLong(request.get("employeeId").toString());
        LocalDate date = LocalDate.parse(request.get("date").toString());
        String startTimeStr = request.get("startTime").toString();
        String endTimeStr = request.get("endTime").toString();
        
        java.time.LocalTime startTime = java.time.LocalTime.parse(startTimeStr);
        java.time.LocalTime endTime = java.time.LocalTime.parse(endTimeStr);
        
        boolean hasConflict = scheduleService.checkConflict(employeeId, date, startTime, endTime);
        return ResponseEntity.ok(Collections.singletonMap("hasConflict", hasConflict));
    }
}
