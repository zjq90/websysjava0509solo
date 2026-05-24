package com.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "车牌查验结果")
public class PlateCheckResult {

    @Schema(description = "车牌号")
    private String plateNumber;

    @Schema(description = "是否合法: true-合法, false-非法")
    private Boolean legal;

    @Schema(description = "结果消息")
    private String message;

    @Schema(description= "风险等级: LOW-低, MEDIUM-中, HIGH-高, CRITICAL-极高")
    private String riskLevel;

    @Schema(description= "违规原因")
    private String violationReason;

    @Schema(description="违规详情描述")
    private String violationDescription;

    @Schema(description="车辆品牌")
    private String brand;

    @Schema(description= "车辆型号")
    private String model;

    @Schema(description= "车身颜色")
    private String color;

    @Schema(description= "车主姓名")
    private String ownerName;
}
