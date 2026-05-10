package com.websys.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 * 
 * @author websys
 * @version 1.0.0
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
