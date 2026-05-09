package com.appsys.production.dto;

import javax.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class QualityInspectionDTO {

    @NotNull(message = "批次ID不能为空")
    private Long batchId;

    @NotBlank(message = "环节编码不能为空")
    private String stageCode;

    private Long operatorId;

    private String operatorName;

    @NotNull(message = "水分不能为空")
    @DecimalMin(value = "0.0", message = "水分不能小于0")
    @DecimalMax(value = "100.0", message = "水分不能大于100")
    @Digits(integer = 3, fraction = 1, message = "水分数值格式不正确，最多1位小数")
    private BigDecimal moisture;

    @NotNull(message = "净度不能为空")
    @DecimalMin(value = "0.0", message = "净度不能小于0")
    @DecimalMax(value = "100.0", message = "净度不能大于100")
    @Digits(integer = 3, fraction = 1, message = "净度数值格式不正确，最多1位小数")
    private BigDecimal purity;

    @NotNull(message = "发芽率不能为空")
    @DecimalMin(value = "0.0", message = "发芽率不能小于0")
    @DecimalMax(value = "100.0", message = "发芽率不能大于100")
    @Digits(integer = 3, fraction = 1, message = "发芽率数值格式不正确，最多1位小数")
    private BigDecimal germinationRate;

    private String remark;
}
