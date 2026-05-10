package com.appsys.field.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 田间记录DTO
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@Schema(description = "田间记录请求")
public class FieldRecordDTO {

    @Schema(description = "地块名称")
    @NotBlank(message = "地块名称不能为空")
    private String fieldName;

    @Schema(description = "地块位置")
    private String location;

    @Schema(description = "地块面积（亩）")
    private BigDecimal area;

    @Schema(description = "种植作物")
    private String cropName;

    @Schema(description = "品种")
    private String variety;

    @Schema(description = "记录日期")
    @NotNull(message = "记录日期不能为空")
    private LocalDate recordDate;

    @Schema(description = "天气情况")
    private String weather;

    @Schema(description = "温度（℃）")
    private BigDecimal temperature;

    @Schema(description = "湿度（%）")
    private BigDecimal humidity;

    @Schema(description = "土壤情况")
    private String soilCondition;

    @Schema(description = "病虫害情况")
    private String pestStatus;

    @Schema(description = "施肥情况")
    private String fertilization;

    @Schema(description = "灌溉情况")
    private String irrigation;

    @Schema(description = "生长情况描述")
    private String growthStatus;

    @Schema(description = "操作内容")
    private String operation;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "图片URL（多个用逗号分隔）")
    private String images;
}
