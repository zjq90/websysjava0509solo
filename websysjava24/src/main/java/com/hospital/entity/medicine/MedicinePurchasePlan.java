package com.hospital.entity.medicine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "medicine_purchase_plan")
@Schema(description = "药品采购计划实体")
public class MedicinePurchasePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "采购计划ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "采购计划编号")
    private String planCode;

    @Column
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

    @Column(nullable = false)
    @Schema(description = "采购数量")
    private Integer quantity;

    @Column(length = 20)
    @Schema(description = "单位")
    private String unit;

    @Column(precision = 10, scale = 2)
    @Schema(description = "预估单价")
    private BigDecimal estimatedPrice;

    @Column(precision = 12, scale = 2)
    @Schema(description = "预估总金额")
    private BigDecimal estimatedTotalAmount;

    @Column(length = 100)
    @Schema(description = "供应商")
    private String supplier;

    @Column
    @Schema(description = "期望到货日期")
    private LocalDateTime expectedArrivalDate;

    @Column
    @Schema(description = "计划状态(0-草稿,1-已审核,2-已采购,3-已完成,4-已取消)")
    private Integer status;

    @Column(length = 50)
    @Schema(description = "申请人")
    private String applicant;

    @Column
    @Schema(description = "申请时间")
    private LocalDateTime applicationTime;

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
