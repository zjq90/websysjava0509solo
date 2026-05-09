package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 盘点单明细实体类
 * 记录每个库存项的盘点结果
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_take_item")
public class StockTakeItem extends BaseEntity {

    /**
     * 盘点单ID
     */
    @Column(name = "take_id", nullable = false)
    private Long takeId;

    /**
     * 库存ID
     */
    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

    /**
     * 批次ID
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 批次信息（多对一关系）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", insertable = false, updatable = false)
    private SeedBatch seedBatch;

    /**
     * 账面数量（系统记录）
     */
    @Column(name = "book_quantity", nullable = false)
    private Integer bookQuantity;

    /**
     * 实盘数量（实际盘点）
     */
    @Column(name = "actual_quantity", nullable = false)
    private Integer actualQuantity;

    /**
     * 差异数量 = 实盘数量 - 账面数量
     */
    @Column(name = "diff_quantity", nullable = false)
    private Integer diffQuantity;

    /**
     * 单价
     */
    @Column(name = "unit_price")
    private Double unitPrice;

    /**
     * 差异金额 = 差异数量 * 单价
     */
    @Column(name = "diff_amount")
    private Double diffAmount;

    /**
     * 状态：
     * 0-正常（无差异）
     * 1-盘亏
     * 2-盘盈
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 盘点说明
     */
    @Column(name = "description", length = 300)
    private String description;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;
}
