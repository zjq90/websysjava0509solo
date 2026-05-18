package com.secondhand.service;

import com.secondhand.common.Result;
import com.secondhand.entity.User;
import com.secondhand.repository.UserRepository;
import com.secondhand.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RSAUtil rsaUtil;

    public Result<Map<String, Object>> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return Result.error("用户名已存在");
        }
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            return Result.error("手机号已注册");
        }

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setNickname(user.getUsername());
        user.setStatus(1);
        user.setIsVerified(false);

        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);

        Map<String, Object> data = new HashMap<>();
        data.put("user", savedUser);

        return Result.success("注册成功", data);
    }

    public Result<Map<String, Object>> login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return Result.error("用户不存在");
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return Result.error("密码错误");
        }

        if (user.getStatus() != 1) {
            return Result.error("账号已被禁用");
        }

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("user:token:" + token, user.getId(), 7, TimeUnit.DAYS);

        user.setPassword(null);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);

        return Result.success("登录成功", data);
    }

    public Result<User> getById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return Result.error("用户不存在");
        }
        User user = userOptional.get();
        user.setPassword(null);
        return Result.success(user);
    }

    public Result<User> update(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return Result.error("用户不存在");
        }

        User existUser = userOptional.get();
        if (user.getNickname() != null) existUser.setNickname(user.getNickname());
        if (user.getAvatar() != null) existUser.setAvatar(user.getAvatar());
        if (user.getPhone() != null) existUser.setPhone(user.getPhone());
        if (user.getEmail() != null) existUser.setEmail(user.getEmail());
        if (user.getLocation() != null) existUser.setLocation(user.getLocation());
        if (user.getLatitude() != null) existUser.setLatitude(user.getLatitude());
        if (user.getLongitude() != null) existUser.setLongitude(user.getLongitude());
        if (user.getBio() != null) existUser.setBio(user.getBio());
        if (user.getElderMode() != null) existUser.setElderMode(user.getElderMode());
        if (user.getPushEnabled() != null) existUser.setPushEnabled(user.getPushEnabled());

        User savedUser = userRepository.save(existUser);
        savedUser.setPassword(null);

        return Result.success("更新成功", savedUser);
    }
}
