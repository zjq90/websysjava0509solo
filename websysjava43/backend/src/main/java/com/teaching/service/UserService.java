package com.teaching.service;

import com.teaching.dto.LoginDTO;
import com.teaching.dto.RegisterDTO;
import com.teaching.entity.User;
import com.teaching.repository.UserRepository;
import com.teaching.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public Map<String, Object> login(LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByUsername(loginDTO.getUsername());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        User user = userOptional.get();
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    @Transactional
    public User register(RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (registerDTO.getEmail() != null && userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setEmail(registerDTO.getEmail());
        user.setRealName(registerDTO.getRealName());
        user.setRole(1);
        user.setStatus(1);
        user.setEmailBound(registerDTO.getEmail() != null);
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Transactional
    public User updateProfile(Long userId, User user) {
        User existingUser = getById(userId);
        if (user.getRealName() != null) existingUser.setRealName(user.getRealName());
        if (user.getGender() != null) existingUser.setGender(user.getGender());
        if (user.getPhone() != null) existingUser.setPhone(user.getPhone());
        if (user.getAvatar() != null) existingUser.setAvatar(user.getAvatar());
        if (user.getSignature() != null) existingUser.setSignature(user.getSignature());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Transactional
    public User bindEmail(Long userId, String email) {
        User user = getById(userId);
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("邮箱已被其他账号绑定");
        }
        user.setEmail(email);
        user.setEmailBound(true);
        return userRepository.save(user);
    }

    @Transactional
    public User unbindEmail(Long userId) {
        User user = getById(userId);
        user.setEmail(null);
        user.setEmailBound(false);
        return userRepository.save(user);
    }
}
