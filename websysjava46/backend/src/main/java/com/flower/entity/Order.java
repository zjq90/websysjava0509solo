package com.flower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单实体类
 * 对应数据库表order
 */
@Data
@Entity
@Table(name = "orders")
public class Order {

    /**
     * 订单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String orderNo;

    /**
     * 客户ID
     */
    @Column(nullable = false)
    private Long customerId;

    /**
     * 客户名称
     */
    @Column(length = 50)
    private String customerName;

    /**
     * 客户电话
     */
    @Column(length = 20)
    private String customerPhone;

    /**
     * 收货地址
     */
    @Column(length = 200)
    private String address;

    /**
     * 订单总金额
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 订单状态：
     * 1-待审核
     * 2-已接单/备货中
     * 3-制作中
     * 4-已发货
     * 5-已完成
     * 6-已取消
     * 7-异常订单
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 是否需要定制：0-否，1-是
     */
    @Column(nullable = false)
    private Integer customFlag = 0;

    /**
     * 定制要求
     */
    @Column(columnDefinition = "TEXT")
    private String customRequirement;

    /**
     * 订单备注
     */
    @Column(length = 500)
    private String remark;

    /**
     * 支付状态：0-未支付，1-已支付
     */
    @Column(nullable = false)
    private Integer payStatus = 0;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    /**
     * 订单明细列表
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (orderNo == null) {
            orderNo = "ORD" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
