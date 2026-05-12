package com.hospital.controller;

import com.hospital.common.Result;
import com.hospital.dto.LoginRequest;
import com.hospital.dto.LoginResponse;
import com.hospital.entity.SysPermission;
import com.hospital.entity.SysRole;
import com.hospital.entity.SysUser;
import com.hospital.repository.SysUserRepository;
import com.hospital.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 认证控制器
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户登录认证相关接口")
public class AuthController {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户使用用户名和密码登录系统")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        Optional<SysUser> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        
        if (!userOptional.isPresent()) {
            return Result.fail("用户名或密码错误");
        }
        
        SysUser user = userOptional.get();
        
        if (user.getStatus() == 0) {
            return Result.fail("用户已被禁用");
        }
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return Result.fail("用户名或密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getUsername());
        LoginResponse response = new LoginResponse(token, user.getUsername(), user.getRealName());
        
        return Result.success("登录成功", response);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/userinfo")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户信息")
    public Result<SysUser> getUserInfo(@RequestHeader("Authorization") String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            Optional<SysUser> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                SysUser user = userOptional.get();
                user.setPassword(null);
                return Result.success(user);
            }
        }
        return Result.unauthorized("未登录");
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录")
    public Result<Void> logout() {
        return Result.success("登出成功", null);
    }

    /**
     * 获取当前用户权限信息
     */
    @GetMapping("/permissions")
    @Operation(summary = "获取用户权限", description = "获取当前登录用户的角色和权限列表")
    public Result<Map<String, Object>> getUserPermissions(@RequestHeader("Authorization") String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);
            Optional<SysUser> userOptional = userRepository.findByUsername(username);
            
            if (userOptional.isPresent()) {
                SysUser user = userOptional.get();
                Map<String, Object> result = new HashMap<>();
                result.put("username", user.getUsername());
                result.put("realName", user.getRealName());
                
                // 获取角色列表
                Set<String> roles = new HashSet<>();
                Set<String> permissions = new HashSet<>();
                
                if (user.getRoles() != null) {
                    for (SysRole role : user.getRoles()) {
                        roles.add(role.getRoleCode());
                        // 获取角色的权限
                        if (role.getPermissions() != null) {
                            for (SysPermission permission : role.getPermissions()) {
                                permissions.add(permission.getPermissionCode());
                            }
                        }
                    }
                }
                
                result.put("roles", roles);
                result.put("permissions", permissions);
                return Result.success(result);
            }
        }
        return Result.unauthorized("未登录");
    }
}
