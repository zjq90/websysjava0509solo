package com.musicplatform.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    private Long musicId;
    private String musicTitle;

    private Long albumId;
    private String albumTitle;

    private Long artistId;
    private String artistName;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private String paymentId;
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum OrderType {
        SINGLE_MUSIC, ALBUM, TIP, VIP_SUBSCRIPTION
    }

    public enum PaymentMethod {
        WECHAT, ALIPAY
    }

    public enum OrderStatus {
        PENDING, PAID, CANCELLED, REFUNDED
    }
}
