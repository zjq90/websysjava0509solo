package com.agricultural.entity;

import com.agricultural.entity.enums.FinanceStatus;
import com.agricultural.entity.enums.FinanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 财务账款实体类
 * 记录应收账款和应付账款，与订单自动关联生成
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "finance")
public class Finance {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 财务单号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String financeNo;

    /**
     * 财务类型：RECEIVABLE（应收）、PAYABLE（应付）
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FinanceType financeType;

    /**
     * 账款状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FinanceStatus status;

    /**
     * 关联的客户/供应商
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * 关联的订单（如果由订单生成）
     */
    @OneToOne(mappedBy = "finance", fetch = FetchType.LAZY)
    private Order order;

    /**
     * 账款日期
     */
    @Column(nullable = false)
    private LocalDate financeDate;

    /**
     * 到期日期
     */
    @Column
    private LocalDate dueDate;

    /**
     * 账款总金额
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 已收/已付金额
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * 剩余未结清金额
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingAmount;

    /**
     * 收款/付款记录
     */
    @OneToMany(mappedBy = "finance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancePayment> payments = new ArrayList<>();

    /**
     * 摘要说明
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

    /**
     * 更新时间
     */
    @Column
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (this.remainingAmount == null) {
            this.remainingAmount = this.totalAmount;
        }
        if (this.paidAmount == null) {
            this.paidAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    /**
     * 计算剩余金额并更新状态
     */
    public void calculateRemainingAndStatus() {
        if (this.totalAmount == null) {
            this.totalAmount = BigDecimal.ZERO;
        }
        if (this.paidAmount == null) {
            this.paidAmount = BigDecimal.ZERO;
        }
        this.remainingAmount = this.totalAmount.subtract(this.paidAmount);

        if (this.remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            this.status = FinanceStatus.SETTLED;
        } else if (this.paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.status = FinanceStatus.PARTIAL_SETTLED;
        } else {
            this.status = FinanceStatus.UNSETTLED;
        }
    }
}
