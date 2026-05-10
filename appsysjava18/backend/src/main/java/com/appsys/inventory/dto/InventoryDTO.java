package com.appsys.inventory.dto;

import com.appsys.common.validator.BatchNumber;
import com.appsys.common.validator.FutureMinMonths;
import com.appsys.common.validator.GerminationRate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存请求DTO
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@Schema(description = "库存请求")
public class InventoryDTO {

    @Schema(description = "批次号（8位数字+字母组合）", example = "BAT00001")
    @NotBlank(message = "批次号不能为空")
    @BatchNumber
    private String batchNo;

    @Schema(description = "种子ID")
    @NotNull(message = "种子ID不能为空")
    private Long seedId;

    @Schema(description = "入库数量")
    @NotNull(message = "入库数量不能为空")
    @DecimalMin(value = "0.01", message = "入库数量必须大于0")
    private BigDecimal quantity;

    @Schema(description = "入库日期")
    private LocalDate inDate;

    @Schema(description = "保质期（不得早于当前日期+6个月）")
    @FutureMinMonths(6)
    private LocalDate expiryDate;

    @Schema(description = "发芽率（0-100，1位小数）")
    @GerminationRate
    private BigDecimal germinationRate;

    @Schema(description = "入库单价")
    @DecimalMin(value = "0", message = "入库单价不能为负数")
    private BigDecimal unitPrice;

    @Schema(description = "仓库位置")
    private String warehouseLocation;

    @Schema(description = "备注")
    private String remark;
}
