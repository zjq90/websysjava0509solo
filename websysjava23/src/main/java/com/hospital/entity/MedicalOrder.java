package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医嘱实体类
 * 存储医生开具的医嘱信息
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "medical_order")
@Schema(description = "医嘱")
public class MedicalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "医嘱ID")
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false, length = 32)
    @Schema(description = "医嘱编号")
    private String orderNo;

    @Column(name = "hospitalization_id", nullable = false)
    @Schema(description = "住院ID")
    private Long hospitalizationId;

    @Column(name = "patient_id", nullable = false)
    @Schema(description = "患者ID")
    private Long patientId;

    @Transient
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(name = "order_type", length = 32)
    @Schema(description = "医嘱类型：长期/临时")
    private String orderType = "长期";

    @Column(name = "category", length = 64)
    @Schema(description = "医嘱分类：药品/检查/治疗/护理/手术/其他")
    private String category;

    @Column(name = "name", length = 128)
    @Schema(description = "医嘱名称")
    private String name;

    @Column(name = "content", length = 1024)
    @Schema(description = "医嘱内容")
    private String content;

    @Column(name = "dosage", length = 128)
    @Schema(description = "剂量")
    private String dosage;

    @Column(name = "frequency", length = 64)
    @Schema(description = "频次")
    private String frequency;

    @Column(name = "route", length = 64)
    @Schema(description = "给药途径")
    private String route;

    @Column(name = "price", precision = 10, scale = 2)
    @Schema(description = "单价")
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "quantity")
    @Schema(description = "数量")
    private Integer quantity = 1;

    @Column(name = "total_amount", precision = 10, scale = 2)
    @Schema(description = "总金额")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "doctor_id")
    @Schema(description = "开具医生ID")
    private Long doctorId;

    @Transient
    @Schema(description = "医生姓名")
    private String doctorName;

    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Column(name = "execute_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "执行时间")
    private LocalDateTime executeTime;

    @Column(name = "execute_nurse_id")
    @Schema(description = "执行护士ID")
    private Long executeNurseId;

    @Transient
    @Schema(description = "执行护士姓名")
    private String executeNurseName;

    @Column(name = "status", length = 32)
    @Schema(description = "状态：待执行/执行中/已执行/已停止/已作废")
    private String status = "待执行";

    @Column(name = "remark", length = 512)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
