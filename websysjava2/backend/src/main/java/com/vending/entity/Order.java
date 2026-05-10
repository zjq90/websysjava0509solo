package com.vending.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单实体类
 * 包含订单号、用户信息、购买商品、金额、支付状态、取货状态、交易时间等
 */
@Data
@Entity
@Table(name = "orders")
@Schema(description = "交易订单")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单ID")
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "订单号", required = true)
    private String orderNo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    @Schema(description = "售货机")
    private VendingMachine machine;
    
    @Schema(description = "用户标识")
    private String userIdentifier;
    
    @Schema(description = "用户名称")
    private String userName;
    
    @Schema(description = "用户手机号")
    private String userPhone;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "订单总金额", required = true)
    private BigDecimal totalAmount;
    
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    @Schema(description = "支付状态：PENDING待支付, PAID已支付, REFUND已退款, CANCELLED已取消")
    private String paymentStatus = "PENDING";
    
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    @Schema(description = "取货状态：PENDING待取货, PICKED已取货, FAILED取货失败")
    private String pickupStatus = "PENDING";
    
    @Schema(description = "支付方式：WECHAT微信, ALIPAY支付宝, CASH现金")
    private String paymentMethod;
    
    @Schema(description = "支付流水号")
    private String paymentTransactionId;
    
    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;
    
    @Schema(description = "取货时间")
    private LocalDateTime pickupTime;
    
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Schema(description = "订单明细")
    private List<OrderItem> items = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
