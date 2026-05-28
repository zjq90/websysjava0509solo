package com.club.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 注册DTO
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@ApiModel("注册请求参数")
public class RegisterDTO {

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("院系")
    private String college;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("年级")
    private String grade;
}
