package com.hospital.service;

import com.hospital.entity.SysPermission;
import com.hospital.entity.SysRole;
import com.hospital.repository.SysPermissionRepository;
import com.hospital.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统角色服务类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class SysRoleService {

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysPermissionRepository permissionRepository;

    /**
     * 分页查询角色列表
     */
    public Page<SysRole> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    /**
     * 查询所有角色
     */
    public List<SysRole> findAll() {
        return roleRepository.findAll();
    }

    /**
     * 根据ID查询角色
     */
    public Optional<SysRole> findById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * 根据角色编码查询角色
     */
    public Optional<SysRole> findByRoleCode(String roleCode) {
        return roleRepository.findByRoleCode(roleCode);
    }

    /**
     * 保存角色
     */
    public SysRole save(SysRole role) {
        return roleRepository.save(role);
    }

    /**
     * 删除角色
     */
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    /**
     * 判断角色编码是否存在
     */
    public boolean existsByRoleCode(String roleCode) {
        return roleRepository.existsByRoleCode(roleCode);
    }

    /**
     * 判断角色编码是否存在（排除指定ID）
     */
    public boolean existsByRoleCodeAndIdNot(String roleCode, Long id) {
        return roleRepository.existsByRoleCodeAndIdNot(roleCode, id);
    }

    /**
     * 分配角色权限
     */
    public SysRole assignPermissions(Long roleId, List<Long> permissionIds) {
        SysRole role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        Set<SysPermission> permissions = new HashSet<>();
        if (permissionIds != null && !permissionIds.isEmpty()) {
            permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        }
        
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    /**
     * 获取角色的权限ID列表
     */
    public List<Long> getRolePermissionIds(Long roleId) {
        SysRole role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        return role.getPermissions().stream()
                .map(SysPermission::getId)
                .collect(Collectors.toList());
    }
}
