package com.websys.service;

import com.websys.entity.Menu;
import com.websys.entity.Permission;
import com.websys.entity.Role;
import com.websys.entity.User;
import com.websys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户服务类
 * 提供用户管理的业务逻辑，包括增删改查等操作
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 分页查询用户列表
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页用户列表
     */
    public Page<User> findPage(String username, String realName, Integer status, Pageable pageable) {
        return userRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (username != null && !username.isEmpty()) {
                predicates.add(cb.like(root.get("username"), "%" + username + "%"));
            }
            
            if (realName != null && !realName.isEmpty()) {
                predicates.add(cb.like(root.get("realName"), "%" + realName + "%"));
            }
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 保存用户（新增/更新）
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    @Transactional
    public User save(User user) {
        if (user.getId() != null) {
            user.setUpdateTime(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    /**
     * 删除用户
     * @param id 用户ID
     */
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @param excludeId 排除的用户ID（更新时使用）
     * @return 是否存在
     */
    public boolean existsByUsername(String username, Long excludeId) {
        if (excludeId != null) {
            return userRepository.existsByUsernameAndIdNot(username, excludeId);
        }
        return userRepository.existsByUsername(username);
    }

    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 新状态
     * @return 更新后的用户
     */
    @Transactional
    public User updateStatus(Long id, Integer status) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(status);
            user.setUpdateTime(LocalDateTime.now());
            return userRepository.save(user);
        }
        throw new RuntimeException("用户不存在");
    }

    public List<Menu> getUserMenus(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return new ArrayList<>();
        }
        User user = userOpt.get();
        Set<Menu> menuSet = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role.getMenus() != null) {
                    menuSet.addAll(role.getMenus());
                }
            }
        }
        return new ArrayList<>(menuSet);
    }

    public List<Permission> getUserPermissions(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return new ArrayList<>();
        }
        User user = userOpt.get();
        Set<Permission> permissionSet = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role.getPermissions() != null) {
                    permissionSet.addAll(role.getPermissions());
                }
            }
        }
        return new ArrayList<>(permissionSet);
    }

    public boolean hasPermission(Long userId, String permissionCode) {
        List<Permission> permissions = getUserPermissions(userId);
        return permissions.stream()
                .anyMatch(p -> p.getCode().equals(permissionCode));
    }
}
