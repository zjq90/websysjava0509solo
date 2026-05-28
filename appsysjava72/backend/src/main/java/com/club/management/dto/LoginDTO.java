package com.club.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录DTO
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@ApiModel("登录请求参数")
public class LoginDTO {

    @ApiModelProperty(value = "登录类型: phone/password/wechat/qq/campus", required = true)
    @NotBlank(message = "登录类型不能为空")
    private String loginType;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("微信openid")
    private String openid;

    @ApiModelProperty("QQ openid")
    private String qqOpenid;

    @ApiModelProperty("校园统一认证token")
    private String campusToken;
}
