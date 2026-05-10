package com.appsys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单详情实体�? * 管理销售订单中每个产品的明细信�? * 
 * @author appsys-team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
@Schema(description = "订单详情")
public class OrderItem {

    /**
     * 订单详情ID，自增主�?     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单详情ID", example = "1")
    private Long id;

    /**
     * 关联订单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(hidden = true)
    private SalesOrder order;

    /**
     * 关联产品ID
     */
    @Column(nullable = false)
    @Schema(description = "产品ID", example = "1")
    private Long productId;

    /**
     * 产品名称（快照）
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "产品名称", example = "玉米种子A�?)
    private String productName;

    /**
     * 批次编号（快照）
     */
    @Column(nullable = false, length = 8)
    @Schema(description = "批次编号", example = "2026AB01")
    private String batchNumber;

    /**
     * 产品规格（快照）
     */
    @Column(length = 100)
    @Schema(description = "产品规格", example = "10kg/�?)
    private String specification;

    /**
     * 下单时的原价
     */
    @Column(precision = 10, scale = 2, nullable = false)
    @Schema(description = "原价", example = "299.00")
    private BigDecimal basePrice;

    /**
     * 实际单价（折扣后�?     */
    @Column(precision = 10, scale = 2, nullable = false)
    @Schema(description = "实际单价", example = "269.10")
    private BigDecimal actualPrice;

    /**
     * 购买数量
     */
    @Column(nullable = false)
    @Schema(description = "购买数量", example = "10")
    private Integer quantity;

    /**
     * 该行总原�?     */
    @Column(precision = 12, scale = 2, nullable = false)
    @Schema(description = "该行总原�?, example = "2990.00")
    private BigDecimal originalLineTotal;

    /**
     * 该行实际总金额（折扣后）
     */
    @Column(precision = 12, scale = 2, nullable = false)
    @Schema(description = "该行实际总金�?, example = "2691.00")
    private BigDecimal actualLineTotal;
}
