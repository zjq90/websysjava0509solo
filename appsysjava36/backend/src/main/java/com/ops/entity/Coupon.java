package com.ops.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 * 存储优惠券的基本信息和发放规则
 * 
 * @author ops-admin
 */
@Data
@Entity
@Table(name = "biz_coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_code", unique = true, nullable = false, length = 50)
    private String couponCode;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "target_user_type", length = 50)
    private String targetUserType;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "valid_start_time")
    private LocalDateTime validStartTime;

    @Column(name = "valid_end_time")
    private LocalDateTime validEndTime;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        usedCount = 0;
        status = "ACTIVE";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
