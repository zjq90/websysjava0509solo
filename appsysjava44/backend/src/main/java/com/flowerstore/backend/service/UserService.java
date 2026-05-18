package com.flowerstore.backend.service;

import com.flowerstore.backend.dto.LoginRequest;
import com.flowerstore.backend.dto.LoginResponse;
import com.flowerstore.backend.dto.RegisterRequest;
import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.*;
import com.flowerstore.backend.repository.*;
import com.flowerstore.backend.util.JWTUtil;
import com.flowerstore.backend.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 用户服务类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmsCodeRepository smsCodeRepository;

    @Autowired
    private MemberLevelRepository memberLevelRepository;

    @Autowired
    private PointRecordRepository pointRecordRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RSAUtil rsaUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户登录
     */
    public Result<LoginResponse> login(LoginRequest request) {
        User user = null;
        boolean isNewUser = false;

        switch (request.getLoginType()) {
            case "password":
                user = loginByPassword(request);
                break;
            case "sms":
                user = loginBySms(request);
                break;
            case "wechat":
                user = loginByWechat(request);
                if (user == null) {
                    isNewUser = true;
                    user = registerByWechat(request);
                }
                break;
            default:
                return Result.error("不支持的登录类型");
        }

        if (user == null) {
            return Result.error("登录失败");
        }

        if (user.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(user);
        response.setIsNewUser(isNewUser);

        return Result.success("登录成功", response);
    }

    /**
     * 密码登录
     */
    private User loginByPassword(LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return null;
        }

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return user;
            }
        }

        if (request.getPhone() != null) {
            userOpt = userRepository.findByPhone(request.getPhone());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    return user;
                }
            }
        }

        return null;
    }

    /**
     * 短信验证码登录
     */
    private User loginBySms(LoginRequest request) {
        if (request.getPhone() == null || request.getSmsCode() == null) {
            return null;
        }

        if (!verifySmsCode(request.getPhone(), request.getSmsCode(), "login")) {
            return null;
        }

        Optional<User> userOpt = userRepository.findByPhone(request.getPhone());
        return userOpt.orElseGet(() -> createUserByPhone(request.getPhone()));
    }

    /**
     * 微信登录
     */
    private User loginByWechat(LoginRequest request) {
        if (request.getWxCode() == null) {
            return null;
        }

        String mockOpenid = "wx_" + request.getWxCode();
        Optional<User> userOpt = userRepository.findByWxOpenid(mockOpenid);
        return userOpt.orElse(null);
    }

    /**
     * 微信注册
     */
    private User registerByWechat(LoginRequest request) {
        User user = new User();
        String mockOpenid = "wx_" + request.getWxCode();
        user.setWxOpenid(mockOpenid);
        user.setUsername("wx_user_" + System.currentTimeMillis());
        user.setNickname("微信用户" + new Random().nextInt(10000));
        user.setMemberLevelId(1L);
        user.setCurrentPoints(100);
        user = userRepository.save(user);

        addPointRecord(user.getId(), 100, 4, "注册赠送积分", null);
        return user;
    }

    /**
     * 通过手机号创建用户
     */
    private User createUserByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setUsername("user_" + phone);
        user.setNickname("用户" + phone.substring(phone.length() - 4));
        user.setMemberLevelId(1L);
        user.setCurrentPoints(100);
        user = userRepository.save(user);

        addPointRecord(user.getId(), 100, 4, "注册赠送积分", null);
        return user;
    }

    /**
     * 用户注册
     */
    @Transactional
    public Result<User> register(RegisterRequest request) {
        switch (request.getRegisterType()) {
            case "phone":
                return registerByPhone(request);
            case "email":
                return registerByEmail(request);
            case "wechat":
                return registerByWechat2(request);
            default:
                return Result.error("不支持的注册类型");
        }
    }

    /**
     * 手机号注册
     */
    private Result<User> registerByPhone(RegisterRequest request) {
        if (request.getPhone() == null || request.getSmsCode() == null) {
            return Result.error("手机号和验证码不能为空");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            return Result.error("该手机号已注册");
        }

        if (!verifySmsCode(request.getPhone(), request.getSmsCode(), "register")) {
            return Result.error("验证码错误或已过期");
        }

        User user = new User();
        user.setPhone(request.getPhone());
        user.setUsername(request.getUsername() != null ? request.getUsername() : "user_" + request.getPhone());
        user.setNickname(request.getNickname() != null ? request.getNickname() : "用户" + request.getPhone().substring(request.getPhone().length() - 4));
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setMemberLevelId(1L);
        user.setCurrentPoints(100);
        user = userRepository.save(user);

        addPointRecord(user.getId(), 100, 4, "注册赠送积分", null);
        return Result.success("注册成功", user);
    }

    /**
     * 邮箱注册
     */
    private Result<User> registerByEmail(RegisterRequest request) {
        if (request.getEmail() == null) {
            return Result.error("邮箱不能为空");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return Result.error("该邮箱已注册");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername() != null ? request.getUsername() : "user_" + System.currentTimeMillis());
        user.setNickname(request.getNickname() != null ? request.getNickname() : "新用户");
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setMemberLevelId(1L);
        user.setCurrentPoints(100);
        user = userRepository.save(user);

        addPointRecord(user.getId(), 100, 4, "注册赠送积分", null);
        return Result.success("注册成功", user);
    }

    /**
     * 微信注册2
     */
    private Result<User> registerByWechat2(RegisterRequest request) {
        if (request.getWxCode() == null) {
            return Result.error("微信授权不能为空");
        }

        String mockOpenid = "wx_" + request.getWxCode();
        if (userRepository.existsByWxOpenid(mockOpenid)) {
            return Result.error("该微信已注册");
        }

        User user = new User();
        user.setWxOpenid(mockOpenid);
        user.setUsername("wx_user_" + System.currentTimeMillis());
        user.setNickname(request.getNickname() != null ? request.getNickname() : "微信用户");
        user.setAvatar(request.getAvatar());
        user.setMemberLevelId(1L);
        user.setCurrentPoints(100);
        user = userRepository.save(user);

        addPointRecord(user.getId(), 100, 4, "注册赠送积分", null);
        return Result.success("注册成功", user);
    }

    /**
     * 发送短信验证码
     */
    public Result<String> sendSmsCode(String phone, String type) {
        String code = String.format("%06d", new Random().nextInt(1000000));

        SmsCode smsCode = new SmsCode();
        smsCode.setPhone(phone);
        smsCode.setCode(code);
        smsCode.setType(type);
        smsCode.setExpireTime(LocalDateTime.now().plusMinutes(5));
        smsCodeRepository.save(smsCode);

        log.info("发送短信验证码 - 手机号：{}，验证码：{}，类型：{}", phone, code, type);
        return Result.success("验证码发送成功", code);
    }

    /**
     * 验证短信验证码
     */
    private boolean verifySmsCode(String phone, String code, String type) {
        List<SmsCode> list = smsCodeRepository.findByPhoneAndTypeAndIsUsedOrderByCreateTimeDesc(phone, type, 0);
        if (list.isEmpty()) {
            return false;
        }

        SmsCode smsCode = list.get(0);
        if (!smsCode.getCode().equals(code)) {
            return false;
        }

        if (smsCode.getExpireTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        smsCode.setIsUsed(1);
        smsCodeRepository.save(smsCode);
        return true;
    }

    /**
     * 获取用户信息
     */
    public Result<User> getUserInfo(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Result.error("用户不存在");
        }
        return Result.success(userOpt.get());
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public Result<User> updateUserInfo(Long userId, User user) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Result.error("用户不存在");
        }

        User existUser = userOpt.get();
        if (user.getNickname() != null) existUser.setNickname(user.getNickname());
        if (user.getAvatar() != null) existUser.setAvatar(user.getAvatar());
        if (user.getGender() != null) existUser.setGender(user.getGender());
        if (user.getBirthday() != null) existUser.setBirthday(user.getBirthday());
        if (user.getRealName() != null) {
            existUser.setRealName(rsaUtil.encryptSensitiveData(user.getRealName()));
        }
        if (user.getIdCard() != null) {
            existUser.setIdCard(rsaUtil.encryptSensitiveData(user.getIdCard()));
        }

        userRepository.save(existUser);
        return Result.success("更新成功", existUser);
    }

    /**
     * 切换长辈模式
     */
    @Transactional
    public Result<User> toggleElderMode(Long userId, Integer elderMode) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Result.error("用户不存在");
        }

        User user = userOpt.get();
        user.setElderMode(elderMode);
        userRepository.save(user);
        return Result.success(elderMode == 1 ? "已开启长辈模式" : "已关闭长辈模式", user);
    }

    /**
     * 添加积分记录
     */
    private void addPointRecord(Long userId, Integer points, Integer type, String description, Long orderId) {
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(type);
        record.setDescription(description);
        record.setOrderId(orderId);
        pointRecordRepository.save(record);
    }

    /**
     * 获取RSA公钥
     */
    public Result<String> getRSAPublicKey() {
        return Result.success(rsaUtil.getPublicKeyStr());
    }
}
