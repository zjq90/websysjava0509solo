package com.personal.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 热力图数据传输对象
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热力图数据")
public class HeatmapDataDTO {

    @Schema(description = "小时（0-23）")
    private Integer hour;

    @Schema(description = "星期（1-7，1=周日）")
    private Integer dayOfWeek;

    @Schema(description = "金额")
    private BigDecimal amount;
}
