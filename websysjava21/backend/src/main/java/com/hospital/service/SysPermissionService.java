package com.hospital.service;

import com.hospital.entity.SysPermission;
import com.hospital.repository.SysPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统权限服务类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class SysPermissionService {

    @Autowired
    private SysPermissionRepository permissionRepository;

    /**
     * 查询所有权限
     */
    public List<SysPermission> findAll() {
        return permissionRepository.findAll(Sort.by("sortOrder"));
    }

    /**
     * 根据ID查询权限
     */
    public Optional<SysPermission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    /**
     * 根据权限编码查询权限
     */
    public Optional<SysPermission> findByPermissionCode(String permissionCode) {
        return permissionRepository.findByPermissionCode(permissionCode);
    }

    /**
     * 保存权限
     */
    public SysPermission save(SysPermission permission) {
        return permissionRepository.save(permission);
    }

    /**
     * 删除权限
     */
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    /**
     * 判断权限编码是否存在
     */
    public boolean existsByPermissionCode(String permissionCode) {
        return permissionRepository.existsByPermissionCode(permissionCode);
    }

    /**
     * 判断权限编码是否存在（排除指定ID）
     */
    public boolean existsByPermissionCodeAndIdNot(String permissionCode, Long id) {
        SysPermission existing = permissionRepository.findByPermissionCode(permissionCode).orElse(null);
        return existing != null && !existing.getId().equals(id);
    }

    /**
     * 构建权限树
     */
    public List<Map<String, Object>> buildPermissionTree() {
        List<SysPermission> allPermissions = findAll();
        return buildTree(allPermissions, 0L);
    }

    /**
     * 递归构建树结构
     */
    private List<Map<String, Object>> buildTree(List<SysPermission> permissions, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        
        for (SysPermission permission : permissions) {
            if ((parentId == 0L && (permission.getParentId() == null || permission.getParentId() == 0L)) 
                || (permission.getParentId() != null && permission.getParentId().equals(parentId))) {
                
                Map<String, Object> node = new HashMap<>();
                node.put("id", permission.getId());
                node.put("permissionCode", permission.getPermissionCode());
                node.put("permissionName", permission.getPermissionName());
                node.put("resourceType", permission.getResourceType());
                node.put("label", permission.getPermissionName());
                node.put("children", buildTree(permissions, permission.getId()));
                
                tree.add(node);
            }
        }
        
        return tree;
    }
}
