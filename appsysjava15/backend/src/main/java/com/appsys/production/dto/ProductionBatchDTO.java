package com.appsys.production.dto;

import javax.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductionBatchDTO {

    @NotBlank(message = "批次编号不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{8}$", message = "批次编号必须为8位数字+字母组合")
    private String batchNo;

    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100")
    private String productName;

    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须为正数")
    private Double quantity;

    @Size(max = 50, message = "单位长度不能超过50")
    private String unit;

    @NotNull(message = "保质期不能为空")
    @Future(message = "保质期必须是未来日期")
    private LocalDate shelfLife;

    @Size(max = 100, message = "客户名称长度不能超过100")
    private String customerName;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "客户手机号格式不正确")
    private String customerPhone;

    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;

    private Long operatorId;
}
