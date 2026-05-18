package com.secondhand.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_order")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false, length = 50)
    private String orderNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Column(length = 20)
    private String status = "PENDING";

    @Column(name = "pickup_type")
    private String pickupType;

    @Column(name = "pickup_point_id")
    private Long pickupPointId;

    private String address;

    private String receiver;

    private String phone;

    @Column(name = "logistics_no", length = 50)
    private String logisticsNo;

    @Column(name = "ship_time")
    private LocalDateTime shipTime;

    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    @Column(name = "refund_time")
    private LocalDateTime refundTime;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Transient
    private Product product;

    @Transient
    private User buyer;

}
