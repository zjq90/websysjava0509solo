package com.personal.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 分类支出数据传输对象
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类支出数据")
public class CategoryExpenseDTO {

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "分类颜色")
    private String color;

    @Schema(description = "支出金额")
    private BigDecimal amount;

    @Schema(description = "占比（百分比）")
    private Double ratio;
}
