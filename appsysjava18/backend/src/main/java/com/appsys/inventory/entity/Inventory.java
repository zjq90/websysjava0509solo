package com.appsys.inventory.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存实体类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "inventory", indexes = {
    @Index(name = "idx_batch_no", columnList = "batch_no", unique = true),
    @Index(name = "idx_seed_id", columnList = "seed_id"),
    @Index(name = "idx_deleted", columnList = "deleted")
})
@Schema(description = "库存信息")
public class Inventory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "批次号（8位数字+字母组合，全局唯一）")
    @Column(name = "batch_no", nullable = false, length = 20, unique = true)
    private String batchNo;

    @Schema(description = "种子ID")
    @Column(name = "seed_id", nullable = false)
    private Long seedId;

    @Schema(description = "种子名称（冗余字段）")
    @Column(name = "seed_name", length = 100)
    private String seedName;

    @Schema(description = "入库数量")
    @Column(name = "quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Schema(description = "剩余数量")
    @Column(name = "remaining_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal remainingQuantity;

    @Schema(description = "入库日期")
    @Column(name = "in_date")
    private LocalDate inDate;

    @Schema(description = "保质期")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Schema(description = "发芽率")
    @Column(name = "germination_rate", precision = 4, scale = 1)
    private BigDecimal germinationRate;

    @Schema(description = "入库单价")
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Schema(description = "入库总价")
    @Column(name = "total_price", precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Schema(description = "仓库位置")
    @Column(name = "warehouse_location", length = 100)
    private String warehouseLocation;

    @Schema(description = "状态：1-正常，2-预警，3-过期")
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Schema(description = "备注")
    @Column(name = "remark", length = 500)
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seed_id", insertable = false, updatable = false)
    private Seed seed;
}
