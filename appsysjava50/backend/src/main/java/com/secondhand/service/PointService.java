package com.secondhand.service;

import com.secondhand.entity.PointRecord;
import com.secondhand.entity.User;
import com.secondhand.repository.PointRecordRepository;
import com.secondhand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 积分服务类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Service
public class PointService {

    @Autowired
    private PointRecordRepository pointRecordRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 添加积分
     */
    @Transactional
    public PointRecord addPoints(Long userId, Integer points, String type, String description, Long relatedId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setPoints(user.getPoints() + points);
        userRepository.save(user);

        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setChangeType(type);
        record.setDescription(description);
        record.setRelatedId(relatedId);
        return pointRecordRepository.save(record);
    }

    /**
     * 扣减积分
     */
    @Transactional
    public PointRecord deductPoints(Long userId, Integer points, String type, String description, Long relatedId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (user.getPoints() < points) {
            throw new RuntimeException("积分不足");
        }

        user.setPoints(user.getPoints() - points);
        userRepository.save(user);

        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(-points);
        record.setChangeType(type);
        record.setDescription(description);
        record.setRelatedId(relatedId);
        return pointRecordRepository.save(record);
    }

    /**
     * 获取用户积分记录
     */
    public List<PointRecord> getUserPointRecords(Long userId) {
        return pointRecordRepository.findByUserIdOrderByCreatedTimeDesc(userId);
    }

    /**
     * 获取用户积分
     */
    public Integer getUserPoints(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId).orElse(null);
        if (user == null) {
            return 0;
        }
        return user.getPoints();
    }

}
