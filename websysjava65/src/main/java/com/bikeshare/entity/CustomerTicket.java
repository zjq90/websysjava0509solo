package com.bikeshare.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客服工单实体类
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "customer_tickets")
public class CustomerTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 50)
    private String username;

    @Column(nullable = false, length = 30)
    private String ticketNo;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(columnDefinition = "TEXT")
    private String reply;

    @Column(length = 50)
    private String handler;

    private LocalDateTime handleTime;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime = LocalDateTime.now();
}
