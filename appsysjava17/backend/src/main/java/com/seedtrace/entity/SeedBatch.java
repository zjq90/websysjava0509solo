package com.seedtrace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 种子批次实体类
 * 
 * <p>存储种子批次的核心信息，是整个追溯系统的核心实体。</p>
 * 
 * <p>数据校验规则：
 * <ul>
 *   <li>批次编号：8位数字+字母组合，全局唯一</li>
 *   <li>保质期：不得早于当前日期+6个月</li>
 *   <li>发芽率：0-100%，精度保留1位小数</li>
 * </ul>
 * </p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seed_batch")
public class SeedBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次编号：8位数字+字母组合，全局唯一
     * 例如：SD2024A1
     */
    @NotBlank(message = "批次编号不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{8}$", message = "批次编号必须为8位数字+字母组合")
    @Column(unique = true, nullable = false, length = 8)
    private String batchCode;

    /**
     * 种子名称
     */
    @NotBlank(message = "种子名称不能为空")
    @Column(nullable = false, length = 100)
    private String seedName;

    /**
     * 种子品种
     */
    @Column(length = 100)
    private String seedVariety;

    /**
     * 发芽率：0-100%，精度保留1位小数
     */
    @NotNull(message = "发芽率不能为空")
    @DecimalMin(value = "0.0", message = "发芽率不能小于0%")
    @DecimalMax(value = "100.0", message = "发芽率不能大于100%")
    @Digits(integer = 3, fraction = 1, message = "发芽率精度只能保留1位小数")
    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal germinationRate;

    /**
     * 纯度(%)
     */
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    @Column(precision = 4, scale = 1)
    private BigDecimal purity;

    /**
     * 水分含量(%)
     */
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    @Column(precision = 4, scale = 1)
    private BigDecimal moistureContent;

    /**
     * 生产日期
     */
    @NotNull(message = "生产日期不能为空")
    @Column(nullable = false)
    private LocalDate productionDate;

    /**
     * 保质期(到期日期)
     * 校验规则：不得早于当前日期+6个月
     */
    @NotNull(message = "保质期不能为空")
    @Column(nullable = false)
    private LocalDate shelfLife;

    /**
     * 数量(kg)
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 0, message = "数量不能为负数")
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 单价
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 状态：ACTIVE(活跃)/INACTIVE(停用)
     */
    @Column(length = 20)
    @Builder.Default
    private String status = "ACTIVE";

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    @Column(length = 50)
    private String createdBy;

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
