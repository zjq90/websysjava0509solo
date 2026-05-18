package com.flower.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 */
@Data
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 20)
    private String type;

    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal value;

    @Column(precision = 10, scale = 2)
    private BigDecimal minAmount;

    private Integer totalCount;

    @Column(columnDefinition = "int default 0")
    private Integer usedCount = 0;

    @Column(columnDefinition = "int default 0")
    private Integer receiveCount = 0;

    @Column(columnDefinition = "boolean default 0")
    private Boolean isNewUserOnly = false;

    private Integer validDays;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(columnDefinition = "boolean default 1")
    private Boolean enabled = true;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
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