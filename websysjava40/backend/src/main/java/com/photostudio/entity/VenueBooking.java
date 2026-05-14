package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 场地预约实体类
 * 用于管理影棚、外景地等场地的预约记录
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "venue_booking")
@Schema(description = "场地预约信息")
public class VenueBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "预约ID", example = "1")
    private Long id;

    @NotNull(message = "场地不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    @Schema(description = "场地信息")
    private Venue venue;

    @NotNull(message = "预约日期不能为空")
    @Column(nullable = false)
    @Schema(description = "预约日期", example = "2024-05-15")
    private LocalDate bookingDate;

    @NotNull(message = "开始时间不能为空")
    @Column(nullable = false)
    @Schema(description = "开始时间", example = "09:00:00")
    private LocalTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Column(nullable = false)
    @Schema(description = "结束时间", example = "12:00:00")
    private LocalTime endTime;

    @Column(length = 20)
    @Schema(description = "客户姓名", example = "李四")
    private String customerName;

    @Column(length = 50)
    @Schema(description = "客户电话", example = "13900139000")
    private String customerPhone;

    @Column(length = 100)
    @Schema(description = "拍摄主题", example = "婚纱照-欧式")
    private String shootingTheme;

    @Column(nullable = false, length = 20)
    @Schema(description = "预约状态", example = "CONFIRMED")
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.CONFIRMED;

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
     * 预约状态枚举
     */
    public enum BookingStatus {
        PENDING("待确认"),
        CONFIRMED("已确认"),
        CANCELLED("已取消"),
        COMPLETED("已完成");

        private final String description;

        BookingStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
