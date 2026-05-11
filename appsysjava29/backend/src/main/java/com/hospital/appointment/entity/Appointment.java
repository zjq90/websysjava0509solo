package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约挂号实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "appointment")
@Schema(description = "预约挂号信息")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "预约ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "schedule_id", nullable = false)
    @Schema(description = "排班ID")
    private Long scheduleId;

    @Column(name = "slot_id")
    @Schema(description = "时段ID")
    private Long slotId;

    @Column(name = "doctor_id", nullable = false)
    @Schema(description = "医生ID")
    private Long doctorId;

    @Column(name = "dept_id", nullable = false)
    @Schema(description = "科室ID")
    private Long deptId;

    @Column(name = "appointment_no", unique = true, length = 50)
    @Schema(description = "预约单号")
    private String appointmentNo;

    @Column(name = "appointment_date")
    @Schema(description = "预约日期")
    private LocalDate appointmentDate;

    @Column(name = "slot_time", length = 20)
    @Schema(description = "预约时段")
    private String slotTime;

    @Column(name = "patient_name", length = 50)
    @Schema(description = "就诊人姓名")
    private String patientName;

    @Column(name = "patient_phone", length = 20)
    @Schema(description = "就诊人电话")
    private String patientPhone;

    @Column(name = "patient_id_card", length = 50)
    @Schema(description = "就诊人身份证号")
    private String patientIdCard;

    @Column(name = "patient_relation", length = 20)
    @Schema(description = "就诊人关系: self/family")
    private String patientRelation;

    @Column(name = "symptoms", columnDefinition = "TEXT")
    @Schema(description = "症状描述")
    private String symptoms;

    @Column(name = "consultation_fee", precision = 10, scale = 2)
    @Schema(description = "挂号费")
    private BigDecimal consultationFee;

    @Column(name = "status", length = 20)
    @Schema(description = "状态: pending/paid/confirmed/cancelled/completed")
    private String status;

    @Column(name = "cancel_reason", columnDefinition = "TEXT")
    @Schema(description = "取消原因")
    private String cancelReason;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Transient
    @Schema(description = "医生信息")
    private Doctor doctor;

    @Transient
    @Schema(description = "科室名称")
    private String deptName;

    @Transient
    @Schema(description = "支付信息")
    private Payment payment;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "pending";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
