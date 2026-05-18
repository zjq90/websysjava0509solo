package com.heritage.service;

import com.heritage.entity.User;
import com.heritage.enums.AuditStatus;
import com.heritage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findSuspiciousUsers() {
        return userRepository.findByIsSuspiciousTrue();
    }

    public List<User> findFrozenFundsUsers() {
        return userRepository.findByFundsFrozenTrue();
    }

    @Transactional
    public User markSuspicious(Long userId, String reason) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setIsSuspicious(true);
        user.setSuspiciousReason(reason);
        user.setCreditScore(Math.max(0, user.getCreditScore() - 10));
        return userRepository.save(user);
    }

    @Transactional
    public User ignoreSuspicious(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setIsSuspicious(false);
        user.setSuspiciousReason(null);
        return userRepository.save(user);
    }

    @Transactional
    public User freezeFunds(Long userId, String reason) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setFundsFrozen(true);
        user.setFreezeReason(reason);
        user.setFreezeTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User unfreezeFunds(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setFundsFrozen(false);
        user.setFreezeReason(null);
        user.setFreezeTime(null);
        return userRepository.save(user);
    }

    @Transactional
    public User auditRealName(Long userId, AuditStatus status, String remark) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setRealNameAuditStatus(status);
        if (status == AuditStatus.APPROVED) {
            user.setIdVerified(true);
            user.setFaceVerified(true);
        }
        return userRepository.save(user);
    }

    @Transactional
    public User auditExpert(Long userId, AuditStatus status, String remark) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setExpertAuditStatus(status);
        user.setExpertAuditRemark(remark);
        user.setExpertAuditTime(LocalDateTime.now());
        if (status == AuditStatus.APPROVED) {
            user.setIsExpert(true);
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateCreditScore(Long userId, Integer score) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setCreditScore(Math.max(0, Math.min(100, score)));
        return userRepository.save(user);
    }

    public List<User> findExpertAuditPending() {
        return userRepository.findByExpertAuditStatus(AuditStatus.PENDING);
    }

    public List<User> findRealNameAuditPending() {
        return userRepository.findByRealNameAuditStatus(AuditStatus.PENDING);
    }

    public List<User> findAllExperts() {
        return userRepository.findByIsExpertTrue();
    }
}
