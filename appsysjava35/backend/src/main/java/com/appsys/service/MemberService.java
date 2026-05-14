package com.appsys.service;

import com.appsys.entity.MemberLevel;
import com.appsys.entity.User;
import com.appsys.repository.MemberLevelRepository;
import com.appsys.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberLevelRepository memberLevelRepository;
    private final UserRepository userRepository;

    public MemberService(MemberLevelRepository memberLevelRepository, UserRepository userRepository) {
        this.memberLevelRepository = memberLevelRepository;
        this.userRepository = userRepository;
    }

    public List<MemberLevel> getAllMemberLevels() {
        return memberLevelRepository.findByStatusOrderByLevelAsc(1);
    }

    public MemberLevel getMemberLevelById(Long id) {
        return memberLevelRepository.findById(id).orElse(null);
    }

    public User getUserWithMemberLevel(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getMemberLevelId() != null) {
                MemberLevel level = memberLevelRepository.findById(user.getMemberLevelId()).orElse(null);
                user.setMemberLevel(level);
            }
            return user;
        }
        return null;
    }

    public void upgradeMemberLevel(Long userId, Integer growth) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setGrowth(user.getGrowth() + growth);
            MemberLevel currentLevel = memberLevelRepository.findFirstByRequiredGrowthLessThanEqualOrderByLevelDesc(user.getGrowth()).orElse(null);
            if (currentLevel != null && !currentLevel.getId().equals(user.getMemberLevelId())) {
                user.setMemberLevelId(currentLevel.getId());
                if (currentLevel.getHasAccountManager()) {
                    user.setAccountManager("专属客服-13800138000");
                }
            }
            userRepository.save(user);
        }
    }
}
