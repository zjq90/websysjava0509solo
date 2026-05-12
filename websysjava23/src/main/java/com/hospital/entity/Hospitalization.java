package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 住院记录实体类
 * 存储患者住院信息
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hospitalization")
@Schema(description = "住院记录")
public class Hospitalization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "住院ID")
    private Long id;

    @Column(name = "hospital_no", unique = true, nullable = false, length = 32)
    @Schema(description = "住院号")
    private String hospitalNo;

    @Column(name = "patient_id", nullable = false)
    @Schema(description = "患者ID")
    private Long patientId;

    @Transient
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(name = "bed_id")
    @Schema(description = "床位ID")
    private Long bedId;

    @Transient
    @Schema(description = "床位号")
    private String bedNo;

    @Transient
    @Schema(description = "病区名称")
    private String wardName;

    @Column(name = "department", length = 64)
    @Schema(description = "科室")
    private String department;

    @Column(name = "doctor_id")
    @Schema(description = "主治医生ID")
    private Long doctorId;

    @Transient
    @Schema(description = "主治医生姓名")
    private String doctorName;

    @Column(name = "nurse_id")
    @Schema(description = "责任护士ID")
    private Long nurseId;

    @Transient
    @Schema(description = "责任护士姓名")
    private String nurseName;

    @Column(name = "admission_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "入院时间")
    private LocalDateTime admissionDate;

    @Column(name = "discharge_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "出院时间")
    private LocalDateTime dischargeDate;

    @Column(name = "admission_diagnosis", length = 512)
    @Schema(description = "入院诊断")
    private String admissionDiagnosis;

    @Column(name = "discharge_diagnosis", length = 512)
    @Schema(description = "出院诊断")
    private String dischargeDiagnosis;

    @Column(name = "total_amount", precision = 10, scale = 2)
    @Schema(description = "总费用")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", precision = 10, scale = 2)
    @Schema(description = "已付费用")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "status", length = 32)
    @Schema(description = "住院状态：待入院/住院中/已出院/已取消")
    private String status = "待入院";

    @Column(name = "remark", length = 1024)
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
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
