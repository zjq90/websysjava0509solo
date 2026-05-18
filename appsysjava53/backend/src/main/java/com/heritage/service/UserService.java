package com.heritage.service;

import com.heritage.entity.User;
import com.heritage.repository.UserRepository;
import com.heritage.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RSAUtil rsaUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String USER_CACHE_KEY = "user:";

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                log.info("用户登录成功: {}", username);
                return user;
            }
        }
        return null;
    }

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("手机号已注册");
        }
        user.setPassword(rsaUtil.encrypt(user.getPassword()));
        if (user.getPhone() != null) {
            user.setPhone(rsaUtil.encrypt(user.getPhone()));
        }
        return userRepository.save(user);
    }

    /**
     * 根据ID查询用户（带Redis缓存）
     */
    @Cacheable(value = "user", key = "#id")
    public User findById(Long id) {
        log.info("从数据库查询用户: {}", id);
        return userRepository.findById(id).orElse(null);
    }

    /**
     * 更新用户信息
     */
    @CacheEvict(value = "user", key = "#user.id")
    @Transactional(rollbackFor = Exception.class)
    public User update(User user) {
        return userRepository.save(user);
    }

    /**
     * 获取所有专家列表
     */
    @Cacheable(value = "experts")
    public List<User> findAllExperts() {
        return userRepository.findAllExperts();
    }

    /**
     * 切换长辈模式
     */
    @CacheEvict(value = "user", key = "#userId")
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleElderMode(Long userId, boolean enable) {
        User user = findById(userId);
        if (user != null) {
            user.setIsElderMode(enable ? 1 : 0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * 更新账户余额
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBalance(Long userId, java.math.BigDecimal amount) {
        User user = findById(userId);
        if (user != null) {
            user.setBalance(user.getBalance().add(amount));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * 冻结/解冻资金
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean freezeBalance(Long userId, java.math.BigDecimal amount, boolean freeze) {
        User user = findById(userId);
        if (user != null) {
            if (freeze) {
                if (user.getBalance().compareTo(amount) < 0) {
                    return false;
                }
                user.setBalance(user.getBalance().subtract(amount));
                user.setFrozenBalance(user.getFrozenBalance().add(amount));
            } else {
                user.setFrozenBalance(user.getFrozenBalance().subtract(amount));
                user.setBalance(user.getBalance().add(amount));
            }
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
