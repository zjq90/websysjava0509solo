package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 服装预约实体类
 * 用于管理服装的预约和使用记录
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "costume_booking")
@Schema(description = "服装预约信息")
public class CostumeBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "预约ID", example = "1")
    private Long id;

    @NotNull(message = "服装不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "costume_id", nullable = false)
    @Schema(description = "服装信息")
    private Costume costume;

    @Column(length = 20)
    @Schema(description = "客户姓名", example = "李四")
    private String customerName;

    @Column(length = 50)
    @Schema(description = "客户电话", example = "13900139000")
    private String customerPhone;

    @NotNull(message = "使用日期不能为空")
    @Column(nullable = false)
    @Schema(description = "使用日期", example = "2024-05-15")
    private LocalDate useDate;

    @Column
    @Schema(description = "归还日期", example = "2024-05-16")
    private LocalDate returnDate;

    @Column(nullable = false, length = 20)
    @Schema(description = "预约状态", example = "BORROWED")
    @Enumerated(EnumType.STRING)
    private CostumeBookingStatus status = CostumeBookingStatus.BORROWED;

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
     * 服装预约状态枚举
     */
    public enum CostumeBookingStatus {
        RESERVED("已预约"),
        BORROWED("已借出"),
        RETURNED("已归还"),
        CANCELLED("已取消");

        private final String description;

        CostumeBookingStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
