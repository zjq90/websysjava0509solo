package com.referee.service;

import com.referee.common.Result;
import com.referee.dto.LoginRequest;
import com.referee.entity.User;
import com.referee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 认证服务类
 *
 * @author Referee System
 * @version 1.0.0
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 登录验证
     *
     * @param request 登录请求
     * @return 登录结果
     */
    public Result<Map<String, Object>> login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        
        if (!userOptional.isPresent()) {
            return Result.error("用户名不存在");
        }
        
        User user = userOptional.get();
        
        if (!user.getPassword().equals(request.getPassword())) {
            return Result.error("密码错误");
        }
        
        if (user.getStatus() != 1) {
            return Result.error("账号已被禁用");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("realName", user.getRealName());
        data.put("role", user.getRole());
        data.put("refereeId", user.getRefereeId());
        data.put("athleteId", user.getAthleteId());
        
        return Result.success(data);
    }

    /**
     * 重置密码（管理员功能）
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     * @return 操作结果
     */
    public Result<String> resetPassword(Long userId, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (!userOptional.isPresent()) {
            return Result.error("用户不存在");
        }
        
        User user = userOptional.get();
        
        if ("ADMIN".equals(user.getRole())) {
            return Result.error("不能重置管理员密码");
        }
        
        user.setPassword(newPassword);
        userRepository.save(user);
        
        return Result.success("密码重置成功");
    }
}
