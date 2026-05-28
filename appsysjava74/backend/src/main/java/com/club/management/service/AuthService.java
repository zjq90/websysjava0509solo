package com.club.management.service;

import com.club.management.dto.LoginDTO;
import com.club.management.dto.LoginResponse;
import com.club.management.entity.User;
import com.club.management.repository.UserRepository;
import com.club.management.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 用户登录
     * 
     * @param loginDTO 登录信息
     * @return 登录响应
     */
    public LoginResponse login(LoginDTO loginDTO) {
        log.info("用户登录请求: username={}", loginDTO.getUsername());

        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> {
                    log.warn("登录失败，用户不存在: username={}", loginDTO.getUsername());
                    return new BadCredentialsException("用户名或密码错误");
                });

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.warn("登录失败，密码错误: username={}", loginDTO.getUsername());
            throw new BadCredentialsException("用户名或密码错误");
        }

        if (!user.getEnabled()) {
            log.warn("登录失败，用户已被禁用: username={}", loginDTO.getUsername());
            throw new BadCredentialsException("用户已被禁用");
        }

        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        log.info("用户登录成功: userId={}, username={}, role={}", 
                user.getId(), user.getUsername(), user.getRole());

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getRole(),
                user.getAvatarUrl()
        );
    }

    /**
     * 根据ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }
}
