package com.agricultural.entity;

import com.agricultural.entity.enums.OrderStatus;
import com.agricultural.entity.enums.OrderType;
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
 * 订单实体类（进销存订单）
 * 支持销售订单和采购订单，与财务单关联自动生成应收应付账款
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_info")
public class Order {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String orderNo;

    /**
     * 订单类型：SALES（销售订单）、PURCHASE（采购订单）
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderType orderType;

    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus orderStatus;

    /**
     * 关联的客户/供应商ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * 订单日期
     */
    @Column(nullable = false)
    private LocalDate orderDate;

    /**
     * 发货/到货日期
     */
    @Column
    private LocalDate deliveryDate;

    /**
     * 订单总金额
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 税额
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    /**
     * 折扣金额
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * 实际应付金额
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;

    /**
     * 订单明细
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 关联的财务账款ID（自动生成）
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finance_id")
    private Finance finance;

    /**
     * 制单人
     */
    @Column(length = 50)
    private String creator;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    /**
     * 计算订单金额
     */
    public void calculateAmounts() {
        if (orderItems != null) {
            this.totalAmount = orderItems.stream()
                    .map(OrderItem::getLineTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (this.discountAmount == null) {
            this.discountAmount = BigDecimal.ZERO;
        }
        if (this.taxAmount == null) {
            this.taxAmount = BigDecimal.ZERO;
        }
        this.netAmount = this.totalAmount.add(this.taxAmount).subtract(this.discountAmount);
    }
}
