package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 生命体征实体类
 * 存储患者生命体征数据
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "vital_signs")
@Schema(description = "生命体征")
public class VitalSigns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @Column(name = "hospitalization_id", nullable = false)
    @Schema(description = "住院ID")
    private Long hospitalizationId;

    @Column(name = "patient_id", nullable = false)
    @Schema(description = "患者ID")
    private Long patientId;

    @Transient
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(name = "temperature", precision = 5, scale = 2)
    @Schema(description = "体温(℃)")
    private java.math.BigDecimal temperature;

    @Column(name = "pulse")
    @Schema(description = "脉搏(次/分)")
    private Integer pulse;

    @Column(name = "respiration")
    @Schema(description = "呼吸(次/分)")
    private Integer respiration;

    @Column(name = "systolic_pressure")
    @Schema(description = "收缩压(mmHg)")
    private Integer systolicPressure;

    @Column(name = "diastolic_pressure")
    @Schema(description = "舒张压(mmHg)")
    private Integer diastolicPressure;

    @Column(name = "oxygen_saturation", precision = 5, scale = 2)
    @Schema(description = "血氧饱和度(%)")
    private java.math.BigDecimal oxygenSaturation;

    @Column(name = "blood_glucose", precision = 5, scale = 2)
    @Schema(description = "血糖(mmol/L)")
    private java.math.BigDecimal bloodGlucose;

    @Column(name = "pain_score")
    @Schema(description = "疼痛评分(0-10)")
    private Integer painScore;

    @Column(name = "consciousness", length = 32)
    @Schema(description = "意识状态：清醒/嗜睡/昏睡/昏迷")
    private String consciousness;

    @Column(name = "nurse_id")
    @Schema(description = "测量护士ID")
    private Long nurseId;

    @Transient
    @Schema(description = "护士姓名")
    private String nurseName;

    @Column(name = "record_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "记录时间")
    private LocalDateTime recordTime;

    @Column(name = "remark", length = 512)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (recordTime == null) {
            recordTime = LocalDateTime.now();
        }
    }
}
