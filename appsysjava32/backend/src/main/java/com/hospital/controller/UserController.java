package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.entity.User;
import com.hospital.repository.UserRepository;
import com.hospital.util.JwtUtil;
import com.hospital.util.SM4Util;
import com.hospital.util.SensitiveDataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户管理", description = "用户信息相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "更新当前用户信息")
    @PutMapping("/profile")
    public Result<User> updateProfile(
            @RequestBody UpdateProfileRequest request,
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        return userRepository.findById(userId)
                .<Result<User>>map(user -> {
                    if (request.getRealName() != null) user.setRealName(request.getRealName());
                    if (request.getPhone() != null) user.setPhone(SM4Util.encryptSensitive(request.getPhone()));
                    if (request.getGender() != null) user.setGender(request.getGender());
                    if (request.getAge() != null) user.setAge(request.getAge());
                    if (request.getAddress() != null) user.setAddress(request.getAddress());
                    if (request.getEmail() != null) user.setEmail(request.getEmail());
                    if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
                    User saved = userRepository.save(user);
                    saved.setPhone(SensitiveDataUtil.maskPhone(SM4Util.decryptSensitive(saved.getPhone())));
                    saved.setIdCard(SensitiveDataUtil.maskIdCard(SM4Util.decryptSensitive(saved.getIdCard())));
                    saved.setPassword(null);
                    return Result.success("更新成功", saved);
                })
                .orElse(Result.<User>notFound());
    }

    @Operation(summary = "切换长辈模式")
    @PutMapping("/elder-mode")
    public Result<Map<String, Object>> toggleElderMode(
            @RequestParam Integer mode,
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        return userRepository.findById(userId)
                .<Result<Map<String, Object>>>map(user -> {
                    user.setElderMode(mode);
                    userRepository.save(user);
                    Map<String, Object> result = new HashMap<>();
                    result.put("elderMode", mode);
                    result.put("message", mode == 1 ? "已开启长辈模式，字体已放大" : "已关闭长辈模式");
                    return Result.success(result);
                })
                .orElse(Result.<Map<String, Object>>notFound());
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(
            @RequestBody ChangePasswordRequest request,
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        return userRepository.findById(userId)
                .<Result<Void>>map(user -> {
                    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                        return Result.<Void>error("原密码不正确");
                    }
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    userRepository.save(user);
                    return Result.<Void>success();
                })
                .orElse(Result.<Void>notFound());
    }

    @Operation(summary = "获取用户长辈模式设置")
    @GetMapping("/elder-mode")
    public Result<Map<String, Object>> getElderMode(
            @RequestHeader(value = "Authorization") String token) {
        
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return Result.unauthorized();
        }

        return userRepository.findById(userId)
                .<Result<Map<String, Object>>>map(user -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("elderMode", user.getElderMode());
                    return Result.success(result);
                })
                .orElse(Result.<Map<String, Object>>notFound());
    }

    private Long getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            return jwtUtil.getUserIdFromToken(jwt);
        }
        return null;
    }

    @Data
    public static class UpdateProfileRequest {
        private String realName;
        private String phone;
        private String gender;
        private Integer age;
        private String address;
        private String email;
        private String avatar;
    }

    @Data
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }
}
