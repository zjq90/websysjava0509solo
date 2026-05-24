package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "payment_records")
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String paymentNo;

    @Column(length = 30)
    private String orderNo;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 50)
    private String username;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    private String paymentType;

    @Column(nullable = false, length = 20)
    private String paymentMethod;

    @Column(length = 20)
    private String thirdPartyNo;

    @Column(nullable = false, length = 20)
    private String status = "SUCCESS";

    @Column(length = 200)
    private String remark;

    private LocalDateTime successTime;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
}
