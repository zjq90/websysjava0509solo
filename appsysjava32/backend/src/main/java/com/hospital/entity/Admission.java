package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入院预约实体类
 * 出入院预约管理
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hos_admission")
@Schema(description = "入院预约信息")
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "入院申请ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    @Schema(description = "申请单号")
    private String applicationNo;

    @Column(nullable = false)
    @Schema(description = "患者用户ID")
    private Long userId;

    @Column(length = 50)
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(length = 50)
    @Schema(description = "科室编码")
    private String deptCode;

    @Column(length = 50)
    @Schema(description = "科室名称")
    private String deptName;

    @Column(length = 20)
    @Schema(description = "病房类型")
    private String wardType;

    @Column(length = 20)
    @Schema(description = "期望床位号")
    private String expectBedNo;

    @Column(length = 20)
    @Schema(description = "实际床位号")
    private String actualBedNo;

    @Column
    @Schema(description = "入院日期")
    private LocalDate admissionDate;

    @Column(length = 200)
    @Schema(description = "入院原因/诊断")
    private String diagnosis;

    @Column(length = 50)
    @Schema(description = "主治医生ID")
    private Long doctorId;

    @Column(length = 50)
    @Schema(description = "主治医生姓名")
    private String doctorName;

    @Column(nullable = false)
    @Schema(description = "状态：0待审核 1审核通过 2已入院 3已出院 4已取消")
    private Integer status = 0;

    @Column
    @Schema(description = "审核人ID")
    private Long auditUserId;

    @Column(length = 50)
    @Schema(description = "审核人姓名")
    private String auditUserName;

    @Column(length = 500)
    @Schema(description = "审核备注")
    private String auditRemark;

    @Column
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Column
    @Schema(description = "实际入院时间")
    private LocalDateTime actualAdmissionTime;

    @Column
    @Schema(description = "出院时间")
    private LocalDateTime dischargeTime;

    @Column(precision = 10, scale = 2)
    @Schema(description = "住院押金")
    private BigDecimal depositAmount = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Column(length = 50)
    @Schema(description = "创建人")
    private String createBy;

    @Column(length = 50)
    @Schema(description = "更新人")
    private String updateBy;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
