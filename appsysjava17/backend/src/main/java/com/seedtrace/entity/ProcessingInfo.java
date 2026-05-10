package com.seedtrace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 加工信息实体类
 * 
 * <p>存储种子加工流程的参数信息，包括清选、分级、干燥、包装等步骤。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "processing_info")
public class ProcessingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次ID（外键）
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 加工日期
     */
    @NotNull(message = "加工日期不能为空")
    @Column(nullable = false)
    private LocalDate processDate;

    /**
     * 加工步骤：
     * CLEANING(清选) / GRADING(分级) / DRYING(干燥) / PACKAGING(包装)
     */
    @NotBlank(message = "加工步骤不能为空")
    @Column(nullable = false, length = 50)
    private String processStep;

    /**
     * 设备名称
     */
    @Column(length = 100)
    private String equipmentName;

    /**
     * 设备型号
     */
    @Column(length = 100)
    private String equipmentModel;

    /**
     * 加工参数(温度、转速、时间等)
     */
    @Column(length = 500)
    private String processParameter;

    /**
     * 投入数量(kg)
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal inputQuantity;

    /**
     * 产出数量(kg)
     */
    @Column(precision = 12, scale = 2)
    private BigDecimal outputQuantity;

    /**
     * 操作人员
     */
    @Column(length = 100)
    private String operator;

    /**
     * 加工厂
     */
    @Column(length = 100)
    private String processingPlant;

    /**
     * 质检结果：PASS(通过)/FAIL(失败)
     */
    @Column(length = 20)
    private String qualityCheckResult;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
