package com.club.service;

import cn.hutool.crypto.digest.BCrypt;
import com.club.dto.LoginDTO;
import com.club.dto.LoginResultDTO;
import com.club.dto.RegisterDTO;
import com.club.entity.User;
import com.club.exception.BusinessException;
import com.club.repository.UserRepository;
import com.club.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    public LoginResultDTO login(LoginDTO loginDTO) {
        log.info("用户登录 - 用户名: {}", loginDTO.getUsername());

        User user = userRepository.findByUsernameAndDeletedFalse(loginDTO.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            log.warn("密码错误 - 用户名: {}", loginDTO.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() != 1) {
            log.warn("账号已禁用 - 用户名: {}", loginDTO.getUsername());
            throw new BusinessException("账号已被禁用，请联系管理员");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        log.info("用户登录成功 - userId: {}, username: {}", user.getId(), user.getUsername());

        return new LoginResultDTO(
                token,
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getAvatar(),
                user.getRole(),
                user.getSchoolId()
        );
    }

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterDTO registerDTO) {
        log.info("用户注册 - 用户名: {}, 学校ID: {}", registerDTO.getUsername(), registerDTO.getSchoolId());

        if (userRepository.existsByUsernameAndDeletedFalse(registerDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt()));
        user.setRealName(registerDTO.getRealName());
        user.setStudentNo(registerDTO.getStudentNo());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setSchoolId(registerDTO.getSchoolId());
        user.setCollege(registerDTO.getCollege());
        user.setMajor(registerDTO.getMajor());
        user.setGrade(registerDTO.getGrade());
        user.setRole("USER");
        user.setStatus(1);
        user.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + registerDTO.getUsername());

        User saved = userRepository.save(user);

        log.info("用户注册成功 - userId: {}, username: {}", user.getId(), user.getUsername());

        return saved;
    }

    /**
     * 根据ID获取用户信息
     */
    public User getUserById(Long userId) {
        return userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }
}
