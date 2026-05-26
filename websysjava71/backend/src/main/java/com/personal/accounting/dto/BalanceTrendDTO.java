package com.personal.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 余额趋势数据传输对象
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "余额趋势数据")
public class BalanceTrendDTO {

    @Schema(description = "日期")
    private LocalDate date;

    @Schema(description = "余额")
    private BigDecimal balance;
}
