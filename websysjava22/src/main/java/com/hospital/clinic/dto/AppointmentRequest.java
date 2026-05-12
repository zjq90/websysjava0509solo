package com.hospital.clinic.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 预约请求DTO
 * 用于接收前端传来的预约数据
 */
@Data
public class AppointmentRequest {

    private Long patientId;

    private String patientName;

    private Long doctorId;

    private Long departmentId;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private BigDecimal consultationFee;
}
