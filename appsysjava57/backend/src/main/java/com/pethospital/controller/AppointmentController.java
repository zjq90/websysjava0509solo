package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Appointment;
import com.pethospital.entity.Doctor;
import com.pethospital.entity.DoctorSchedule;
import com.pethospital.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 预约挂号Controller
 * 提供科室选择、医生排班查询、预约改签/取消等API接口
 * 
 * @author Pet Hospital Team
 */
@RestController
@RequestMapping("/appointments")
@Tag(name = "预约挂号", description = "科室选择、医生排班、预约管理等接口")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 获取科室列表
     */
    @GetMapping("/departments")
    @Operation(summary = "获取科室列表", description = "获取所有科室信息")
    public Result<List<Map<String, Object>>> getDepartments() {
        return Result.success(appointmentService.getDepartments());
    }

    /**
     * 获取科室医生列表
     */
    @GetMapping("/doctors/{department}")
    @Operation(summary = "获取科室医生", description = "获取指定科室的医生列表")
    public Result<List<Doctor>> getDoctorsByDepartment(
            @Parameter(description = "科室名称") @PathVariable String department) {
        return Result.success(appointmentService.getDoctorsByDepartment(department));
    }

    /**
     * 获取医生排班
     */
    @GetMapping("/schedules/{doctorId}")
    @Operation(summary = "获取医生排班", description = "获取指定医生的排班信息")
    public Result<List<DoctorSchedule>> getDoctorSchedules(
            @Parameter(description = "医生ID") @PathVariable Long doctorId) {
        return Result.success(appointmentService.getDoctorSchedules(doctorId));
    }

    /**
     * 创建预约
     */
    @PostMapping
    @Operation(summary = "创建预约", description = "创建新的预约挂号")
    public Result<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return Result.success(appointmentService.createAppointment(appointment));
    }

    /**
     * 取消预约
     */
    @PutMapping("/{appointmentId}/cancel")
    @Operation(summary = "取消预约", description = "取消指定的预约")
    public Result<Appointment> cancelAppointment(
            @Parameter(description = "预约ID") @PathVariable Long appointmentId,
            @Parameter(description = "取消原因") @RequestParam String reason) {
        return Result.success(appointmentService.cancelAppointment(appointmentId, reason));
    }

    /**
     * 改签预约
     */
    @PutMapping("/{appointmentId}/reschedule")
    @Operation(summary = "改签预约", description = "修改预约时间")
    public Result<Appointment> rescheduleAppointment(
            @Parameter(description = "预约ID") @PathVariable Long appointmentId,
            @Parameter(description = "新预约时间") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newTime) {
        return Result.success(appointmentService.rescheduleAppointment(appointmentId, newTime));
    }

    /**
     * 获取用户预约列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户预约列表", description = "获取指定用户的所有预约记录")
    public Result<List<Appointment>> getUserAppointments(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(appointmentService.getUserAppointments(userId));
    }

    /**
     * 获取预约详情
     */
    @GetMapping("/{appointmentId}")
    @Operation(summary = "获取预约详情", description = "根据预约ID获取预约详细信息")
    public Result<Appointment> getAppointmentById(
            @Parameter(description = "预约ID") @PathVariable Long appointmentId) {
        return Result.success(appointmentService.getAppointmentById(appointmentId));
    }
}
