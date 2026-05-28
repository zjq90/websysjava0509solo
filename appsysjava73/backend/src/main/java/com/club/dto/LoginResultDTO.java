package com.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录结果DTO
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录结果")
public class LoginResultDTO {

    @Schema(description = "Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "student001")
    private String username;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "头像", example = "https://example.com/avatar.png")
    private String avatar;

    @Schema(description = "角色", example = "USER")
    private String role;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;
}
