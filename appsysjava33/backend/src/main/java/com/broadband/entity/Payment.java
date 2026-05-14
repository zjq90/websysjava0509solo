package com.broadband.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体类
 * 管理用户支付记录
 * 
 * @author broadband
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "biz_payment")
@Schema(description = "支付记录")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "支付ID")
    private Long id;

    @Schema(description = "支付单号")
    @Column(length = 50, nullable = false, unique = true)
    private String paymentNo;

    @Schema(description = "用户ID")
    @Column(nullable = false)
    private Long userId;

    @Schema(description = "关联账单ID")
    private Long billId;

    @Schema(description = "关联工单ID")
    private Long workOrderId;

    @Schema(description = "支付类型: 1-账单缴费 2-新装付费 3-套餐续费 4-增值服务购买")
    @Column(nullable = false)
    private Integer type;

    @Schema(description = "支付方式: 1-微信支付 2-支付宝 3-银联 4-对公转账")
    @Column(nullable = false)
    private Integer payMethod;

    @Schema(description = "第三方支付单号")
    @Column(length = 100)
    private String thirdPartyNo;

    @Schema(description = "支付金额")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Schema(description = "支付状态: 0-待支付 1-支付中 2-支付成功 3-支付失败 4-已退款")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer status = 0;

    @Schema(description = "支付完成时间")
    private LocalDateTime payTime;

    @Schema(description = "支付失败原因")
    @Column(length = 500)
    private String failReason;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "退款原因")
    @Column(length = 500)
    private String refundReason;

    @Schema(description = "银行名称(对公转账)")
    @Column(length = 100)
    private String bankName;

    @Schema(description = "银行账号(对公转账)")
    @Column(length = 100)
    private String bankAccount;

    @Schema(description = "转账凭证路径")
    @Column(length = 512)
    private String transferProof;

    @Schema(description = "备注")
    @Column(length = 500)
    private String remark;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

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
