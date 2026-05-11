package com.medical.appointment.service;

import com.medical.appointment.entity.SmsCode;
import com.medical.appointment.entity.User;
import com.medical.appointment.repository.SmsCodeRepository;
import com.medical.appointment.repository.UserRepository;
import com.medical.appointment.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmsCodeRepository smsCodeRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuditLogService auditLogService;

    public String generateSmsCode(String phone, SmsCode.CodeType type) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        long count = smsCodeRepository.countByPhoneAndTypeAndCreatedAtAfter(phone, type, oneMinuteAgo);
        if (count > 0) {
            throw new RuntimeException("验证码发送过于频繁，请稍后再试");
        }

        String code = String.format("%06d", new Random().nextInt(1000000));
        SmsCode smsCode = SmsCode.builder()
                .phone(phone)
                .code(code)
                .type(type)
                .expireAt(LocalDateTime.now().plusMinutes(5))
                .build();
        smsCodeRepository.save(smsCode);

        return code;
    }

    @Transactional
    public Map<String, Object> loginWithSms(String phone, String code) {
        SmsCode smsCode = smsCodeRepository.findFirstByPhoneAndTypeAndIsUsedFalseOrderByCreatedAtDesc(
                phone, SmsCode.CodeType.LOGIN)
                .orElseThrow(() -> new RuntimeException("验证码不存在或已过期"));

        if (!smsCode.isValid(code)) {
            throw new RuntimeException("验证码错误或已过期");
        }

        smsCode.setIsUsed(true);
        smsCodeRepository.save(smsCode);

        User user = userRepository.findByPhone(phone).orElse(null);
        if (user == null) {
            user = User.builder()
                    .phone(phone)
                    .nickname("用户" + phone.substring(phone.length() - 4))
                    .build();
            user = userRepository.save(user);
        }

        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new RuntimeException("账号已被禁用");
        }

        String token = jwtTokenUtil.generateToken(user.getId(), user.getPhone(), user.getRole().name());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", convertToUserVO(user));

        auditLogService.logLogin(user.getId(), phone, "手机号验证码登录");

        return result;
    }

    @Transactional
    public Map<String, Object> loginWithWechat(String code, String nickname, String avatar) {
        String mockOpenId = "wx_" + System.currentTimeMillis();
        String mockUnionId = "wx_union_" + System.currentTimeMillis();

        User user = userRepository.findByWechatOpenId(mockOpenId).orElse(null);
        if (user == null) {
            String phone = "wx_" + System.currentTimeMillis();
            user = User.builder()
                    .phone(phone)
                    .nickname(nickname != null ? nickname : "微信用户")
                    .avatar(avatar)
                    .wechatOpenId(mockOpenId)
                    .wechatUnionId(mockUnionId)
                    .build();
            user = userRepository.save(user);
        }

        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new RuntimeException("账号已被禁用");
        }

        String token = jwtTokenUtil.generateToken(user.getId(), user.getPhone(), user.getRole().name());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", convertToUserVO(user));

        auditLogService.logLogin(user.getId(), user.getPhone(), "微信授权登录");

        return result;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Transactional
    public User updateProfile(Long userId, String nickname, String avatar) {
        User user = findById(userId);
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        user = userRepository.save(user);
        auditLogService.logUpdateProfile(userId, user.getPhone());
        return user;
    }

    @Transactional
    public void toggleElderlyMode(Long userId) {
        User user = findById(userId);
        user.setElderlyMode(!user.getElderlyMode());
        userRepository.save(user);
    }

    private Map<String, Object> convertToUserVO(User user) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", user.getId());
        vo.put("phone", user.getPhone());
        vo.put("nickname", user.getNickname());
        vo.put("avatar", user.getAvatar());
        vo.put("role", user.getRole().name());
        vo.put("elderlyMode", user.getElderlyMode());
        return vo;
    }
}
