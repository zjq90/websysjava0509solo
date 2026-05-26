package com.personal.accounting.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户实体类
 * 用于管理用户的各种账户（现金、银行卡、支付宝等）
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账户实体")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "账户ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "账户名称", example = "工商银行储蓄卡")
    private String name;

    @Column(length = 50)
    @Schema(description = "账户类型", example = "储蓄卡")
    private String accountType;

    @Column(precision = 15, scale = 2)
    @Schema(description = "初始余额", example = "10000.00")
    private BigDecimal initialBalance = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2, nullable = false)
    @Schema(description = "当前余额", example = "15000.50")
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(length = 50)
    @Schema(description = "货币类型", example = "CNY")
    private String currency = "CNY";

    @Column(length = 7)
    @Schema(description = "图标颜色", example = "#4ECDC4")
    private String color;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "账户备注", example = "工资卡")
    private String notes;

    @Column(nullable = false)
    @Schema(description = "是否包含在总资产中", example = "true")
    private Boolean includeInTotal = true;

    @Column(nullable = false)
    @Schema(description = "排序", example = "1")
    private Integer sortOrder = 0;

    @Column(nullable = false)
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled = true;

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
