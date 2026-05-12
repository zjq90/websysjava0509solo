package com.hospital.config;

import com.hospital.entity.SysPermission;
import com.hospital.entity.SysRole;
import com.hospital.entity.SysUser;
import com.hospital.repository.SysPermissionRepository;
import com.hospital.repository.SysRoleRepository;
import com.hospital.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 用户角色数据初始化器
 * 系统启动时初始化用户和角色关联数据
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysPermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 为ADMIN角色分配所有权限
        Optional<SysRole> adminRoleOpt = roleRepository.findByRoleCode("ADMIN");
        if (adminRoleOpt.isPresent()) {
            SysRole adminRole = adminRoleOpt.get();
            if (adminRole.getPermissions() == null || adminRole.getPermissions().isEmpty()) {
                List<SysPermission> allPermissions = permissionRepository.findAll();
                adminRole.setPermissions(new HashSet<>(allPermissions));
                roleRepository.save(adminRole);
                System.out.println("管理员角色权限分配完成！共分配 " + allPermissions.size() + " 个权限");
            }
        }

        // 初始化管理员用户
        if (!userRepository.existsByUsername("admin")) {
            SysUser admin = new SysUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRealName("系统管理员");
            admin.setPhone("13800138000");
            admin.setEmail("admin@hospital.com");
            admin.setStatus(1);
            
            // 设置角色
            Set<SysRole> roles = new HashSet<>();
            adminRoleOpt.ifPresent(roles::add);
            admin.setRoles(roles);
            
            userRepository.save(admin);
            System.out.println("管理员账号初始化成功！用户名: admin, 密码: 123456");
        }

        // 初始化测试用户 - 医院管理者
        if (!userRepository.existsByUsername("manager")) {
            SysUser manager = new SysUser();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("123456"));
            manager.setRealName("张院长");
            manager.setPhone("13800138001");
            manager.setEmail("manager@hospital.com");
            manager.setStatus(1);
            
            Optional<SysRole> managerRole = roleRepository.findByRoleCode("HOSPITAL_MANAGER");
            Set<SysRole> roles = new HashSet<>();
            managerRole.ifPresent(roles::add);
            manager.setRoles(roles);
            
            userRepository.save(manager);
            System.out.println("医院管理者账号初始化成功！用户名: manager, 密码: 123456");
        }

        // 初始化测试用户 - 医生
        if (!userRepository.existsByUsername("doctor")) {
            SysUser doctor = new SysUser();
            doctor.setUsername("doctor");
            doctor.setPassword(passwordEncoder.encode("123456"));
            doctor.setRealName("李医生");
            doctor.setPhone("13800138002");
            doctor.setEmail("doctor@hospital.com");
            doctor.setStatus(1);
            
            Optional<SysRole> doctorRole = roleRepository.findByRoleCode("DOCTOR");
            Set<SysRole> roles = new HashSet<>();
            doctorRole.ifPresent(roles::add);
            doctor.setRoles(roles);
            
            userRepository.save(doctor);
            System.out.println("医生账号初始化成功！用户名: doctor, 密码: 123456");
        }

        // 初始化测试用户 - 护士
        if (!userRepository.existsByUsername("nurse")) {
            SysUser nurse = new SysUser();
            nurse.setUsername("nurse");
            nurse.setPassword(passwordEncoder.encode("123456"));
            nurse.setRealName("王护士");
            nurse.setPhone("13800138003");
            nurse.setEmail("nurse@hospital.com");
            nurse.setStatus(1);
            
            Optional<SysRole> nurseRole = roleRepository.findByRoleCode("NURSE");
            Set<SysRole> roles = new HashSet<>();
            nurseRole.ifPresent(roles::add);
            nurse.setRoles(roles);
            
            userRepository.save(nurse);
            System.out.println("护士账号初始化成功！用户名: nurse, 密码: 123456");
        }
    }
}
