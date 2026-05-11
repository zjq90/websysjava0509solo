package com.hospital.appointment.controller;

import com.hospital.appointment.common.Result;
import com.hospital.appointment.entity.Schedule;
import com.hospital.appointment.entity.ScheduleSlot;
import com.hospital.appointment.security.UserPrincipal;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.appointment.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班号源控制器
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/schedules")
@Tag(name = "排班号源管理", description = "排班和号源查询接口")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("")
    @Operation(summary = "获取排班列表", description = "按日期和条件查询排班")
    public Result<List<Schedule>> getSchedules(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") @Parameter(description = "预约日期") LocalDate date,
            @RequestParam(required = false) @Parameter(description = "科室ID") Long deptId,
            @RequestParam(required = false) @Parameter(description = "医生ID") Long doctorId,
            @RequestParam(required = false) @Parameter(description = "时段: morning/afternoon/night") String timePeriod) {
        return Result.success(scheduleService.getSchedulesByDate(date, deptId, doctorId, timePeriod));
    }

    @GetMapping("/dates")
    @Operation(summary = "获取可用日期", description = "获取未来有号源的日期列表")
    public Result<List<LocalDate>> getAvailableDates() {
        return Result.success(scheduleService.getAvailableDates());
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "获取医生排班", description = "获取指定医生的排班信息")
    public Result<List<Schedule>> getDoctorSchedules(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") @Parameter(description = "预约日期") LocalDate date) {
        return Result.success(scheduleService.getDoctorSchedules(doctorId, date));
    }
}

@RestController
@RequestMapping("/slots")
@Tag(name = "号源时段管理", description = "号源时段查询和锁定接口")
class SlotController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/{scheduleId}")
    @Operation(summary = "获取时段列表", description = "获取指定排班的号源时段列表")
    public Result<List<ScheduleSlot>> getSlots(@PathVariable Long scheduleId) {
        return Result.success(appointmentService.getScheduleSlots(scheduleId));
    }

    @PostMapping("/{slotId}/lock")
    @Operation(summary = "锁定号源", description = "锁定号源10分钟，超时自动释放")
    public Result<ScheduleSlot> lockSlot(
            @PathVariable Long slotId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(appointmentService.lockSlot(slotId, userPrincipal.getUserId()));
    }

    @PostMapping("/{slotId}/unlock")
    @Operation(summary = "释放号源", description = "手动释放已锁定的号源")
    public Result<ScheduleSlot> unlockSlot(
            @PathVariable Long slotId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(appointmentService.unlockSlot(slotId, userPrincipal.getUserId()));
    }
}
