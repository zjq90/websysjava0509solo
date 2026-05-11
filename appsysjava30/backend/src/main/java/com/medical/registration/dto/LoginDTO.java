package com.medical.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "登录请求")
public class LoginDTO {
    
    @Schema(description = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @Schema(description = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;
}
