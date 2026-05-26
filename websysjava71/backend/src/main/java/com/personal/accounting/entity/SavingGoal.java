package com.personal.accounting.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 储蓄目标实体类
 * 用于管理用户的储蓄目标
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Entity
@Table(name = "saving_goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "储蓄目标实体")
public class SavingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "目标ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "目标名称", example = "旅行基金")
    private String name;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "目标描述", example = "存够5万元去欧洲旅行")
    private String description;

    @Column(precision = 15, scale = 2, nullable = false)
    @Schema(description = "目标金额", example = "50000.00")
    private BigDecimal targetAmount;

    @Column(precision = 15, scale = 2, nullable = false)
    @Schema(description = "当前已存金额", example = "15000.00")
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Column(name = "target_date")
    @Schema(description = "目标完成日期")
    private LocalDate targetDate;

    @Column(length = 7)
    @Schema(description = "图标颜色", example = "#FFD93D")
    private String color;

    @Column(length = 50)
    @Schema(description = "图标名称", example = "travel")
    private String icon;

    @Column
    @Schema(description = "是否已完成", example = "false")
    private Boolean completed = false;

    @Column(name = "completed_at")
    @Schema(description = "完成时间")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
