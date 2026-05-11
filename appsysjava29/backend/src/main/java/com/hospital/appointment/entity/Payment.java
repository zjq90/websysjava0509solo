package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "payment")
@Schema(description = "支付记录信息")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "支付ID")
    private Long id;

    @Column(name = "payment_no", unique = true, length = 50)
    @Schema(description = "支付单号")
    private String paymentNo;

    @Column(name = "appointment_id", nullable = false)
    @Schema(description = "预约ID")
    private Long appointmentId;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    @Schema(description = "支付金额")
    private BigDecimal amount;

    @Column(name = "insurance_amount", precision = 10, scale = 2)
    @Schema(description = "医保报销金额")
    private BigDecimal insuranceAmount;

    @Column(name = "self_pay_amount", precision = 10, scale = 2)
    @Schema(description = "自付金额")
    private BigDecimal selfPayAmount;

    @Column(name = "pay_method", length = 20)
    @Schema(description = "支付方式: wechat/alipay/unionpay/insurance")
    private String payMethod;

    @Column(name = "pay_status", length = 20)
    @Schema(description = "支付状态: pending/paid/refunded/failed")
    private String payStatus;

    @Column(name = "insurance_no", length = 50)
    @Schema(description = "医保电子凭证号")
    private String insuranceNo;

    @Column(name = "insurance_data", columnDefinition = "TEXT")
    @Schema(description = "医保结算数据")
    private String insuranceData;

    @Column(name = "third_party_no", length = 100)
    @Schema(description = "第三方支付单号")
    private String thirdPartyNo;

    @Column(name = "paid_time")
    @Schema(description = "支付时间")
    private LocalDateTime paidTime;

    @Column(name = "fail_reason", columnDefinition = "TEXT")
    @Schema(description = "失败原因")
    private String failReason;

    @Column(name = "refund_reason", columnDefinition = "TEXT")
    @Schema(description = "退款原因")
    private String refundReason;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (payStatus == null) payStatus = "pending";
        if (insuranceAmount == null) insuranceAmount = BigDecimal.ZERO;
        if (selfPayAmount == null) selfPayAmount = amount;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
