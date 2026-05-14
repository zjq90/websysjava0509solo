package com.broadband.service;

import com.broadband.entity.User;
import com.broadband.repository.UserRepository;
import com.broadband.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务
 * 处理用户登录、验证码等认证相关功能
 * 
 * @author broadband
 * @version 1.0.0
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";

    public String sendSmsCode(String phone) throws Exception {
        String encryptedPhone = RSAUtil.encrypt(phone);
        String code = generateRandomCode(6);
        
        redisTemplate.opsForValue().set(SMS_CODE_PREFIX + phone, code, 5, TimeUnit.MINUTES);
        
        System.out.println("发送验证码到手机: " + phone + ", 验证码: " + code);
        
        return code;
    }

    public boolean verifySmsCode(String phone, String code) {
        String savedCode = redisTemplate.opsForValue().get(SMS_CODE_PREFIX + phone);
        return code.equals(savedCode);
    }

    public Map<String, Object> loginByPhone(String phone, String code) throws Exception {
        if (!verifySmsCode(phone, code)) {
            throw new RuntimeException("验证码错误或已过期");
        }

        String encryptedPhone = RSAUtil.encrypt(phone);
        User user = userRepository.findByPhone(encryptedPhone).orElse(null);

        if (user == null) {
            user = new User();
            user.setPhone(encryptedPhone);
            user.setStatus(1);
            user.setRealNameStatus(0);
            user.setCreateTime(LocalDateTime.now());
            user = userRepository.save(user);
        }

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("token", "token_" + user.getId() + "_" + System.currentTimeMillis());
        result.put("user", user);

        return result;
    }

    public Map<String, Object> loginByWechat(String wxOpenId, String name, String avatar) {
        User user = userRepository.findByWxOpenId(wxOpenId).orElse(null);

        if (user == null) {
            user = new User();
            user.setWxOpenId(wxOpenId);
            user.setName(name);
            user.setAvatar(avatar);
            user.setStatus(1);
            user.setRealNameStatus(0);
            user.setCreateTime(LocalDateTime.now());
            user = userRepository.save(user);
        }

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("token", "token_" + user.getId() + "_" + System.currentTimeMillis());
        result.put("user", user);

        return result;
    }

    public Map<String, Object> loginByAlipay(String alipayUserId, String name, String avatar) {
        User user = userRepository.findByAlipayUserId(alipayUserId).orElse(null);

        if (user == null) {
            user = new User();
            user.setAlipayUserId(alipayUserId);
            user.setName(name);
            user.setAvatar(avatar);
            user.setStatus(1);
            user.setRealNameStatus(0);
            user.setCreateTime(LocalDateTime.now());
            user = userRepository.save(user);
        }

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("token", "token_" + user.getId() + "_" + System.currentTimeMillis());
        result.put("user", user);

        return result;
    }

    public Map<String, Object> faceLogin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("token", "token_" + user.getId() + "_" + System.currentTimeMillis());
        result.put("user", user);

        return result;
    }

    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
