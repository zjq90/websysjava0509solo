package com.heritage.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易订单实体类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "trade_order")
public class TradeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false, length = 50)
    private String orderNo;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "heritage_id", nullable = false)
    private Long heritageId;

    @Column(name = "heritage_name", length = 200)
    private String heritageName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "service_fee")
    private BigDecimal serviceFee;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "pay_status")
    private Integer payStatus = 0;

    @Column(name = "order_status")
    private Integer orderStatus = 1;

    @Column(name = "freeze_reason", length = 500)
    private String freezeReason;

    @Column(name = "pay_time")
    private LocalDateTime payTime;

    @Column(name = "ship_time")
    private LocalDateTime shipTime;

    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "is_supervised")
    private Integer isSupervised = 1;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}
