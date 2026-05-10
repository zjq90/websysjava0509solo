package com.vending.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

/**
 * 补货单明细实体类
 */
@Data
@Entity
@Table(name = "restock_order_item")
@Schema(description = "补货单明细")
public class RestockOrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "明细ID")
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restock_order_id", nullable = false)
    @JsonBackReference
    @Schema(description = "所属补货单", required = true)
    private RestockOrder restockOrder;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @Schema(description = "商品")
    private Product product;
    
    @Column(nullable = false)
    @Schema(description = "商品名称", required = true)
    private String productName;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id")
    @Schema(description = "目标货道")
    private Slot slot;
    
    @Column(nullable = false)
    @Schema(description = "货道编号", required = true)
    private Integer slotNumber;
    
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Schema(description = "补货前库存")
    private Integer beforeStock = 0;
    
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Schema(description = "补货数量", required = true)
    private Integer restockQuantity = 0;
    
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    @Schema(description = "补货后库存")
    private Integer afterStock = 0;
}
