package com.club.management.service;

import com.club.management.dto.LoginDTO;
import com.club.management.dto.RegisterDTO;
import com.club.management.vo.LoginVO;

/**
 * 认证服务接口
 *
 * @author club-management
 * @since 2024-01-01
 */
public interface AuthService {

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     */
    LoginVO register(RegisterDTO registerDTO);

    /**
     * 发送验证码
     */
    void sendVerifyCode(String phone);

    /**
     * 退出登录
     */
    void logout();
}
