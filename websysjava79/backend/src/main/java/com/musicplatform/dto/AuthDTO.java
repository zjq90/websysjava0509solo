package com.musicplatform.dto;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AuthDTO {

    @Data
    public static class LoginRequest {
        private String loginType;
        private String username;
        private String email;
        private String phone;
        private String password;
        private String code;
        private String wechatCode;
        private String qqCode;
    }

    @Data
    public static class RegisterRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;

        @Email
        private String email;

        private String phone;

        private String nickname;

        private String verificationCode;
        private String registerType;
    }

    @Data
    public static class LoginResponse {
        private String token;
        private Long userId;
        private String username;
        private String nickname;
        private String avatar;
        private String role;
        private boolean isVip;
    }

    @Data
    public static class PasswordResetRequest {
        private String email;
        private String phone;
        private String verificationCode;
        private String newPassword;
    }

    @Data
    public static class VerificationCodeRequest {
        private String target;
        private String type;
    }
}
