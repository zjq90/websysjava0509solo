package com.flower.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@Entity
@Table(name = "sys_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String orderNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal payAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal freightAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    private Long couponId;

    private Integer pointsUsed;

    @Column(precision = 10, scale = 2)
    private BigDecimal pointsAmount;

    @Column(length = 20)
    private String status;

    @Column(length = 20)
    private String payMethod;

    private LocalDateTime payTime;

    @Column(length = 20)
    private String deliveryType;

    @Column(length = 50)
    private String deliveryTime;

    @Column(length = 200)
    private String receiverName;

    @Column(length = 20)
    private String receiverPhone;

    @Column(length = 500)
    private String receiverAddress;

    @Column(length = 500)
    private String remark;

    private LocalDateTime cancelTime;

    @Column(length = 200)
    private String cancelReason;

    private LocalDateTime completeTime;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) status = "pending_payment";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}