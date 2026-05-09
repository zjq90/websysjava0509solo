package com.agriculture.service;

import com.agriculture.entity.User;
import com.agriculture.repository.UserRepository;
import com.agriculture.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务类
 * 提供用户管理的业务逻辑
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionUtil encryptionUtil;

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，登录失败返回null
     */
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        String hashedPassword = encryptionUtil.hashPassword(password);
        Optional<User> userOpt = userRepository.findByUsernameAndPassword(username, hashedPassword);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if ("ACTIVE".equals(user.getStatus())) {
                user.setLastLoginTime(LocalDateTime.now());
                userRepository.save(user);
                return user;
            }
        }
        
        return null;
    }

    /**
     * 注册新用户
     * 
     * @param user 用户信息
     * @return 创建后的用户
     */
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        user.setPassword(encryptionUtil.hashPassword(user.getPassword()));
        if (user.getPhone() != null) {
            user.setPhone(encryptionUtil.encrypt(user.getPhone()));
        }
        
        return userRepository.save(user);
    }

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建后的用户
     */
    public User create(User user) {
        return register(user);
    }

    /**
     * 更新用户信息
     * 
     * @param user 用户信息
     * @return 更新后的用户
     */
    public User update(User user) {
        Optional<User> existingOpt = userRepository.findById(user.getId());
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User existing = existingOpt.get();
        
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(encryptionUtil.hashPassword(user.getPassword()));
        } else {
            user.setPassword(existing.getPassword());
        }
        
        if (user.getPhone() != null) {
            user.setPhone(encryptionUtil.encrypt(user.getPhone()));
        }
        
        return userRepository.save(user);
    }

    /**
     * 根据ID删除用户
     * 
     * @param id 用户ID
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 根据ID查找用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
