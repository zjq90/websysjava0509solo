package com.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录DTO
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "登录请求参数")
public class LoginDTO {

    @Schema(description = "用户名", example = "student001", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "123456", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
