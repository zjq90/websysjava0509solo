package com.hospital.service;

import com.hospital.entity.SysRole;
import com.hospital.entity.SysUser;
import com.hospital.repository.SysRoleRepository;
import com.hospital.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统用户服务类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class SysUserService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 分页查询用户列表
     */
    public Page<SysUser> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * 根据ID查询用户
     */
    public Optional<SysUser> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名查询用户
     */
    public Optional<SysUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 保存用户
     */
    public SysUser save(SysUser user) {
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            SysUser existingUser = userRepository.findById(user.getId()).orElse(null);
            if (existingUser != null && user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else if (existingUser != null) {
                user.setPassword(existingUser.getPassword());
            }
        }
        return userRepository.save(user);
    }

    /**
     * 删除用户
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 判断用户名是否存在
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 判断用户名是否存在（排除指定ID）
     */
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return userRepository.existsByUsernameAndIdNot(username, id);
    }

    /**
     * 分配用户角色
     */
    public SysUser assignRoles(Long userId, List<Long> roleIds) {
        SysUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Set<SysRole> roles = new HashSet<>();
        if (roleIds != null && !roleIds.isEmpty()) {
            roles = new HashSet<>(roleRepository.findAllById(roleIds));
        }
        
        user.setRoles(roles);
        return userRepository.save(user);
    }

    /**
     * 获取用户的角色ID列表
     */
    public List<Long> getUserRoleIds(Long userId) {
        SysUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return user.getRoles().stream()
                .map(SysRole::getId)
                .collect(Collectors.toList());
    }
}
