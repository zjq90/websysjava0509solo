package com.vending.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 订单明细实体类
 */
@Data
@Entity
@Table(name = "order_item")
@Schema(description = "订单明细")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "明细ID")
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    @Schema(description = "所属订单", required = true)
    private Order order;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @Schema(description = "商品")
    private Product product;
    
    @Column(nullable = false)
    @Schema(description = "商品名称快照", required = true)
    private String productName;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "商品单价快照", required = true)
    private BigDecimal price;
    
    @Column(nullable = false)
    @Schema(description = "购买数量", required = true)
    private Integer quantity;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "小计金额", required = true)
    private BigDecimal subtotal;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id")
    @Schema(description = "出货货道")
    private Slot slot;
}
