package com.hospital.finance.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医保结算实体类
 * 记录与医保系统的对接结算信息
 */
@Data
@Entity
@Table(name = "insurance_settlement")
@Schema(description = "医保结算记录")
public class InsuranceSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "结算记录ID")
    private Long id;

    @Column(name = "settlement_no", unique = true, nullable = false, length = 50)
    @Schema(description = "结算单号")
    private String settlementNo;

    @Column(name = "patient_id", nullable = false)
    @Schema(description = "患者ID")
    private Long patientId;

    @Column(name = "patient_name", length = 100)
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(name = "insurance_no", length = 50)
    @Schema(description = "医保编号")
    private String insuranceNo;

    @Column(name = "insurance_type", length = 50)
    @Schema(description = "医保类型")
    private String insuranceType;

    @Column(name = "charge_type", length = 20)
    @Schema(description = "收费类型(门诊、住院)")
    private String chargeType;

    @Column(name = "charge_id")
    @Schema(description = "对应的收费记录ID")
    private Long chargeId;

    @Column(name = "charge_no", length = 50)
    @Schema(description = "对应的收费单号")
    private String chargeNo;

    @Column(name = "total_amount", precision = 10, scale = 2)
    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Column(name = "within_scope_amount", precision = 10, scale = 2)
    @Schema(description = "医保范围内金额")
    private BigDecimal withinScopeAmount;

    @Column(name = "outside_scope_amount", precision = 10, scale = 2)
    @Schema(description = "医保范围外金额")
    private BigDecimal outsideScopeAmount;

    @Column(name = "insurance_pay_amount", precision = 10, scale = 2)
    @Schema(description = "医保支付金额")
    private BigDecimal insurancePayAmount;

    @Column(name = "self_pay_amount", precision = 10, scale = 2)
    @Schema(description = "个人自付金额")
    private BigDecimal selfPayAmount;

    @Column(name = "status", length = 20)
    @Schema(description = "结算状态(待结算、已结算、已撤销)")
    private String status;

    @Column(name = "settlement_time")
    @Schema(description = "结算时间")
    private LocalDateTime settlementTime;

    @Column(name = "operator", length = 100)
    @Schema(description = "操作员")
    private String operator;

    @Column(name = "insurance_response", columnDefinition = "TEXT")
    @Schema(description = "医保系统返回信息")
    private String insuranceResponse;

    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
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