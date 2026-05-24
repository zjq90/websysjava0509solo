package com.bikesystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bikesystem.common.BusinessException;
import com.bikesystem.common.ResultCode;
import com.bikesystem.dto.LoginRequest;
import com.bikesystem.dto.LoginResponse;
import com.bikesystem.dto.RealNameVerifyRequest;
import com.bikesystem.dto.RegisterRequest;
import com.bikesystem.entity.*;
import com.bikesystem.mapper.*;
import com.bikesystem.utils.JwtUtils;
import com.bikesystem.utils.RedisUtils;
import com.bikesystem.utils.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private DepositRecordMapper depositRecordMapper;

    @Resource
    private CreditRecordMapper creditRecordMapper;

    @Resource
    private CouponMapper couponMapper;

    @Value("${bike.credit.initial-score:100}")
    private Integer initialCreditScore;

    @Value("${bike.deposit.amount:199.00}")
    private BigDecimal depositAmount;

    @Value("${bike.deposit.free-credit-score:650}")
    private Integer freeCreditScore;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 发送短信验证码
     */
    public Map<String, Object> sendSmsCode(String phone) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        String key = "sms:code:" + phone;
        redisUtils.set(key, code, 5, TimeUnit.MINUTES);
        
        Map<String, Object> result = new HashMap<>();
        result.put("phone", phone);
        result.put("expireMinutes", 5);
        result.put("code", code);
        return result;
    }

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse register(RegisterRequest request) {
        String key = "sms:code:" + request.getPhone();
        String savedCode = (String) redisUtils.get(key);
        
        if (savedCode == null || !savedCode.equals(request.getSmsCode())) {
            throw new BusinessException("验证码错误或已过期");
        }
        
        User existUser = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, request.getPhone()));
        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        }
        
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : "用户" + request.getPhone().substring(7));
        user.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + request.getPhone());
        user.setGender(0);
        user.setDepositStatus(0);
        user.setDepositAmount(BigDecimal.ZERO);
        user.setBalance(BigDecimal.ZERO);
        user.setCreditScore(initialCreditScore);
        user.setCreditLevel("NORMAL");
        user.setStatus(1);
        save(user);
        
        Coupon coupon = new Coupon();
        coupon.setUserId(user.getId());
        coupon.setCouponName("新用户专享5折券");
        coupon.setCouponType("DISCOUNT");
        coupon.setDiscountValue(new BigDecimal("0.50"));
        coupon.setMinAmount(BigDecimal.ONE);
        coupon.setIssueTime(LocalDateTime.now());
        coupon.setExpireTime(LocalDateTime.now().plusMonths(1));
        coupon.setStatus("UNUSED");
        coupon.setSource("REGISTER");
        couponMapper.insert(coupon);
        
        redisUtils.delete(key);
        
        String token = jwtUtils.generateToken(user.getId(), user.getPhone());
        return new LoginResponse(token, user.getId(), user.getPhone(), user.getNickname(), user.getAvatar());
    }

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, request.getPhone()));
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);
        
        String token = jwtUtils.generateToken(user.getId(), user.getPhone());
        return new LoginResponse(token, user.getId(), user.getPhone(), user.getNickname(), user.getAvatar());
    }

    /**
     * 第三方登录
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse thirdPartyLogin(String platform, String openid, String nickname, String avatar) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if ("WECHAT".equals(platform)) {
            wrapper.eq(User::getWechatOpenid, openid);
        } else if ("ALIPAY".equals(platform)) {
            wrapper.eq(User::getAlipayOpenid, openid);
        }
        
        User user = getOne(wrapper);
        if (user == null) {
            user = new User();
            if ("WECHAT".equals(platform)) {
                user.setWechatOpenid(openid);
            } else if ("ALIPAY".equals(platform)) {
                user.setAlipayOpenid(openid);
            }
            user.setNickname(nickname != null ? nickname : "第三方用户");
            user.setAvatar(avatar);
            user.setGender(0);
            user.setDepositStatus(0);
            user.setDepositAmount(BigDecimal.ZERO);
            user.setBalance(BigDecimal.ZERO);
            user.setCreditScore(initialCreditScore);
            user.setCreditLevel("NORMAL");
            user.setStatus(1);
            save(user);
        }
        
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);
        
        String token = jwtUtils.generateToken(user.getId(), user.getPhone());
        return new LoginResponse(token, user.getId(), user.getPhone(), user.getNickname(), user.getAvatar());
    }

    /**
     * 获取当前用户信息
     */
    public User getCurrentUser() {
        Long userId = UserContext.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        user.setPassword(null);
        return user;
    }

    /**
     * 实名认证
     */
    @Transactional(rollbackFor = Exception.class)
    public User realNameVerify(RealNameVerifyRequest request) {
        Long userId = UserContext.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setRealName(request.getRealName());
        user.setIdCard(request.getIdCard());
        user.setIdCardVerified(1);
        user.setFaceVerified(1);
        user.setRealNameVerified(1);
        updateById(user);
        
        if (user.getCreditScore() < initialCreditScore + 2) {
            changeCreditScore(userId, 2, "首次完成实名认证", "REGISTER", null);
        }
        
        return user;
    }

    /**
     * 缴纳押金
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> payDeposit(String paymentMethod) {
        Long userId = UserContext.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        if (user.getDepositStatus() != 0) {
            throw new BusinessException("您已缴纳押金或已享受免押金服务");
        }
        
        if (user.getBalance().compareTo(depositAmount) >= 0) {
            user.setBalance(user.getBalance().subtract(depositAmount));
            user.setDepositStatus(1);
            user.setDepositAmount(depositAmount);
            updateById(user);
            
            DepositRecord record = new DepositRecord();
            record.setUserId(userId);
            record.setRecordType("PAY");
            record.setAmount(depositAmount);
            record.setPaymentMethod("BALANCE");
            record.setStatus("SUCCESS");
            record.setRemark("余额支付押金");
            depositRecordMapper.insert(record);
        } else {
            DepositRecord record = new DepositRecord();
            record.setUserId(userId);
            record.setRecordType("PAY");
            record.setAmount(depositAmount);
            record.setPaymentMethod(paymentMethod);
            record.setStatus("SUCCESS");
            record.setRemark("第三方支付押金");
            depositRecordMapper.insert(record);
            
            user.setDepositStatus(1);
            user.setDepositAmount(depositAmount);
            updateById(user);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("depositStatus", user.getDepositStatus());
        result.put("depositAmount", user.getDepositAmount());
        result.put("balance", user.getBalance());
        return result;
    }

    /**
     * 信用免押金授权
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> creditExempt(String authSource, Integer creditScore) {
        Long userId = UserContext.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        if (creditScore < freeCreditScore) {
            throw new BusinessException("信用分不足" + freeCreditScore + "分，无法享受免押金服务");
        }
        
        user.setDepositStatus(2);
        user.setDepositAmount(BigDecimal.ZERO);
        updateById(user);
        
        DepositRecord record = new DepositRecord();
        record.setUserId(userId);
        record.setRecordType("EXEMPT");
        record.setAmount(BigDecimal.ZERO);
        record.setCreditAuthSource(authSource);
        record.setCreditScore(creditScore);
        record.setStatus("SUCCESS");
        record.setRemark("芝麻信用免押金授权成功");
        depositRecordMapper.insert(record);
        
        Map<String, Object> result = new HashMap<>();
        result.put("depositStatus", 2);
        result.put("creditScore", creditScore);
        result.put("authSource", authSource);
        return result;
    }

    /**
     * 退还押金
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> refundDeposit() {
        Long userId = UserContext.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        if (user.getDepositStatus() != 1) {
            throw new BusinessException("您没有可退还的押金");
        }
        
        user.setBalance(user.getBalance().add(user.getDepositAmount()));
        user.setDepositStatus(0);
        user.setDepositAmount(BigDecimal.ZERO);
        updateById(user);
        
        DepositRecord record = new DepositRecord();
        record.setUserId(userId);
        record.setRecordType("REFUND");
        record.setAmount(user.getDepositAmount());
        record.setStatus("SUCCESS");
        record.setRemark("押金退还至余额");
        depositRecordMapper.insert(record);
        
        Map<String, Object> result = new HashMap<>();
        result.put("depositStatus", 0);
        result.put("refundAmount", depositAmount);
        result.put("balance", user.getBalance());
        return result;
    }

    /**
     * 账户充值
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> recharge(BigDecimal amount, String paymentMethod) {
        Long userId = UserContext.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        BigDecimal bonus = BigDecimal.ZERO;
        if (amount.compareTo(new BigDecimal("50")) >= 0) {
            bonus = new BigDecimal("5");
        }
        if (amount.compareTo(new BigDecimal("100")) >= 0) {
            bonus = new BigDecimal("15");
        }
        
        BigDecimal total = amount.add(bonus);
        user.setBalance(user.getBalance().add(total));
        updateById(user);
        
        Map<String, Object> result = new HashMap<>();
        result.put("rechargeAmount", amount);
        result.put("bonusAmount", bonus);
        result.put("totalAmount", total);
        result.put("balance", user.getBalance());
        return result;
    }

    /**
     * 修改信用分
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeCreditScore(Long userId, int scoreChange, String reason, String relatedType, Long relatedId) {
        User user = getById(userId);
        if (user == null) {
            return;
        }
        
        int beforeScore = user.getCreditScore();
        int afterScore = Math.max(0, Math.min(120, beforeScore + scoreChange));
        
        user.setCreditScore(afterScore);
        
        String creditLevel;
        if (afterScore >= 90) {
            creditLevel = "EXCELLENT";
        } else if (afterScore >= 80) {
            creditLevel = "NORMAL";
        } else if (afterScore >= 60) {
            creditLevel = "WARNING";
        } else {
            creditLevel = "RESTRICTED";
        }
        user.setCreditLevel(creditLevel);
        updateById(user);
        
        CreditRecord record = new CreditRecord();
        record.setUserId(userId);
        record.setChangeType(scoreChange >= 0 ? "REWARD" : "DEDUCT");
        record.setScoreChange(scoreChange);
        record.setReason(reason);
        record.setRelatedId(relatedId);
        record.setRelatedType(relatedType);
        record.setBeforeScore(beforeScore);
        record.setAfterScore(afterScore);
        creditRecordMapper.insert(record);
    }

    /**
     * 检查用户用车权限
     */
    public void checkRidePermission() {
        User user = getCurrentUser();
        
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        
        if (user.getRealNameVerified() == null || user.getRealNameVerified() == 0) {
            throw new BusinessException(ResultCode.REAL_NAME_NOT_VERIFIED);
        }
        
        if (user.getDepositStatus() == null || user.getDepositStatus() == 0) {
            throw new BusinessException(ResultCode.DEPOSIT_NOT_PAID);
        }
        
        if ("RESTRICTED".equals(user.getCreditLevel())) {
            throw new BusinessException("信用分过低，暂时无法用车");
        }
    }
}
