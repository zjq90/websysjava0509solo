package com.websys.service;

import com.websys.entity.Menu;
import com.websys.entity.Permission;
import com.websys.entity.Role;
import com.websys.repository.MenuRepository;
import com.websys.repository.PermissionRepository;
import com.websys.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 角色服务类
 * 提供角色管理的业务逻辑，包括增删改查等操作
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * 分页查询角色列表
     * @param name 角色名称（模糊查询）
     * @param code 角色编码（模糊查询）
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页角色列表
     */
    public Page<Role> findPage(String name, String code, Integer status, Pageable pageable) {
        return roleRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            
            if (code != null && !code.isEmpty()) {
                predicates.add(cb.like(root.get("code"), "%" + code + "%"));
            }
            
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    /**
     * 查询所有角色
     * @return 角色列表
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色对象
     */
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * 根据编码查询角色
     * @param code 角色编码
     * @return 角色对象
     */
    public Optional<Role> findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    /**
     * 保存角色（新增/更新）
     * @param role 角色对象
     * @return 保存后的角色对象
     */
    @Transactional
    public Role save(Role role) {
        if (role.getId() != null) {
            role.setUpdateTime(LocalDateTime.now());
        }
        return roleRepository.save(role);
    }

    /**
     * 删除角色
     * @param id 角色ID
     */
    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    /**
     * 检查角色编码是否存在
     * @param code 角色编码
     * @return 是否存在
     */
    public boolean existsByCode(String code) {
        return roleRepository.existsByCode(code);
    }

    /**
     * 为角色分配菜单
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    @Transactional
    public Role assignMenus(Long roleId, List<Long> menuIds) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RuntimeException("角色不存在");
        }
        Role role = roleOpt.get();
        List<Menu> menus = menuRepository.findAllById(menuIds);
        role.setMenus(menus);
        role.setUpdateTime(LocalDateTime.now());
        return roleRepository.save(role);
    }

    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    @Transactional
    public Role assignPermissions(Long roleId, List<Long> permissionIds) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RuntimeException("角色不存在");
        }
        Role role = roleOpt.get();
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.setPermissions(permissions);
        role.setUpdateTime(LocalDateTime.now());
        return roleRepository.save(role);
    }

    /**
     * 获取角色的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    public List<Long> getRoleMenuIds(Long roleId) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            return new ArrayList<>();
        }
        Role role = roleOpt.get();
        List<Long> menuIds = new ArrayList<>();
        if (role.getMenus() != null) {
            for (Menu menu : role.getMenus()) {
                menuIds.add(menu.getId());
            }
        }
        return menuIds;
    }

    /**
     * 获取角色的权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    public List<Long> getRolePermissionIds(Long roleId) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            return new ArrayList<>();
        }
        Role role = roleOpt.get();
        List<Long> permissionIds = new ArrayList<>();
        if (role.getPermissions() != null) {
            for (Permission permission : role.getPermissions()) {
                permissionIds.add(permission.getId());
            }
        }
        return permissionIds;
    }
}
