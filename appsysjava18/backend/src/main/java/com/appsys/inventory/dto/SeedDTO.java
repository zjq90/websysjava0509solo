package com.appsys.inventory.dto;

import com.appsys.common.validator.FutureMinMonths;
import com.appsys.common.validator.GerminationRate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 种子信息DTO
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@Schema(description = "种子信息")
public class SeedDTO {

    @Schema(description = "种子名称")
    @NotBlank(message = "种子名称不能为空")
    private String seedName;

    @Schema(description = "种子类别")
    private String category;

    @Schema(description = "规格")
    private String specification;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "进价")
    @DecimalMin(value = "0", message = "进价不能为负数")
    private BigDecimal purchasePrice;

    @Schema(description = "售价")
    @DecimalMin(value = "0", message = "售价不能为负数")
    private BigDecimal salePrice;

    @Schema(description = "发芽率（0-100，1位小数）")
    @GerminationRate
    private BigDecimal germinationRate;

    @Schema(description = "保质期")
    @FutureMinMonths(6)
    private LocalDate expiryDate;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "备注")
    private String remark;
}
