package com.medical.registration.service;

import com.medical.registration.common.BusinessException;
import com.medical.registration.common.ErrorCode;
import com.medical.registration.dto.LoginDTO;
import com.medical.registration.dto.LoginVO;
import com.medical.registration.dto.RegisterDTO;
import com.medical.registration.entity.Role;
import com.medical.registration.entity.User;
import com.medical.registration.entity.UserRole;
import com.medical.registration.repository.RoleRepository;
import com.medical.registration.repository.UserRepository;
import com.medical.registration.repository.UserRoleRepository;
import com.medical.registration.util.JwtUtil;
import com.medical.registration.util.Sm4Util;
import com.medical.registration.aop.LogOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @LogOperation(operationType = "登录", operationDesc = "用户登录", module = "认证模块")
    public LoginVO login(LoginDTO dto) {
        Optional<User> userOpt = userRepository.findByUsername(dto.getUsername());
        if (!userOpt.isPresent()) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST);
        }
        
        User user = userOpt.get();
        if (user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }
        
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR);
        }
        
        String role = "USER";
        
        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setPhone(user.getPhone() != null ? Sm4Util.maskPhone(Sm4Util.decrypt(user.getPhone())) : null);
        vo.setRole(role);
        vo.setToken(jwtUtil.generateToken(user.getId(), user.getUsername(), role));
        
        return vo;
    }
    
    @Transactional
    @LogOperation(operationType = "注册", operationDesc = "用户注册", module = "认证模块")
    public LoginVO register(RegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
        }
        
        if (dto.getPhone() != null && userRepository.existsByPhone(Sm4Util.encrypt(dto.getPhone()))) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXIST);
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone() != null ? Sm4Util.encrypt(dto.getPhone()) : null);
        user.setIdCard(dto.getIdCard() != null ? Sm4Util.encrypt(dto.getIdCard()) : null);
        user.setGender(dto.getGender());
        user.setAge(dto.getAge());
        user.setAddress(dto.getAddress());
        user.setStatus(1);
        user = userRepository.save(user);
        
        Optional<Role> roleOpt = roleRepository.findByRoleCode("USER");
        if (roleOpt.isPresent()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleOpt.get().getId());
            userRoleRepository.save(userRole);
        }
        
        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setPhone(dto.getPhone() != null ? Sm4Util.maskPhone(dto.getPhone()) : null);
        vo.setRole("USER");
        vo.setToken(jwtUtil.generateToken(user.getId(), user.getUsername(), "USER"));
        
        return vo;
    }
    
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST));
    }
    
    public void updateUserInfo(Long userId, RegisterDTO dto) {
        User user = getUserById(userId);
        if (dto.getRealName() != null) {
            user.setRealName(dto.getRealName());
        }
        if (dto.getPhone() != null) {
            user.setPhone(Sm4Util.encrypt(dto.getPhone()));
        }
        if (dto.getIdCard() != null) {
            user.setIdCard(Sm4Util.encrypt(dto.getIdCard()));
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getAge() != null) {
            user.setAge(dto.getAge());
        }
        if (dto.getAddress() != null) {
            user.setAddress(dto.getAddress());
        }
        userRepository.save(user);
    }
}
