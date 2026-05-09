package com.agricultural.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 订单明细实体类
 * 记录每个订单的商品明细，包括品种、数量、单价、金额等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的订单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * 行号，用于排序
     */
    @Column(nullable = false)
    private Integer lineNo;

    /**
     * 关联的品种
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variety_id", nullable = false)
    private Variety variety;

    /**
     * 数量
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantity;

    /**
     * 单价
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 行金额 = 数量 * 单价
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal lineTotal;

    /**
     * 备注
     */
    @Column(length = 200)
    private String remark;

    /**
     * 计算行金额
     */
    public void calculateLineTotal() {
        if (quantity != null && unitPrice != null) {
            this.lineTotal = quantity.multiply(unitPrice);
        }
    }
}
