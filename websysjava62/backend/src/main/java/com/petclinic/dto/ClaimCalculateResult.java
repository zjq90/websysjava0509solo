package com.petclinic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 理赔费用计算结果DTO
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Schema(description = "理赔费用计算结果")
public class ClaimCalculateResult {

    @Schema(description = "问诊总费用")
    private BigDecimal totalFee;

    @Schema(description = "免赔额")
    private BigDecimal deductible;

    @Schema(description = "理赔比例（%）")
    private BigDecimal claimRate;

    @Schema(description = "最高理赔限额")
    private BigDecimal maxClaimAmount;

    @Schema(description = "可理赔金额（总费用-免赔额）")
    private BigDecimal claimableAmount;

    @Schema(description = "实际理赔金额")
    private BigDecimal claimAmount;

    @Schema(description = "自付金额")
    private BigDecimal selfPayAmount;

    @Schema(description = "保险公司名称")
    private String insuranceCompanyName;

    @Schema(description = "保险公司ID")
    private Long insuranceCompanyId;

    @Schema(description = "计算说明")
    private String description;
}
