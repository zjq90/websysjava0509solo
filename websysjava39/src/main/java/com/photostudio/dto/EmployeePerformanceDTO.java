package com.photostudio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工绩效排行DTO
 * 包含员工绩效排名数据
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Schema(description = "员工绩效数据")
public class EmployeePerformanceDTO {

    @Schema(description = "员工ID")
    private Long employeeId;

    @Schema(description = "员工姓名")
    private String employeeName;

    @Schema(description = "岗位")
    private String position;

    @Schema(description = "接单数量")
    private Integer orderCount;

    @Schema(description = "客户评分")
    private BigDecimal avgRating;

    @Schema(description = "成单金额")
    private BigDecimal totalRevenue;

    @Schema(description = "修图数量（仅修图师）")
    private Integer editPhotoCount;

    @Schema(description = "返修率（%，仅修图师）")
    private BigDecimal reworkRate;

    @Schema(description = "排名")
    private Integer rank;
}
