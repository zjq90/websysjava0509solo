package com.hospital.appointment.controller;

import com.hospital.appointment.common.Result;
import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.entity.Payment;
import com.hospital.appointment.security.UserPrincipal;
import com.hospital.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
@Tag(name = "预约挂号管理")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("")
    @Operation(summary = "创建预约")
    public Result<Appointment> createAppointment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Map<String, Object> params) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        Long slotId = Long.valueOf(params.get("slotId").toString());
        Long patientId = Long.valueOf(params.get("patientId").toString());
        String symptoms = (String) params.get("symptoms");
        return Result.success(appointmentService.createAppointment(userPrincipal.getUserId(), slotId, patientId, symptoms));
    }

    @GetMapping("")
    @Operation(summary = "获取我的预约")
    public Result<List<Appointment>> getMyAppointments(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String status) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(appointmentService.getUserAppointments(userPrincipal.getUserId(), status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取预约详情")
    public Result<Appointment> getAppointmentDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        return Result.success(appointmentService.getAppointmentDetail(id, userPrincipal.getUserId()));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "取消预约")
    public Result<Appointment> cancelAppointment(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> params,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        String reason = params != null ? params.get("reason") : null;
        return Result.success(appointmentService.cancelAppointment(id, userPrincipal.getUserId(), reason));
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "支付预约")
    public Result<Payment> payAppointment(
            @PathVariable Long id,
            @RequestBody Map<String, String> params,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return Result.error("用户未登录");
        }
        String paymentMethod = params.get("paymentMethod");
        Boolean useInsurance = params.containsKey("useInsurance") ? 
            Boolean.valueOf(params.get("useInsurance").toString()) : false;
        String insuranceNo = params.get("insuranceNo");
        
        if (paymentMethod == null) {
            paymentMethod = "WECHAT";
        }
        return Result.success(appointmentService.payAppointment(id, userPrincipal.getUserId(), paymentMethod, useInsurance, insuranceNo));
    }
}
