package com.appsys.security.service;

import com.appsys.common.exception.BusinessException;
import com.appsys.common.util.AESUtil;
import com.appsys.security.dto.LoginRequest;
import com.appsys.security.dto.LoginResponse;
import com.appsys.security.util.JwtUtil;
import com.appsys.system.entity.SysPermission;
import com.appsys.system.entity.SysRole;
import com.appsys.system.entity.SysUser;
import com.appsys.system.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证服务类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Service
public class AuthService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AESUtil aesUtil;

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        // 查询用户
        SysUser user = userRepository.findByUsernameAndDeletedFalse(request.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 获取角色列表
        List<String> roleCodes = user.getRoles().stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());

        // 获取权限列表
        List<String> permissionCodes = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(SysPermission::getPermissionCode)
                .distinct()
                .collect(Collectors.toList());

        // 生成Token（使用第一个角色）
        String mainRole = roleCodes.isEmpty() ? "ROLE_USER" : roleCodes.get(0);
        String token = jwtUtil.generateToken(user.getUsername(), mainRole);

        // 解密手机号并脱敏
        String phone = null;
        if (user.getPhone() != null) {
            phone = aesUtil.decryptPhone(user.getPhone(), true);
        }

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .phone(phone)
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .roles(roleCodes)
                .permissions(permissionCodes)
                .build();
    }

    /**
     * 获取当前用户信息
     */
    public LoginResponse getCurrentUserInfo(String username) {
        SysUser user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        // 获取角色列表
        List<String> roleCodes = user.getRoles().stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());

        // 获取权限列表
        List<String> permissionCodes = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(SysPermission::getPermissionCode)
                .distinct()
                .collect(Collectors.toList());

        // 解密手机号并脱敏
        String phone = null;
        if (user.getPhone() != null) {
            phone = aesUtil.decryptPhone(user.getPhone(), true);
        }

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .phone(phone)
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .roles(roleCodes)
                .permissions(permissionCodes)
                .build();
    }
}
