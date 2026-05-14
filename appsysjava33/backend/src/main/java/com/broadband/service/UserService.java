package com.broadband.service;

import com.broadband.entity.User;
import com.broadband.repository.UserRepository;
import com.broadband.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户服务
 * 处理用户信息管理、实名认证等功能
 * 
 * @author broadband
 * @version 1.0.0
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public User updateUser(User user) {
        User existUser = getUserById(user.getId());
        if (user.getName() != null) existUser.setName(user.getName());
        if (user.getEmail() != null) existUser.setEmail(user.getEmail());
        if (user.getAvatar() != null) existUser.setAvatar(user.getAvatar());
        if (user.getInstallAddress() != null) existUser.setInstallAddress(user.getInstallAddress());
        if (user.getDetailAddress() != null) existUser.setDetailAddress(user.getDetailAddress());
        if (user.getLongitude() != null) existUser.setLongitude(user.getLongitude());
        if (user.getLatitude() != null) existUser.setLatitude(user.getLatitude());
        existUser.setUpdateTime(LocalDateTime.now());
        return userRepository.save(existUser);
    }

    public User submitRealName(Long userId, String name, String idCard, MultipartFile frontImg, MultipartFile backImg) throws Exception {
        User user = getUserById(userId);
        String uploadPath = "./uploads/";
        new File(uploadPath).mkdirs();

        if (frontImg != null && !frontImg.isEmpty()) {
            String frontFileName = UUID.randomUUID() + "_front." + getFileExtension(frontImg.getOriginalFilename());
            frontImg.transferTo(new File(uploadPath + frontFileName));
            user.setIdCardFront(uploadPath + frontFileName);
        }

        if (backImg != null && !backImg.isEmpty()) {
            String backFileName = UUID.randomUUID() + "_back." + getFileExtension(backImg.getOriginalFilename());
            backImg.transferTo(new File(uploadPath + backFileName));
            user.setIdCardBack(uploadPath + backFileName);
        }

        user.setName(name);
        user.setIdCard(RSAUtil.encrypt(idCard));
        user.setRealNameStatus(1);
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User verifyLiveness(Long userId) {
        User user = getUserById(userId);
        user.setLivenessStatus(1);
        user.setRealNameStatus(2);
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User toggleElderMode(Long userId, Integer elderMode) {
        User user = getUserById(userId);
        user.setElderMode(elderMode);
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User toggleAutoRenewal(Long userId, Integer autoRenewal) {
        User user = getUserById(userId);
        user.setAutoRenewal(autoRenewal);
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "jpg";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
