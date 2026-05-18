package com.psyconsult.controller;

import com.psyconsult.common.Result;
import com.psyconsult.entity.Appointment;
import com.psyconsult.repository.AppointmentRepository;
import com.psyconsult.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@Api(tags = "预约管理")
@CrossOrigin
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/counselor/{counselorId}")
    @ApiOperation("获取咨询师的预约")
    public Result<List<Appointment>> getAppointmentsByCounselor(@PathVariable Long counselorId) {
        return Result.success(appointmentRepository.findByCounselorId(counselorId));
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("获取用户的预约")
    public Result<List<Appointment>> getAppointmentsByUser(@PathVariable Long userId) {
        return Result.success(appointmentRepository.findByUserId(userId));
    }

    @GetMapping("/counselor/{counselorId}/date/{date}")
    @ApiOperation("获取咨询师指定日期的预约")
    public Result<List<Appointment>> getAppointmentsByDate(
            @PathVariable Long counselorId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return Result.success(appointmentRepository.findByCounselorIdAndDate(counselorId, date));
    }

    @PostMapping
    @ApiOperation("创建预约")
    public Result<Appointment> createAppointment(@RequestBody Appointment appointment) {
        appointment.setStatus("pending");
        Appointment saved = appointmentRepository.save(appointment);
        if (saved.getScheduleId() != null) {
            scheduleService.incrementAppointmentCount(saved.getScheduleId());
        }
        return Result.success(saved);
    }

    @PutMapping("/{id}/status")
    @ApiOperation("更新预约状态")
    public Result<Appointment> updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus(status);
            return Result.success(appointmentRepository.save(appointment));
        }).orElse(Result.error("预约不存在"));
    }

    @PutMapping("/{id}/cancel")
    @ApiOperation("取消预约")
    public Result<Appointment> cancelAppointment(@PathVariable Long id, @RequestParam String reason) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus("cancelled");
            appointment.setCancelReason(reason);
            return Result.success(appointmentRepository.save(appointment));
        }).orElse(Result.error("预约不存在"));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取预约详情")
    public Result<Appointment> getAppointmentById(@PathVariable Long id) {
        return Result.success(appointmentRepository.findById(id).orElse(null));
    }
}
