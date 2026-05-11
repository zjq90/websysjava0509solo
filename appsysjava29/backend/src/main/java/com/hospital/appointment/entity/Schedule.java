package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 号源排班实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "schedule")
@Schema(description = "号源排班信息")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "排班ID")
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    @Schema(description = "医生ID")
    private Long doctorId;

    @Column(name = "dept_id", nullable = false)
    @Schema(description = "科室ID")
    private Long deptId;

    @Column(name = "schedule_date", nullable = false)
    @Schema(description = "排班日期")
    private LocalDate scheduleDate;

    @Column(name = "time_period", length = 20)
    @Schema(description = "时段: morning/afternoon/night")
    private String timePeriod;

    @Column(name = "period_name", length = 50)
    @Schema(description = "时段名称: 上午/下午/夜间")
    private String periodName;

    @Column(name = "start_time")
    @Schema(description = "开始时间")
    private LocalTime startTime;

    @Column(name = "end_time")
    @Schema(description = "结束时间")
    private LocalTime endTime;

    @Column(name = "total_slots")
    @Schema(description = "总号源数")
    private Integer totalSlots;

    @Column(name = "booked_slots")
    @Schema(description = "已预约数")
    private Integer bookedSlots;

    @Column(name = "available_slots")
    @Schema(description = "可用号源数")
    private Integer availableSlots;

    @Column(name = "status")
    @Schema(description = "状态: 0-停诊 1-正常")
    private Integer status;

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

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = 1;
        if (bookedSlots == null) bookedSlots = 0;
        if (availableSlots == null && totalSlots != null) availableSlots = totalSlots;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
