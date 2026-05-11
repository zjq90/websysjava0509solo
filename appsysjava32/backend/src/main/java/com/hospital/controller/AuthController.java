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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "认证管理", description = "登录登出相关接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return Result.error(401, "用户名或密码错误");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        if (user.getStatus() != 1) {
            return Result.error(403, "账号已被禁用，请联系管理员");
        }

        String roles = user.getRoles().stream()
                .map(role -> role.getRoleCode())
                .collect(Collectors.joining(","));

        final String jwt = jwtUtil.generateToken(user.getId(), user.getUsername(), roles);

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);
        result.put("tokenType", "Bearer");
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("realName", user.getRealName());
        userInfo.put("phone", SensitiveDataUtil.maskPhone(SM4Util.decryptSensitive(user.getPhone())));
        userInfo.put("gender", user.getGender());
        userInfo.put("age", user.getAge());
        userInfo.put("patientNo", user.getPatientNo());
        userInfo.put("elderMode", user.getElderMode());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("roles", roles);
        
        result.put("user", userInfo);

        return Result.success("登录成功", result);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return Result.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setPhone(SM4Util.encryptSensitive(request.getPhone()));
        user.setIdCard(SM4Util.encryptSensitive(request.getIdCard()));
        user.setGender(request.getGender());
        user.setAge(request.getAge());
        user.setAddress(request.getAddress());
        user.setPatientNo("P" + System.currentTimeMillis());
        user.setStatus(1);
        user.setCreateBy("self");
        user.setElderMode(0);

        userRepository.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("patientNo", user.getPatientNo());

        return Result.success("注册成功", result);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<User> getCurrentUser(@RequestHeader(value = "Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            String username = jwtUtil.getUsernameFromToken(jwt);
            return userRepository.findByUsername(username)
                    .<Result<User>>map(user -> {
                        user.setPhone(SensitiveDataUtil.maskPhone(SM4Util.decryptSensitive(user.getPhone())));
                        user.setIdCard(SensitiveDataUtil.maskIdCard(SM4Util.decryptSensitive(user.getIdCard())));
                        user.setPassword(null);
                        return Result.success(user);
                    })
                    .orElse(Result.<User>notFound());
        }
        return Result.unauthorized();
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String realName;
        private String phone;
        private String idCard;
        private String gender;
        private Integer age;
        private String address;
    }
}
