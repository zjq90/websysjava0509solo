package com.platform.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 销售报表查询DTO
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@Schema(description = "销售报表查询请求")
public class SalesReportDTO {

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "时间维度: DAY/WEEK/MONTH/YEAR")
    private String dimension = "DAY";

    @Schema(description = "支付渠道")
    private String payChannel;
}
