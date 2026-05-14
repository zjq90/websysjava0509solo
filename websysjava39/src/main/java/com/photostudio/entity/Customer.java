package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 存储影楼客户的基本信息
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_customer")
@Schema(description = "客户信息")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "客户ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "客户姓名")
    private String name;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String phone;

    @Column(length = 10)
    @Schema(description = "性别")
    private String gender;

    @Column(name = "birth_date")
    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Column(length = 200)
    @Schema(description = "联系地址")
    private String address;

    @Column(length = 100)
    @Schema(description = "微信号")
    private String wechat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referral_id")
    @Schema(description = "推荐人ID")
    private Customer referral;

    @Column(name = "is_old_customer")
    @Schema(description = "是否老客户")
    private Boolean oldCustomer = false;

    @Column(name = "total_orders")
    @Schema(description = "总订单数")
    private Integer totalOrders = 0;

    @Column(name = "total_amount", precision = 12, scale = 2)
    @Schema(description = "总消费金额")
    private java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;

    @Column(name = "last_visit_time")
    @Schema(description = "最近访问时间")
    private LocalDateTime lastVisitTime;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
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
