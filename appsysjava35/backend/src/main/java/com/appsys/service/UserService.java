package com.appsys.service;

import com.appsys.entity.User;
import com.appsys.repository.UserRepository;
import com.appsys.util.RSAUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RSAUtil rsaUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RSAUtil rsaUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rsaUtil = rsaUtil;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMemberLevelId(1L);
        user.setPoints(0);
        user.setGrowth(0);
        user.setStatus(1);
        user.setElderMode(false);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return null;
        }
        if (user.getRealName() != null) {
            existingUser.setRealName(user.getRealName());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getAvatar() != null) {
            existingUser.setAvatar(user.getAvatar());
        }
        return userRepository.save(existingUser);
    }

    public boolean toggleElderMode(Long userId, boolean elderMode) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setElderMode(elderMode);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public String getRSAPublicKey() {
        return rsaUtil.getPublicKeyBase64();
    }

    public String encryptData(String data) throws Exception {
        return rsaUtil.encryptByPublicKey(data);
    }

    public String decryptData(String encryptedData) throws Exception {
        return rsaUtil.decryptByPrivateKey(encryptedData);
    }
}
