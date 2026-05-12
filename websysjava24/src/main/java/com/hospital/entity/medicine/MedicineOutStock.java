package com.hospital.entity.medicine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "medicine_out_stock")
@Schema(description = "药品出库记录实体")
public class MedicineOutStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "出库记录ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "出库单号")
    private String outStockCode;

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

    @Column(length = 50)
    @Schema(description = "批号")
    private String batchNumber;

    @Column(nullable = false)
    @Schema(description = "出库数量")
    private Integer quantity;

    @Column(length = 20)
    @Schema(description = "单位")
    private String unit;

    @Column(precision = 10, scale = 2)
    @Schema(description = "出库单价")
    private BigDecimal outPrice;

    @Column(precision = 12, scale = 2)
    @Schema(description = "出库总金额")
    private BigDecimal totalAmount;

    @Column(length = 50)
    @Schema(description = "领用科室")
    private String department;

    @Column(length = 50)
    @Schema(description = "领用人")
    private String receiver;

    @Column(length = 100)
    @Schema(description = "处方号/医嘱号")
    private String prescriptionNo;

    @Column
    @Schema(description = "出库类型(0-领用出库,1-销售出库,2-调拨出库,3-报损出库,4-退货出库)")
    private Integer outStockType;

    @Column(length = 50)
    @Schema(description = "仓库位置")
    private String warehouseLocation;

    @Column
    @Schema(description = "出库状态(0-待审核,1-已审核,2-已驳回)")
    private Integer status;

    @Column(length = 50)
    @Schema(description = "出库人")
    private String outStockPerson;

    @Column
    @Schema(description = "出库时间")
    private LocalDateTime outStockTime;

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
