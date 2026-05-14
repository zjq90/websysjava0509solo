package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 存储影楼订单信息，包含完整的状态流转用于客户转化漏斗分析
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_order")
@Schema(description = "订单信息")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单ID")
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    @Schema(description = "订单编号")
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @Schema(description = "客户")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    @Schema(description = "套餐")
    private Package aPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @Schema(description = "门店")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    @Schema(description = "销售员")
    private Employee sales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    @Schema(description = "摄影师")
    private Employee photographer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "makeup_artist_id")
    @Schema(description = "化妆师")
    private Employee makeupArtist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_selector_id")
    @Schema(description = "选片师")
    private Employee photoSelector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_editor_id")
    @Schema(description = "修图师")
    private Employee photoEditor;

    /**
     * 订单状态（转化漏斗）：
     * CONSULTING-咨询中
     * ORDERED-已下单
     * SHOOTING-拍摄中
     * SHOOT_COMPLETED-拍摄完成
     * SELECTING-选片中
     * EDITING-修片中
     * DELIVERED-已交付
     * CANCELLED-已取消
     */
    @Column(nullable = false, length = 30)
    @Schema(description = "订单状态")
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "订单金额")
    private BigDecimal amount;

    @Column(name = "deposit_amount", precision = 10, scale = 2)
    @Schema(description = "定金金额")
    private BigDecimal depositAmount;

    @Column(name = "balance_amount", precision = 10, scale = 2)
    @Schema(description = "尾款金额")
    private BigDecimal balanceAmount;

    @Column(name = "shoot_date")
    @Schema(description = "拍摄日期")
    private LocalDate shootDate;

    @Column(name = "deliver_date")
    @Schema(description = "交付日期")
    private LocalDate deliverDate;

    @Column(name = "customer_rating", precision = 2, scale = 1)
    @Schema(description = "客户评分")
    private BigDecimal customerRating;

    @Column(length = 500)
    @Schema(description = "客户评价")
    private String customerFeedback;

    @Column(name = "cost_labor", precision = 10, scale = 2)
    @Schema(description = "人工成本")
    private BigDecimal costLabor = BigDecimal.ZERO;

    @Column(name = "cost_clothing", precision = 10, scale = 2)
    @Schema(description = "服装成本")
    private BigDecimal costClothing = BigDecimal.ZERO;

    @Column(name = "cost_materials", precision = 10, scale = 2)
    @Schema(description = "耗材成本")
    private BigDecimal costMaterials = BigDecimal.ZERO;

    @Column(name = "consult_time")
    @Schema(description = "咨询时间")
    private LocalDateTime consultTime;

    @Column(name = "order_time")
    @Schema(description = "下单时间")
    private LocalDateTime orderTime;

    @Column(name = "shoot_time")
    @Schema(description = "拍摄完成时间")
    private LocalDateTime shootTime;

    @Column(name = "deliver_time")
    @Schema(description = "交付时间")
    private LocalDateTime deliverTime;

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
        if (consultTime == null) {
            consultTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
