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
 * 质检报告实体类
 * 
 * <p>存储种子质量检测报告信息，包括各项检测指标和总体结论。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quality_report")
public class QualityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次ID（外键）
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 报告编号
     */
    @Column(unique = true, length = 50)
    private String reportNo;

    /**
     * 检验日期
     */
    @NotNull(message = "检验日期不能为空")
    @Column(nullable = false)
    private LocalDate inspectionDate;

    /**
     * 检验机构
     */
    @Column(length = 100)
    private String inspectionOrganization;

    /**
     * 检验人员
     */
    @Column(length = 100)
    private String inspector;

    /**
     * 发芽率检测值(%)
     */
    @Column(precision = 4, scale = 1)
    private BigDecimal germinationRateTest;

    /**
     * 纯度检测值(%)
     */
    @Column(precision = 4, scale = 1)
    private BigDecimal purityTest;

    /**
     * 水分检测值(%)
     */
    @Column(precision = 4, scale = 1)
    private BigDecimal moistureTest;

    /**
     * 净度检测值(%)
     */
    @Column(precision = 4, scale = 1)
    private BigDecimal clarityTest;

    /**
     * 活力检测
     */
    @Column(length = 100)
    private String vigorTest;

    /**
     * 健康检测
     */
    @Column(length = 100)
    private String healthTest;

    /**
     * 其他检测项目
     */
    @Column(length = 500)
    private String otherTests;

    /**
     * 总体结论：QUALIFIED(合格)/UNQUALIFIED(不合格)
     */
    @NotBlank(message = "总体结论不能为空")
    @Column(nullable = false, length = 20)
    private String overallResult;

    /**
     * 检验结论
     */
    @Column(length = 500)
    private String conclusion;

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
