package com.hospital.entity.medicine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "medicine_inventory")
@Schema(description = "药品库存实体")
public class MedicineInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "库存ID")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "药品ID")
    private Long medicineId;

    @Column(nullable = false, length = 50)
    @Schema(description = "药品编码")
    private String medicineCode;

    @Column(nullable = false, length = 100)
    @Schema(description = "药品名称")
    private String medicineName;

    @Column(length = 50)
    @Schema(description = "规格")
    private String specification;

    @Column(nullable = false, length = 50)
    @Schema(description = "批号")
    private String batchNumber;

    @Column
    @Schema(description = "生产日期")
    private LocalDate productionDate;

    @Column(nullable = false)
    @Schema(description = "有效期至")
    private LocalDate expiryDate;

    @Column(nullable = false)
    @Schema(description = "库存数量")
    private Integer quantity;

    @Column(length = 20)
    @Schema(description = "单位")
    private String unit;

    @Column(precision = 10, scale = 2)
    @Schema(description = "采购单价")
    private BigDecimal purchasePrice;

    @Column(precision = 10, scale = 2)
    @Schema(description = "零售单价")
    private BigDecimal retailPrice;

    @Column(length = 50)
    @Schema(description = "仓库位置")
    private String warehouseLocation;

    @Column
    @Schema(description = "库存状态(0-正常,1-近效期,2-已过期,3-滞销)")
    private Integer inventoryStatus;

    @Column
    @Schema(description = "最后入库时间")
    private LocalDateTime lastInTime;

    @Column
    @Schema(description = "最后出库时间")
    private LocalDateTime lastOutTime;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (inventoryStatus == null) {
            inventoryStatus = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
