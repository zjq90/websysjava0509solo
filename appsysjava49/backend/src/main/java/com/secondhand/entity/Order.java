package com.secondhand.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String orderNo;

    @Column(nullable = false)
    private Long buyerId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long productId;

    @Column(length = 200)
    private String productTitle;

    @Column(length = 255)
    private String productImage;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal logisticsFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 50)
    private String paymentMethod;

    private LocalDateTime paymentTime;

    @Column(nullable = false)
    private Integer status = 0;

    @Column(length = 200)
    private String logisticsNo;

    @Column(length = 50)
    private String logisticsCompany;

    private Long pickupPointId;

    @Column(length = 500)
    private String buyerAddress;

    @Column(length = 50)
    private String buyerPhone;

    @Column(length = 50)
    private String buyerName;

    @Column(length = 500)
    private String sellerRemark;

    @Column(length = 500)
    private String buyerRemark;

    private LocalDateTime deliveryTime;

    private LocalDateTime receiveTime;

    private LocalDateTime completeTime;

    private LocalDateTime cancelTime;

    @Column(length = 200)
    private String cancelReason;

    @Column(updatable = false)
    private LocalDateTime createTime;

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
