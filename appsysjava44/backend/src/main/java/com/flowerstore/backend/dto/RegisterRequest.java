package com.flowerstore.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 注册请求DTO
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@Schema(description = "注册请求")
public class RegisterRequest {

    @Schema(description = "注册类型：phone-手机号注册，email-邮箱注册，wechat-微信注册")
    private String registerType;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "短信验证码")
    private String smsCode;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "微信Code")
    private String wxCode;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;
}
