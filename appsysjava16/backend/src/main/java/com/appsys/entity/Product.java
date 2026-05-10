package com.appsys.entity;

import com.appsys.validation.BatchNumber;
import com.appsys.validation.ExpiryDate;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产品实体�? * 管理产品信息，包括批次编号、保质期、发芽率等核心字�? * 
 * @author appsys-team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Schema(description = "产品信息")
public class Product {

    /**
     * 产品ID，自增主�?     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "产品ID", example = "1")
    private Long id;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    @Column(nullable = false, length = 100)
    @Schema(description = "产品名称", example = "玉米种子A�?)
    private String name;

    /**
     * 批次编号：必须为8位数�?字母组合，全局唯一
     */
    @BatchNumber
    @Column(nullable = false, unique = true, length = 8)
    @Schema(description = "批次编号�?位数�?字母组合�?, example = "2026AB01")
    private String batchNumber;

    /**
     * 产品规格
     */
    @Column(length = 100)
    @Schema(description = "产品规格", example = "10kg/�?)
    private String specification;

    /**
     * 保质期：不得早于当前日期+6个月
     */
    @ExpiryDate
    @NotNull(message = "保质期不能为�?)
    @Column(nullable = false)
    @Schema(description = "保质期（不得早于当前日期+6个月�?, example = "2027-12-31")
    private LocalDate expiryDate;

    /**
     * 发芽率：数值范�?-100%，精度保�?位小�?     */
    @DecimalMin(value = "0.0", message = "发芽率不能低�?%")
    @DecimalMax(value = "100.0", message = "发芽率不能超�?00%")
    @Column(precision = 4, scale = 1, nullable = false)
    @Schema(description = "发芽率（0-100%�?位小数）", example = "95.5")
    private BigDecimal germinationRate;

    /**
     * 原价（基础价格�?     */
    @NotNull(message = "产品价格不能为空")
    @Column(precision = 10, scale = 2, nullable = false)
    @Schema(description = "产品原价", example = "299.00")
    private BigDecimal basePrice;

    /**
     * 批发价格
     */
    @Column(precision = 10, scale = 2)
    @Schema(description = "批发价格", example = "250.00")
    private BigDecimal wholesalePrice;

    /**
     * 种子类别
     */
    @Column(length = 50)
    @Schema(description = "种子类别", example = "常规�?)
    private String seedCategory;

    /**
     * 纯度�?-100%
     */
    @DecimalMin(value = "0.0", message = "纯度不能低于0%")
    @DecimalMax(value = "100.0", message = "纯度不能超过100%")
    @Column(precision = 4, scale = 1)
    @Schema(description = "纯度�?-100%�?, example = "98.0")
    private BigDecimal purity;

    /**
     * 水分�?-100%
     */
    @DecimalMin(value = "0.0", message = "水分不能低于0%")
    @DecimalMax(value = "100.0", message = "水分不能超过100%")
    @Column(precision = 4, scale = 1)
    @Schema(description = "水分�?-100%�?, example = "13.0")
    private BigDecimal moisture;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "备注", example = "优质种子")
    private String notes;

    /**
     * 库存数量
     */
    @Column(nullable = false)
    @Schema(description = "库存数量", example = "1000")
    private Integer stockQuantity = 0;

    /**
     * 产品描述
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "产品描述", example = "优质玉米种子，适合北方种植")
    private String description;

    /**
     * 产品分类
     */
    @Column(length = 50)
    @Schema(description = "产品分类", example = "玉米种子")
    private String category;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    /**
     * 实体持久化前自动设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 实体更新前自动设置更新时�?     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
