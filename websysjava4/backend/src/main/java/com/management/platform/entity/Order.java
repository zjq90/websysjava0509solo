package com.management.platform.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 存储订单的基本信息
 */
@Entity
@Table(name = "orders")
public class Order {

    /**
     * 订单ID，主键自增
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
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 商品ID
     */
    @Column(nullable = false)
    private Long productId;

    /**
     * 购买数量
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 订单总金额
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 订单状态：pending（待支付）, paid（已支付）, completed（已完成）, cancelled（已取消）
     */
    @Column(length = 20)
    private String status = "paid";

    /**
     * 购买时间（用于统计购买时段）
     */
    @Column(nullable = false)
    private LocalDateTime purchaseTime;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (purchaseTime == null) {
            purchaseTime = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getPurchaseTime() { return purchaseTime; }
    public void setPurchaseTime(LocalDateTime purchaseTime) { this.purchaseTime = purchaseTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
