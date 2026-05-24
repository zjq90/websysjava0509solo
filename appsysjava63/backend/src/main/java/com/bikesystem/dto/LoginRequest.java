package com.bikesystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {

    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号", required = true)
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", required = true)
    private String password;
}
