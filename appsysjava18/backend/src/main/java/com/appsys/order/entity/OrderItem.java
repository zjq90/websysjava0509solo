package com.appsys.order.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 订单明细实体类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_item", indexes = {
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_inventory_id", columnList = "inventory_id")
})
@Schema(description = "订单明细")
public class OrderItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Schema(description = "库存ID")
    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

    @Schema(description = "批次号")
    @Column(name = "batch_no", length = 20)
    private String batchNo;

    @Schema(description = "种子名称")
    @Column(name = "seed_name", length = 100)
    private String seedName;

    @Schema(description = "销售数量")
    @Column(name = "quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Schema(description = "销售单价")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Schema(description = "小计金额")
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Schema(description = "备注")
    @Column(name = "remark", length = 255)
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;
}
