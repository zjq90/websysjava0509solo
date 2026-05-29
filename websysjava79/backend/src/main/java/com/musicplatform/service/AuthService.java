package com.musicplatform.service;

import com.musicplatform.dto.AuthDTO.*;
import com.musicplatform.entity.User;
import com.musicplatform.entity.VerificationCode;
import com.musicplatform.repository.UserRepository;
import com.musicplatform.repository.VerificationCodeRepository;
import com.musicplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("手机号已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setRole(User.Role.USER);
        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole().name());
        response.setVip(user.isVip());

        return response;
    }

    public LoginResponse login(LoginRequest request) {
        User user = null;

        switch (request.getLoginType()) {
            case "username":
                user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    throw new RuntimeException("密码错误");
                }
                break;
            case "email":
                user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    throw new RuntimeException("密码错误");
                }
                break;
            case "phone":
                user = userRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
                verifyCode(request.getPhone(), VerificationCode.CodeType.LOGIN, request.getCode());
                break;
            case "wechat":
                user = handleWechatLogin(request.getWechatCode());
                break;
            case "qq":
                user = handleQqLogin(request.getQqCode());
                break;
            default:
                throw new RuntimeException("不支持的登录方式");
        }

        if (user.isBlocked()) {
            throw new RuntimeException("账号已被封禁");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole().name());
        response.setVip(user.isVip());

        return response;
    }

    @Transactional
    public void sendVerificationCode(String target, String typeStr) {
        VerificationCode.CodeType type = VerificationCode.CodeType.valueOf(typeStr);
        String code = generateVerificationCode();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setTarget(target);
        verificationCode.setType(type);
        verificationCode.setCode(code);
        verificationCodeRepository.save(verificationCode);

        String redisKey = "verification:" + type + ":" + target;
        redisTemplate.opsForValue().set(redisKey, code, 5, TimeUnit.MINUTES);

        System.out.println("发送验证码到 " + target + ": " + code);
    }

    private void verifyCode(String target, VerificationCode.CodeType type, String code) {
        String redisKey = "verification:" + type + ":" + target;
        String savedCode = (String) redisTemplate.opsForValue().get(redisKey);
        
        if (savedCode == null || !savedCode.equals(code)) {
            throw new RuntimeException("验证码错误或已过期");
        }
        
        redisTemplate.delete(redisKey);
    }

    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        User user;
        if (request.getEmail() != null) {
            user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            verifyCode(request.getEmail(), VerificationCode.CodeType.PASSWORD_RESET, request.getVerificationCode());
        } else if (request.getPhone() != null) {
            user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            verifyCode(request.getPhone(), VerificationCode.CodeType.PASSWORD_RESET, request.getVerificationCode());
        } else {
            throw new RuntimeException("请提供邮箱或手机号");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private User handleWechatLogin(String code) {
        String mockOpenId = "wx_" + UUID.randomUUID().toString().substring(0, 8);
        
        return userRepository.findByWechatOpenId(mockOpenId)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setUsername("wx_user_" + System.currentTimeMillis());
                newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                newUser.setWechatOpenId(mockOpenId);
                newUser.setNickname("微信用户");
                newUser.setRole(User.Role.USER);
                return userRepository.save(newUser);
            });
    }

    private User handleQqLogin(String code) {
        String mockOpenId = "qq_" + UUID.randomUUID().toString().substring(0, 8);
        
        return userRepository.findByQqOpenId(mockOpenId)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setUsername("qq_user_" + System.currentTimeMillis());
                newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                newUser.setQqOpenId(mockOpenId);
                newUser.setNickname("QQ用户");
                newUser.setRole(User.Role.USER);
                return userRepository.save(newUser);
            });
    }
}
