package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 调拨单明细实体类
 * 记录调拨单中每个批次的具体调拨数量
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transfer_order_item")
public class TransferOrderItem extends BaseEntity {

    /**
     * 调拨单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

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
     * 调拨数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 已出库数量
     */
    @Column(name = "outbound_quantity", nullable = false)
    private Integer outboundQuantity = 0;

    /**
     * 已入库数量
     */
    @Column(name = "inbound_quantity", nullable = false)
    private Integer inboundQuantity = 0;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;
}
