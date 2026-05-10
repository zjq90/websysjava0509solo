package com.platform.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售报表VO
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@Schema(description = "销售报表数据")
public class SalesReportVO {

    @Schema(description = "时间标签（日/周/月/年）")
    private String timeLabel;

    @Schema(description = "销售金额")
    private BigDecimal salesAmount;

    @Schema(description = "订单数量")
    private Long orderCount;

    @Schema(description = "平均订单金额")
    private BigDecimal avgOrderAmount;
}
