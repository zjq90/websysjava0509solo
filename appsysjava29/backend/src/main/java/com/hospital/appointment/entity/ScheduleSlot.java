package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 号源时段实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "schedule_slot")
@Schema(description = "号源时段信息")
public class ScheduleSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "时段ID")
    private Long id;

    @Column(name = "schedule_id", nullable = false)
    @Schema(description = "排班ID")
    private Long scheduleId;

    @Column(name = "slot_time", nullable = false, length = 20)
    @Schema(description = "时段: 09:00-09:15")
    private String slotTime;

    @Column(name = "slot_start", nullable = false)
    @Schema(description = "开始时间")
    private LocalTime slotStart;

    @Column(name = "slot_end", nullable = false)
    @Schema(description = "结束时间")
    private LocalTime slotEnd;

    @Column(name = "slot_index")
    @Schema(description = "时段序号")
    private Integer slotIndex;

    @Column(name = "status", length = 20)
    @Schema(description = "状态: available/locked/booked")
    private String status;

    @Column(name = "lock_user_id")
    @Schema(description = "锁定用户ID")
    private Long lockUserId;

    @Column(name = "lock_expire_time")
    @Schema(description = "锁号过期时间")
    private LocalDateTime lockExpireTime;

    @Column(name = "appointment_id")
    @Schema(description = "关联预约ID")
    private Long appointmentId;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Transient
    @Schema(description = "剩余锁定秒数")
    private Long remainingLockSeconds;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "available";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
