package com.hospital.finance.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 住院收费实体类
 * 记录患者住院期间的各项收费信息
 */
@Data
@Entity
@Table(name = "inpatient_charge")
@Schema(description = "住院收费记录")
public class InpatientCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "收费记录ID")
    private Long id;

    @Column(name = "charge_no", unique = true, nullable = false, length = 50)
    @Schema(description = "收费单号")
    private String chargeNo;

    @Column(name = "patient_id", nullable = false)
    @Schema(description = "患者ID")
    private Long patientId;

    @Column(name = "patient_name", length = 100)
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(name = "admission_no", length = 50)
    @Schema(description = "住院号")
    private String admissionNo;

    @Column(name = "department", length = 100)
    @Schema(description = "住院科室")
    private String department;

    @Column(name = "bed_no", length = 20)
    @Schema(description = "床位号")
    private String bedNo;

    @Column(name = "doctor_name", length = 100)
    @Schema(description = "主治医生")
    private String doctorName;

    @Column(name = "total_amount", precision = 10, scale = 2)
    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Column(name = "deposit_amount", precision = 10, scale = 2)
    @Schema(description = "押金金额")
    private BigDecimal depositAmount;

    @Column(name = "self_pay_amount", precision = 10, scale = 2)
    @Schema(description = "自付金额")
    private BigDecimal selfPayAmount;

    @Column(name = "insurance_amount", precision = 10, scale = 2)
    @Schema(description = "医保报销金额")
    private BigDecimal insuranceAmount;

    @Column(name = "status", length = 20)
    @Schema(description = "收费状态(住院中、已结算、已退费)")
    private String status;

    @Column(name = "payment_method", length = 50)
    @Schema(description = "支付方式(现金、微信、支付宝、银行卡、医保)")
    private String paymentMethod;

    @Column(name = "charge_time")
    @Schema(description = "收费时间")
    private LocalDateTime chargeTime;

    @Column(name = "operator", length = 100)
    @Schema(description = "收费员")
    private String operator;

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