package com.hospital.entity.medicine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "medicine_in_stock")
@Schema(description = "药品入库记录实体")
public class MedicineInStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "入库记录ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "入库单号")
    private String inStockCode;

    @Column(nullable = false)
    @Schema(description = "药品ID")
    private Long medicineId;

    @Column(length = 50)
    @Schema(description = "药品编码")
    private String medicineCode;

    @Column(length = 100)
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
    @Schema(description = "入库数量")
    private Integer quantity;

    @Column(length = 20)
    @Schema(description = "单位")
    private String unit;

    @Column(precision = 10, scale = 2)
    @Schema(description = "采购单价")
    private BigDecimal purchasePrice;

    @Column(precision = 12, scale = 2)
    @Schema(description = "采购总金额")
    private BigDecimal totalAmount;

    @Column(length = 100)
    @Schema(description = "供应商")
    private String supplier;

    @Column(length = 50)
    @Schema(description = "仓库位置")
    private String warehouseLocation;

    @Column
    @Schema(description = "入库类型(0-采购入库,1-调拨入库,2-退货入库,3-盘盈入库)")
    private Integer inStockType;

    @Column
    @Schema(description = "关联采购计划ID")
    private Long purchasePlanId;

    @Column
    @Schema(description = "入库状态(0-待审核,1-已审核,2-已驳回)")
    private Integer status;

    @Column(length = 50)
    @Schema(description = "入库人")
    private String inStockPerson;

    @Column
    @Schema(description = "入库时间")
    private LocalDateTime inStockTime;

    @Column(length = 50)
    @Schema(description = "审核人")
    private String auditor;

    @Column
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Column(length = 500)
    @Schema(description = "审核意见")
    private String auditOpinion;

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
        if (status == null) {
            status = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
