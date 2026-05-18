package com.flower.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 订单明细实体类
 * 对应数据库表order_item
 */
@Data
@Entity
@Table(name = "order_item")
public class OrderItem {

    /**
     * 明细ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单ID
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * 商品ID
     */
    @Column(nullable = false)
    private Long productId;

    /**
     * 商品名称
     */
    @Column(nullable = false, length = 100)
    private String productName;

    /**
     * 商品图片
     */
    @Column(length = 500)
    private String productImage;

    /**
     * 商品单价
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 购买数量
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 小计金额
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}
