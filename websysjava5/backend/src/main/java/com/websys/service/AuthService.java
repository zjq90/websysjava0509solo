package com.websys.service;

import com.websys.config.JwtUtil;
import com.websys.dto.LoginRequest;
import com.websys.dto.LoginResponse;
import com.websys.entity.User;
import com.websys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 认证服务类
 * 
 * @author websys
 * @version 1.0.0
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 用户登录
     * 
     * @param request 登录请求
     * @param ipAddress IP地址
     * @return 登录响应
     */
    @Transactional
    public LoginResponse login(LoginRequest request, String ipAddress) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        
        if (!userOpt.isPresent()) {
            operationLogService.saveLog(
                null,
                request.getUsername(),
                "LOGIN",
                "AUTH",
                "登录失败：用户不存在",
                null,
                null,
                ipAddress,
                0,
                "用户不存在"
            );
            throw new RuntimeException("用户名或密码错误");
        }

        User user = userOpt.get();
        
        if (user.getStatus() != 1) {
            operationLogService.saveLog(
                user.getId(),
                user.getUsername(),
                "LOGIN",
                "AUTH",
                "登录失败：用户已被禁用",
                null,
                null,
                ipAddress,
                0,
                "用户已被禁用"
            );
            throw new RuntimeException("用户已被禁用");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            operationLogService.saveLog(
                user.getId(),
                user.getUsername(),
                "LOGIN",
                "AUTH",
                "登录失败：密码错误",
                null,
                null,
                ipAddress,
                0,
                "密码错误"
            );
            throw new RuntimeException("用户名或密码错误");
        }

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(
            user.getUsername(),
            user.getId(),
            user.getRoleType(),
            user.getAgentId()
        );

        operationLogService.saveLog(
            user.getId(),
            user.getUsername(),
            "LOGIN",
            "AUTH",
            "登录成功",
            null,
            null,
            ipAddress,
            1,
            null
        );

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setRoleType(user.getRoleType());
        response.setAgentId(user.getAgentId());

        return response;
    }

    /**
     * 获取当前用户信息
     * 
     * @param username 用户名
     * @return 用户信息
     */
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
}
