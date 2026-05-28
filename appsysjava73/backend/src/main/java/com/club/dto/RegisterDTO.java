package com.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册DTO
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Schema(description = "注册请求参数")
public class RegisterDTO {

    @Schema(description = "用户名", example = "student001", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "123456", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "真实姓名", example = "张三", required = true)
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @Schema(description = "学号", example = "2021001001")
    private String studentNo;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "邮箱", example = "student@example.com")
    private String email;

    @Schema(description = "学校ID", example = "1", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "院系", example = "计算机科学与技术学院")
    private String college;

    @Schema(description = "专业", example = "软件工程")
    private String major;

    @Schema(description = "年级", example = "2021级")
    private String grade;
}
