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
 * 田间管理记录实体类
 * 
 * <p>存储田间管理的详细记录，包括播种、施肥、打药、浇水、收获等操作。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "field_management")
public class FieldManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次ID（外键）
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 操作日期
     */
    @NotNull(message = "操作日期不能为空")
    @Column(nullable = false)
    private LocalDate operationDate;

    /**
     * 操作类型：
     * SOWING(播种) / FERTILIZER(施肥) / PESTICIDE(打药) / 
     * WATERING(浇水) / HARVEST(收获)
     */
    @NotBlank(message = "操作类型不能为空")
    @Column(nullable = false, length = 50)
    private String operationType;

    /**
     * 操作名称
     */
    @NotBlank(message = "操作名称不能为空")
    @Column(nullable = false, length = 100)
    private String operationName;

    /**
     * 物资名称(农药/化肥)
     */
    @Column(length = 200)
    private String substanceName;

    /**
     * 用量
     */
    @Column(length = 100)
    private String dosage;

    /**
     * 浓度
     */
    @Column(length = 100)
    private String concentration;

    /**
     * 施用方法
     */
    @Column(length = 100)
    private String applicationMethod;

    /**
     * 天气情况
     */
    @Column(length = 100)
    private String weatherCondition;

    /**
     * 温度(℃)
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    /**
     * 操作人员
     */
    @Column(length = 100)
    private String operator;

    /**
     * 种植地点
     */
    @Column(length = 200)
    private String location;

    /**
     * 土壤类型
     */
    @Column(length = 100)
    private String soilType;

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
