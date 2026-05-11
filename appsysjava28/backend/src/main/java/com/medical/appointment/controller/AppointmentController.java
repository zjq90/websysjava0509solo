package com.medical.appointment.controller;

import com.medical.appointment.common.ApiResponse;
import com.medical.appointment.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "预约接口")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @ApiOperation("创建预约")
    @PostMapping
    public ApiResponse<Map<String, Object>> createAppointment(@RequestBody Map<String, Object> requestBody,
                                                              HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        Long patientId = requestBody.get("patientId") != null ? 
                Long.valueOf(requestBody.get("patientId").toString()) : null;
        Long doctorId = Long.valueOf(requestBody.get("doctorId").toString());
        Long scheduleId = requestBody.get("scheduleId") != null ? 
                Long.valueOf(requestBody.get("scheduleId").toString()) : null;
        String symptoms = (String) requestBody.get("symptoms");
        String remark = (String) requestBody.get("remark");

        try {
            Map<String, Object> appointment = appointmentService.createAppointment(
                    userId, patientId, doctorId, scheduleId, symptoms, remark);
            return ApiResponse.success("预约创建成功", appointment);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation("获取预约列表")
    @GetMapping
    public ApiResponse<List<Map<String, Object>>> getAppointmentList(
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Map<String, Object>> appointments = appointmentService.getUserAppointments(userId, status);
        return ApiResponse.success(appointments);
    }

    @ApiOperation("获取预约详情")
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getAppointmentDetail(@PathVariable Long id,
                                                                  HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Object> detail = appointmentService.getAppointmentDetail(userId, id);
        return ApiResponse.success(detail);
    }

    @ApiOperation("支付预约")
    @PostMapping("/{id}/pay")
    public ApiResponse<String> payAppointment(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        try {
            appointmentService.payAppointment(userId, id);
            return ApiResponse.success("支付成功", null);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation("取消预约")
    @PostMapping("/{id}/cancel")
    public ApiResponse<String> cancelAppointment(@PathVariable Long id,
                                                 @RequestBody(required = false) Map<String, String> requestBody,
                                                 HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String reason = requestBody != null ? requestBody.get("reason") : null;
        
        try {
            appointmentService.cancelAppointment(userId, id, reason);
            return ApiResponse.success("取消成功", null);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation("创建评价")
    @PostMapping("/{id}/review")
    public ApiResponse<Map<String, Object>> createReview(@PathVariable Long id,
                                                          @RequestBody Map<String, Object> requestBody,
                                                          HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        Double rating = Double.valueOf(requestBody.get("rating").toString());
        String comment = (String) requestBody.get("comment");
        String tags = (String) requestBody.get("tags");
        Boolean isAnonymous = (Boolean) requestBody.getOrDefault("isAnonymous", false);

        try {
            Map<String, Object> review = appointmentService.createReview(
                    userId, id, rating, comment, tags, isAnonymous);
            return ApiResponse.success("评价成功", review);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
