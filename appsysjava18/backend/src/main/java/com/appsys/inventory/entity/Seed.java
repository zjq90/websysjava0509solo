package com.appsys.inventory.entity;

import com.appsys.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 种子信息实体类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "seed", indexes = {
    @Index(name = "idx_seed_name", columnList = "seed_name"),
    @Index(name = "idx_deleted", columnList = "deleted")
})
@Schema(description = "种子信息")
public class Seed extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "种子名称")
    @Column(name = "seed_name", nullable = false, length = 100)
    private String seedName;

    @Schema(description = "种子类别")
    @Column(name = "category", length = 50)
    private String category;

    @Schema(description = "规格")
    @Column(name = "specification", length = 100)
    private String specification;

    @Schema(description = "单位")
    @Column(name = "unit", length = 20)
    private String unit;

    @Schema(description = "进价")
    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Schema(description = "售价")
    @Column(name = "sale_price", precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Schema(description = "发芽率（0-100，1位小数）")
    @Column(name = "germination_rate", precision = 4, scale = 1)
    private BigDecimal germinationRate;

    @Schema(description = "保质期")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Schema(description = "供应商")
    @Column(name = "supplier", length = 100)
    private String supplier;

    @Schema(description = "备注")
    @Column(name = "remark", length = 500)
    private String remark;
}
