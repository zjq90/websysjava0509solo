package com.appsys.order.dto;

import com.appsys.common.validator.ChinesePhone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 客户信息DTO
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@Schema(description = "客户信息")
public class CustomerDTO {

    @Schema(description = "客户名称")
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    @Schema(description = "手机号")
    @ChinesePhone
    private String phone;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "客户类型")
    private String customerType;

    @Schema(description = "备注")
    private String remark;
}
