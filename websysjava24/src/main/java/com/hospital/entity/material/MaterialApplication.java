package com.hospital.entity.material;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "material_application")
@Schema(description = "物资领用申请实体")
public class MaterialApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "申请ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "申请单号")
    private String applicationCode;

    @Column(nullable = false)
    @Schema(description = "物资ID")
    private Long materialId;

    @Column(length = 50)
    @Schema(description = "物资编码")
    private String materialCode;

    @Column(length = 100)
    @Schema(description = "物资名称")
    private String materialName;

    @Column(length = 50)
    @Schema(description = "规格型号")
    private String specification;

    @Column(nullable = false)
    @Schema(description = "申请数量")
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

    @Column(length = 50)
    @Schema(description = "申请科室")
    private String department;

    @Column(length = 50)
    @Schema(description = "申请人")
    private String applicant;

    @Column
    @Schema(description = "申请时间")
    private LocalDateTime applicationTime;

    @Column(length = 50)
    @Schema(description = "领用用途")
    private String purpose;

    @Column
    @Schema(description = "申请状态(0-草稿,1-待审核,2-已审核,3-已领用,4-已驳回,5-已取消)")
    private Integer status;

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
