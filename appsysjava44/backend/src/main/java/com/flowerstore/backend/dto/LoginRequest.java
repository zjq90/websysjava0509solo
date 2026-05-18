package com.flowerstore.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录请求DTO
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {

    @Schema(description = "登录类型：password-密码登录，sms-短信验证码登录，wechat-微信登录")
    private String loginType;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "短信验证码")
    private String smsCode;

    @Schema(description = "微信Code")
    private String wxCode;

    @Schema(description = "邮箱")
    private String email;
}
