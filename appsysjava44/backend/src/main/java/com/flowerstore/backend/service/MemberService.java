package com.flowerstore.backend.service;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.MemberLevel;
import com.flowerstore.backend.entity.PointRecord;
import com.flowerstore.backend.entity.User;
import com.flowerstore.backend.repository.MemberLevelRepository;
import com.flowerstore.backend.repository.PointRecordRepository;
import com.flowerstore.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 会员服务类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberLevelRepository memberLevelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRecordRepository pointRecordRepository;

    /**
     * 获取会员等级列表
     */
    public Result<List<MemberLevel>> getMemberLevels() {
        List<MemberLevel> levels = memberLevelRepository.findByStatusOrderBySortOrderAsc(1);
        return Result.success(levels);
    }

    /**
     * 获取用户会员信息
     */
    public Result<Map<String, Object>> getUserMemberInfo(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Result.error("用户不存在");
        }
        User user = userOpt.get();
        
        MemberLevel currentLevel = memberLevelRepository.findById(user.getMemberLevelId()).orElse(null);
        MemberLevel nextLevel = null;
        
        if (currentLevel != null) {
            List<MemberLevel> levels = memberLevelRepository.findByStatusOrderBySortOrderAsc(1);
            for (int i = 0; i < levels.size(); i++) {
                if (levels.get(i).getId().equals(currentLevel.getId()) && i < levels.size() - 1) {
                    nextLevel = levels.get(i + 1);
                    break;
                }
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("currentLevel", currentLevel);
        result.put("nextLevel", nextLevel);
        
        return Result.success(result);
    }

    /**
     * 获取积分记录
     */
    public Result<List<PointRecord>> getPointRecords(Long userId) {
        List<PointRecord> records = pointRecordRepository.findByUserIdOrderByCreateTimeDesc(userId);
        return Result.success(records);
    }

    /**
     * 积分兑换礼品
     */
    @Transactional
    public Result<String> exchangeGift(Long userId, Integer points, String giftName) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Result.error("用户不存在");
        }
        User user = userOpt.get();
        
        if (user.getCurrentPoints() < points) {
            return Result.error("积分不足");
        }
        
        user.setCurrentPoints(user.getCurrentPoints() - points);
        userRepository.save(user);
        
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(-points);
        record.setType(2);
        record.setDescription("兑换" + giftName);
        pointRecordRepository.save(record);
        
        return Result.success("兑换成功");
    }
}
