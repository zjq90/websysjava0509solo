package com.example.websys.service;

import com.example.websys.entity.AdminUser;
import com.example.websys.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 管理员用户业务逻辑服务类
 * 提供管理员用户的增删改查及登录验证功能
 */
@Service
@Transactional
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    /**
     * 用户登录验证
     */
    public Optional<AdminUser> login(String username, String password) {
        return adminUserRepository.findByUsernameAndPassword(username, password);
    }

    /**
     * 根据用户名查找用户
     */
    public Optional<AdminUser> findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    /**
     * 根据ID查找用户
     */
    public Optional<AdminUser> findById(Long id) {
        return adminUserRepository.findById(id);
    }

    /**
     * 保存用户
     */
    public AdminUser save(AdminUser user) {
        return adminUserRepository.save(user);
    }

    /**
     * 更新用户
     */
    public AdminUser update(AdminUser user) {
        Optional<AdminUser> existingOpt = adminUserRepository.findById(user.getId());
        if (existingOpt.isPresent()) {
            AdminUser existing = existingOpt.get();
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existing.setPassword(user.getPassword());
            }
            existing.setRealName(user.getRealName());
            existing.setEmail(user.getEmail());
            existing.setPhone(user.getPhone());
            existing.setRole(user.getRole());
            existing.setStatus(user.getStatus());
            return adminUserRepository.save(existing);
        }
        return null;
    }

    /**
     * 删除用户
     */
    public void deleteById(Long id) {
        adminUserRepository.deleteById(id);
    }

    /**
     * 获取所有用户
     */
    public List<AdminUser> getAll() {
        return adminUserRepository.findAll();
    }
}
