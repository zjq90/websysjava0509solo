package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 排班实体类
 * 用于管理摄影师、化妆师、选片师的档期安排
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "schedule")
@Schema(description = "排班信息")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "排班ID", example = "1")
    private Long id;

    @NotNull(message = "员工不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @Schema(description = "员工信息")
    private Employee employee;

    @NotNull(message = "排班日期不能为空")
    @Column(nullable = false)
    @Schema(description = "排班日期", example = "2024-05-15")
    private LocalDate scheduleDate;

    @NotNull(message = "开始时间不能为空")
    @Column(nullable = false)
    @Schema(description = "开始时间", example = "09:00:00")
    private LocalTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Column(nullable = false)
    @Schema(description = "结束时间", example = "18:00:00")
    private LocalTime endTime;

    @Column(length = 20)
    @Schema(description = "客户姓名", example = "李四")
    private String customerName;

    @Column(length = 50)
    @Schema(description = "客户电话", example = "13900139000")
    private String customerPhone;

    @Column(length = 100)
    @Schema(description = "拍摄主题", example = "婚纱照-海景")
    private String shootingTheme;

    @Column(nullable = false)
    @Schema(description = "是否已提醒", example = "false")
    private Boolean reminded = false;

    @Column
    @Schema(description = "提醒时间")
    private LocalDateTime remindTime;

    @Column(nullable = false, length = 20)
    @Schema(description = "排班状态", example = "CONFIRMED")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status = ScheduleStatus.CONFIRMED;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    /**
     * 排班状态枚举
     */
    public enum ScheduleStatus {
        PENDING("待确认"),
        CONFIRMED("已确认"),
        CANCELLED("已取消"),
        COMPLETED("已完成"),
        TRANSFER_REQUESTED("调班申请中");

        private final String description;

        ScheduleStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
