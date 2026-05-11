package com.hospital.controller;

import com.hospital.annotation.OperationLog;
import com.hospital.common.Result;
import com.hospital.dto.AppointmentDTO;
import com.hospital.dto.AppointmentVO;
import com.hospital.entity.Appointment;
import com.hospital.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约控制器
 * 处理预约挂号相关请求
 * 
 * @author hospital
 * @version 1.0.0
 */
@RestController
@RequestMapping("/patient/appointments")
@Tag(name = "预约管理", description = "预约挂号相关接口")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 创建预约
     */
    @PostMapping
    @Operation(summary = "创建预约", description = "患者预约挂号")
    @OperationLog(value = "预约挂号", operationType = "APPOINTMENT_CREATE", module = "APPOINTMENT")
    public Result<AppointmentVO> createAppointment(
            @AuthenticationPrincipal Long userId,
            @RequestBody AppointmentDTO dto) {
        Appointment appointment = appointmentService.createAppointment(userId, dto);
        AppointmentVO vo = appointmentService.getAppointmentVOById(appointment.getId());
        return Result.success("预约成功", vo);
    }

    /**
     * 获取我的预约列表
     */
    @GetMapping
    @Operation(summary = "获取我的预约", description = "获取当前用户的预约列表")
    public Result<List<AppointmentVO>> getMyAppointments(@AuthenticationPrincipal Long userId) {
        return Result.success(appointmentService.getPatientAppointmentVOs(userId));
    }

    /**
     * 获取预约详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取预约详情", description = "根据ID获取预约详细信息")
    public Result<AppointmentVO> getAppointmentById(@PathVariable Long id) {
        return Result.success(appointmentService.getAppointmentVOById(id));
    }

    /**
     * 取消预约（POST）
     */
    @PostMapping("/{id}/cancel")
    @Operation(summary = "取消预约", description = "取消已预约的挂号")
    @OperationLog(value = "取消预约", operationType = "APPOINTMENT_CANCEL", module = "APPOINTMENT")
    public Result<?> cancelAppointmentPost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        appointmentService.cancelAppointment(id, userId, reason != null ? reason : "用户主动取消");
        return Result.success("取消成功");
    }

    /**
     * 取消预约（PUT）
     */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约", description = "取消已预约的挂号")
    @OperationLog(value = "取消预约", operationType = "APPOINTMENT_CANCEL", module = "APPOINTMENT")
    public Result<?> cancelAppointmentPut(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestBody(required = false) java.util.Map<String, Object> body) {
        String reason = body != null && body.get("reason") != null ? body.get("reason").toString() : "用户主动取消";
        appointmentService.cancelAppointment(id, userId, reason);
        return Result.success("取消成功");
    }

    /**
     * 模拟支付（POST）
     */
    @PostMapping("/{id}/pay")
    @Operation(summary = "模拟支付", description = "模拟支付挂号费")
    @OperationLog(value = "支付挂号费", operationType = "PAYMENT", module = "PAYMENT")
    public Result<?> payAppointmentPost(
            @PathVariable Long id,
            @RequestParam(defaultValue = "WECHAT") String paymentMethod) {
        appointmentService.payAppointment(id, paymentMethod);
        return Result.success("支付成功");
    }

    /**
     * 模拟支付（PUT）
     */
    @PutMapping("/{id}/pay")
    @Operation(summary = "模拟支付", description = "模拟支付挂号费")
    @OperationLog(value = "支付挂号费", operationType = "PAYMENT", module = "PAYMENT")
    public Result<?> payAppointmentPut(
            @PathVariable Long id,
            @RequestBody(required = false) java.util.Map<String, Object> body) {
        String paymentMethod = body != null && body.get("paymentMethod") != null ? 
                body.get("paymentMethod").toString() : "WECHAT";
        appointmentService.payAppointment(id, paymentMethod);
        return Result.success("支付成功");
    }
}
