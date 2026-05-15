package com.teaching.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class PasswordDTO {

    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    private String code;

    private String email;
}
