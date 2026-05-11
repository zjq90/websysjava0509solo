package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 押金补缴实体类
 * 住院押金支付记录
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hos_deposit_payment")
@Schema(description = "押金补缴信息")
public class DepositPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "支付记录ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "支付订单号")
    private String orderNo;

    @Column(nullable = false)
    @Schema(description = "入院申请ID")
    private Long admissionId;

    @Column(nullable = false)
    @Schema(description = "患者用户ID")
    private Long userId;

    @Column(length = 50)
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(precision = 10, scale = 2)
    @Schema(description = "支付金额")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(length = 20)
    @Schema(description = "支付方式：微信 支付宝 银行卡 现金")
    private String paymentMethod;

    @Column(length = 50)
    @Schema(description = "第三方支付流水号")
    private String transactionId;

    @Column(nullable = false)
    @Schema(description = "支付状态：0待支付 1支付中 2支付成功 3支付失败 4已退款")
    private Integer status = 0;

    @Column
    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

    @Column(length = 500)
    @Schema(description = "支付备注")
    private String remark;

    @Column(length = 200)
    @Schema(description = "失败原因")
    private String failReason;

    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Column(length = 50)
    @Schema(description = "创建人")
    private String createBy;

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
