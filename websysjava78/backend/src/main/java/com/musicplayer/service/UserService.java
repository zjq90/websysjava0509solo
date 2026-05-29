package com.musicplayer.service;

import com.musicplayer.dto.LoginRequest;
import com.musicplayer.dto.RegisterRequest;
import com.musicplayer.entity.User;
import com.musicplayer.repository.UserRepository;
import com.musicplayer.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setRole(User.UserRole.FREE);

        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("账号已被禁用");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getId());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        if (userDetails.getNickname() != null) {
            user.setNickname(userDetails.getNickname());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getAvatar() != null) {
            user.setAvatar(userDetails.getAvatar());
        }
        return userRepository.save(user);
    }

    @Transactional
    public User upgradeToPremium(Long id) {
        User user = getUserById(id);
        user.setRole(User.UserRole.PREMIUM);
        return userRepository.save(user);
    }

    @Transactional
    public void resetDownloadCountIfNeeded(User user) {
        LocalDate today = LocalDate.now();
        LocalDate lastReset = user.getLastDownloadReset() != null ?
                user.getLastDownloadReset().toLocalDate() : null;

        if (lastReset == null || !lastReset.isEqual(today)) {
            user.setDownloadCountToday(0);
            user.setLastDownloadReset(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Transactional
    public void incrementDownloadCount(User user) {
        resetDownloadCountIfNeeded(user);
        user.setDownloadCountToday(user.getDownloadCountToday() + 1);
        userRepository.save(user);
    }

    public int getRemainingDownloads(User user) {
        resetDownloadCountIfNeeded(user);
        int limit = user.getRole() == User.UserRole.PREMIUM ? 100 : 5;
        return limit - user.getDownloadCountToday();
    }
}
