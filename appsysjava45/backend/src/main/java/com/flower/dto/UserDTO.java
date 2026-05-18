package com.flower.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户DTO
 */
@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String email;

    private String phone;

    private String nickname;

    private String avatar;

    private String gender;

    private Integer age;

    private String address;

    private Integer points;
}