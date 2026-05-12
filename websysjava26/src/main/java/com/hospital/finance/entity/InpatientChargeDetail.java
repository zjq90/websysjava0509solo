package com.hospital.finance.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 住院收费明细实体类
 * 记录住院收费的具体费用项目，包括药品、检查、治疗、床位、护理等明细
 */
@Data
@Entity
@Table(name = "inpatient_charge_detail")
@Schema(description = "住院收费明细")
public class InpatientChargeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "明细ID")
    private Long id;

    @Column(name = "charge_id", nullable = false)
    @Schema(description = "收费记录ID")
    private Long chargeId;

    @Column(name = "charge_no", length = 50)
    @Schema(description = "收费单号")
    private String chargeNo;

    @Column(name = "item_type", length = 50)
    @Schema(description = "费用类型(药品、检查、治疗、床位、护理等)")
    private String itemType;

    @Column(name = "item_code", length = 50)
    @Schema(description = "项目编码")
    private String itemCode;

    @Column(name = "item_name", length = 200)
    @Schema(description = "项目名称")
    private String itemName;

    @Column(name = "specification", length = 100)
    @Schema(description = "规格")
    private String specification;

    @Column(name = "unit", length = 20)
    @Schema(description = "单位")
    private String unit;

    @Column(name = "quantity")
    @Schema(description = "数量")
    private Integer quantity;

    @Column(name = "unit_price", precision = 10, scale = 2)
    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Column(name = "amount", precision = 10, scale = 2)
    @Schema(description = "金额")
    private BigDecimal amount;

    @Column(name = "is_insurance")
    @Schema(description = "是否医保报销")
    private Boolean isInsurance;

    @Column(name = "insurance_ratio")
    @Schema(description = "医保报销比例")
    private Double insuranceRatio;

    @Column(name = "insurance_amount", precision = 10, scale = 2)
    @Schema(description = "医保报销金额")
    private BigDecimal insuranceAmount;

    @Column(name = "self_pay_amount", precision = 10, scale = 2)
    @Schema(description = "自付金额")
    private BigDecimal selfPayAmount;

    @Column(name = "charge_date")
    @Schema(description = "费用日期")
    private LocalDateTime chargeDate;

    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}