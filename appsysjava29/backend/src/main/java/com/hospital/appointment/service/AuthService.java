package com.hospital.appointment.service;

import com.hospital.appointment.common.BusinessException;
import com.hospital.appointment.entity.User;
import com.hospital.appointment.repository.UserRepository;
import com.hospital.appointment.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Map<String, Object> login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        String token = jwtTokenUtil.generateToken(user.getId(), user.getUsername(), "USER");
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("elderMode", user.getElderMode());
        
        return result;
    }

    @Transactional
    public User register(String username, String password, String realName, String phone) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("用户名已存在");
        }
        
        if (userRepository.existsByPhone(phone)) {
            throw new BusinessException("手机号已注册");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRealName(realName);
        user.setPhone(phone);
        user.setStatus(1);
        user.setElderMode(0);
        
        return userRepository.save(user);
    }

    public User getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setPassword(null);
        return user;
    }

    @Transactional
    public User updateUserInfo(Long userId, User updateUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        if (updateUser.getRealName() != null) {
            user.setRealName(updateUser.getRealName());
        }
        if (updateUser.getPhone() != null) {
            user.setPhone(updateUser.getPhone());
        }
        if (updateUser.getEmail() != null) {
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getAvatar() != null) {
            user.setAvatar(updateUser.getAvatar());
        }
        if (updateUser.getGender() != null) {
            user.setGender(updateUser.getGender());
        }
        if (updateUser.getBirthday() != null) {
            user.setBirthday(updateUser.getBirthday());
        }
        if (updateUser.getAddress() != null) {
            user.setAddress(updateUser.getAddress());
        }
        
        return userRepository.save(user);
    }

    @Transactional
    public User toggleElderMode(Long userId, Integer elderMode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setElderMode(elderMode);
        return userRepository.save(user);
    }

    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}
