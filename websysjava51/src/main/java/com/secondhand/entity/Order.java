package com.secondhand.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_order")
@Schema(description = "订单实体")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    @Column(nullable = false, unique = true, length = 50)
    private String orderNo;

    @Schema(description = "商品ID")
    @Column(nullable = false)
    private Long productId;

    @Schema(description = "商品名称")
    @Column(nullable = false, length = 200)
    private String productName;

    @Schema(description = "商品图片")
    @Column(length = 500)
    private String productImage;

    @Schema(description = "购买数量")
    @Column(nullable = false)
    private Integer quantity;

    @Schema(description = "订单金额")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Schema(description = "买家ID")
    @Column(nullable = false)
    private Long buyerId;

    @Schema(description = "卖家ID")
    @Column(nullable = false)
    private Long sellerId;

    @Schema(description = "订单状态: PENDING_PAYMENT-待付款, PENDING_SHIPMENT-待发货, SHIPPED-已发货, COMPLETED-已完成, CANCELLED-已取消, REFUNDING-退款中, REFUNDED-已退款")
    @Column(nullable = false, length = 30)
    private String status;

    @Schema(description = "是否异常订单")
    @Column(nullable = false)
    private Boolean isAbnormal;

    @Schema(description = "异常原因")
    @Column(length = 500)
    private String abnormalReason;

    @Schema(description = "收货人姓名")
    @Column(length = 50)
    private String receiverName;

    @Schema(description = "收货人电话")
    @Column(length = 20)
    private String receiverPhone;

    @Schema(description = "收货地址")
    @Column(length = 500)
    private String shippingAddress;

    @Schema(description = "物流公司")
    @Column(length = 50)
    private String logisticsCompany;

    @Schema(description = "物流单号")
    @Column(length = 50)
    private String trackingNumber;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "发货时间")
    private LocalDateTime shipTime;

    @Schema(description = "完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "更新时间")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (isAbnormal == null) {
            isAbnormal = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

}