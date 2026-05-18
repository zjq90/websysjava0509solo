package com.secondhand.service;

import com.secondhand.entity.User;
import com.secondhand.repository.UserRepository;
import com.secondhand.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RSAUtil rsaUtil;

    /**
     * 用户注册
     */
    public User register(String username, String password, String nickname) throws Exception {
        if (userRepository.existsByUsernameAndIsDeletedFalse(username)) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        user.setNickname(nickname != null ? nickname : username);
        user.setAvatar("https://via.placeholder.com/150");
        user.setPoints(100);

        return userRepository.save(user);
    }

    /**
     * 用户登录
     */
    public Map<String, Object> login(String username, String password) throws Exception {
        Optional<User> userOpt = userRepository.findByUsernameAndIsDeletedFalse(username);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            throw new RuntimeException("密码错误");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("user:token:" + token, user.getId(), 7, TimeUnit.DAYS);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    /**
     * 根据token获取用户
     */
    public User getUserByToken(String token) {
        Object userId = redisTemplate.opsForValue().get("user:token:" + token);
        if (userId == null) {
            return null;
        }
        return userRepository.findByIdAndIsDeletedFalse((Long) userId).orElse(null);
    }

    /**
     * 根据ID获取用户
     */
    public User getUserById(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
    }

    /**
     * 更新用户信息
     */
    public User updateUser(Long id, User user) {
        User existUser = userRepository.findByIdAndIsDeletedFalse(id).orElse(null);
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }

        if (user.getNickname() != null) existUser.setNickname(user.getNickname());
        if (user.getAvatar() != null) existUser.setAvatar(user.getAvatar());
        if (user.getPhone() != null) existUser.setPhone(user.getPhone());
        if (user.getGender() != null) existUser.setGender(user.getGender());
        if (user.getAge() != null) existUser.setAge(user.getAge());
        if (user.getLatitude() != null) existUser.setLatitude(user.getLatitude());
        if (user.getLongitude() != null) existUser.setLongitude(user.getLongitude());
        if (user.getAddress() != null) existUser.setAddress(user.getAddress());
        if (user.getElderMode() != null) existUser.setElderMode(user.getElderMode());

        return userRepository.save(existUser);
    }

    /**
     * 每日签到
     */
    public int dailySign(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        LocalDate today = LocalDate.now();
        if (user.getLastSignDate() != null && user.getLastSignDate().toLocalDate().equals(today)) {
            throw new RuntimeException("今日已签到");
        }

        int points = 10;
        int signDays = user.getSignDays() + 1;

        if (signDays >= 7) {
            points = 30;
            signDays = 0;
        } else if (signDays >= 3) {
            points = 20;
        }

        user.setPoints(user.getPoints() + points);
        user.setLastSignDate(LocalDateTime.now());
        user.setSignDays(signDays);
        userRepository.save(user);

        return points;
    }

    /**
     * 获取签到状态
     */
    public Map<String, Object> getSignStatus(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElse(null);
        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("signed", false);
            result.put("signDays", 0);
            return result;
        }

        LocalDate today = LocalDate.now();
        boolean signed = user.getLastSignDate() != null && user.getLastSignDate().toLocalDate().equals(today);

        result.put("signed", signed);
        result.put("signDays", user.getSignDays());
        result.put("points", user.getPoints());
        return result;
    }

}
