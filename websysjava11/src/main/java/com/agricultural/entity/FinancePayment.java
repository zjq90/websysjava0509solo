package com.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 财务收款/付款记录实体类
 * 记录每笔账款的收款或付款明细
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "finance_payment")
public class FinancePayment {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 付款单号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String paymentNo;

    /**
     * 关联的财务账款
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finance_id", nullable = false)
    private Finance finance;

    /**
     * 收付款日期
     */
    @Column(nullable = false)
    private LocalDate paymentDate;

    /**
     * 收付款金额
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * 支付方式：CASH（现金）、BANK（银行转账）、CHECK（支票）、OTHER（其他）
     */
    @Column(nullable = false, length = 20)
    private String paymentMethod;

    /**
     * 银行名称
     */
    @Column(length = 100)
    private String bankName;

    /**
     * 交易号/支票号
     */
    @Column(length = 50)
    private String transactionNo;

    /**
     * 经办人
     */
    @Column(length = 50)
    private String handler;

    /**
     * 摘要
     */
    @Column(length = 200)
    private String summary;

    /**
     * 备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
