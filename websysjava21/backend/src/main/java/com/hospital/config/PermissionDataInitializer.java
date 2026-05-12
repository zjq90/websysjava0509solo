package com.hospital.config;

import com.hospital.entity.SysPermission;
import com.hospital.repository.SysPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 权限数据初始化器
 * 系统启动时初始化默认权限数据
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Component
@Order(1)
public class PermissionDataInitializer implements CommandLineRunner {

    @Autowired
    private SysPermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (permissionRepository.count() == 0) {
            initPermissions();
            System.out.println("权限数据初始化完成！");
        }
    }

    private void initPermissions() {
        // ========== 系统管理模块 ==========
        
        // 用户管理权限
        createPermission(1L, "system:user", "用户管理", "menu", 0L, 1);
        createPermission(2L, "system:user:list", "用户列表", "button", 1L, 1);
        createPermission(3L, "system:user:add", "新增用户", "button", 1L, 2);
        createPermission(4L, "system:user:edit", "编辑用户", "button", 1L, 3);
        createPermission(5L, "system:user:delete", "删除用户", "button", 1L, 4);
        createPermission(6L, "system:user:role", "分配角色", "button", 1L, 5);

        // 角色管理权限
        createPermission(7L, "system:role", "角色管理", "menu", 0L, 2);
        createPermission(8L, "system:role:list", "角色列表", "button", 7L, 1);
        createPermission(9L, "system:role:add", "新增角色", "button", 7L, 2);
        createPermission(10L, "system:role:edit", "编辑角色", "button", 7L, 3);
        createPermission(11L, "system:role:delete", "删除角色", "button", 7L, 4);
        createPermission(12L, "system:role:permission", "分配权限", "button", 7L, 5);

        // 权限管理权限
        createPermission(13L, "system:permission", "权限管理", "menu", 0L, 3);
        createPermission(14L, "system:permission:list", "权限列表", "button", 13L, 1);
        createPermission(15L, "system:permission:add", "新增权限", "button", 13L, 2);
        createPermission(16L, "system:permission:edit", "编辑权限", "button", 13L, 3);
        createPermission(17L, "system:permission:delete", "删除权限", "button", 13L, 4);

        // 字典管理权限
        createPermission(18L, "system:dict", "字典管理", "menu", 0L, 4);
        createPermission(19L, "system:dict:list", "字典列表", "button", 18L, 1);
        createPermission(20L, "system:dict:add", "新增字典", "button", 18L, 2);
        createPermission(21L, "system:dict:edit", "编辑字典", "button", 18L, 3);
        createPermission(22L, "system:dict:delete", "删除字典", "button", 18L, 4);

        // 日志管理权限
        createPermission(23L, "system:log", "日志管理", "menu", 0L, 5);
        createPermission(24L, "system:log:list", "日志列表", "button", 23L, 1);
        createPermission(25L, "system:log:delete", "删除日志", "button", 23L, 2);

        // 系统配置权限
        createPermission(26L, "system:config", "系统配置", "menu", 0L, 6);
        createPermission(27L, "system:config:list", "配置列表", "button", 26L, 1);
        createPermission(28L, "system:config:add", "新增配置", "button", 26L, 2);
        createPermission(29L, "system:config:edit", "编辑配置", "button", 26L, 3);
        createPermission(30L, "system:config:delete", "删除配置", "button", 26L, 4);

        // ========== 医生工作台模块 ==========
        createPermission(31L, "doctor", "医生工作台", "menu", 0L, 7);
        createPermission(32L, "doctor:patient", "患者管理", "button", 31L, 1);

        // ========== 护士工作站模块 ==========
        createPermission(33L, "nurse", "护士工作站", "menu", 0L, 8);
        createPermission(34L, "nurse:ward", "病房管理", "button", 33L, 1);

        // ========== 收费管理模块 ==========
        createPermission(35L, "finance", "收费管理", "menu", 0L, 9);
        createPermission(36L, "finance:charge", "收费登记", "button", 35L, 1);
    }

    private void createPermission(Long id, String code, String name, String type, Long parentId, Integer sortOrder) {
        SysPermission permission = new SysPermission();
        permission.setId(id);
        permission.setPermissionCode(code);
        permission.setPermissionName(name);
        permission.setResourceType(type);
        permission.setParentId(parentId);
        permission.setSortOrder(sortOrder);
        permission.setStatus(1);
        permission.setDescription(name + "权限");
        permissionRepository.save(permission);
    }
}
