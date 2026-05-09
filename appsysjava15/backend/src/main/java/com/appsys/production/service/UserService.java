package com.appsys.production.service;

import com.appsys.production.config.EncryptionConfig;
import com.appsys.production.dto.UserDTO;
import com.appsys.production.entity.User;
import com.appsys.production.exception.BusinessException;
import com.appsys.production.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionConfig encryptionConfig;

    public List<User> list() {
        List<User> users = userRepository.findAll();
        users.forEach(this::decryptUserPhone);
        return users;
    }

    public User getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException("用户不存在"));
        decryptUserPhone(user);
        return user;
    }

    public User getByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        userOpt.ifPresent(this::decryptUserPhone);
        return userOpt.orElse(null);
    }

    @Transactional
    public User create(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRealName(dto.getRealName());
        user.setRole(dto.getRole());
        user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) {
            user.setPhoneEncrypted(encryptionConfig.encrypt(dto.getPhone()));
        }
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, UserDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setRealName(dto.getRealName());
        user.setRole(dto.getRole());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        if (dto.getPhone() != null) {
            user.setPhoneEncrypted(encryptionConfig.encrypt(dto.getPhone()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    private void decryptUserPhone(User user) {
        if (user.getPhoneEncrypted() != null) {
            try {
                user.setPhoneEncrypted(encryptionConfig.decrypt(user.getPhoneEncrypted()));
            } catch (Exception e) {
            }
        }
    }
}
