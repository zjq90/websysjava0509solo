package com.personal.accounting.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * 预算实体类
 * 用于管理每个分类的月度预算
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Entity
@Table(name = "budgets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"category_id", "budget_month"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "预算实体")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "预算ID", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "分类")
    private Category category;

    @Column(name = "budget_month", nullable = false)
    @Schema(description = "预算月份", example = "2024-01")
    private YearMonth budgetMonth;

    @Column(precision = 15, scale = 2, nullable = false)
    @Schema(description = "预算金额", example = "2000.00")
    private BigDecimal budgetAmount;

    @Column(name = "rollover_remaining")
    @Schema(description = "是否结转剩余预算到下月", example = "false")
    private Boolean rolloverRemaining = false;

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
