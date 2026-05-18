package com.flowerstore.backend.dto;

import com.flowerstore.backend.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录响应DTO
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "JWT Token")
    private String token;

    @Schema(description = "用户信息")
    private User user;

    @Schema(description = "是否新用户")
    private Boolean isNewUser;
}
