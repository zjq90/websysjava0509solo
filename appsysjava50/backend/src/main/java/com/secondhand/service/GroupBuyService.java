package com.secondhand.service;

import com.secondhand.entity.GroupBuyActivity;
import com.secondhand.entity.GroupBuyRecord;
import com.secondhand.entity.Product;
import com.secondhand.repository.GroupBuyActivityRepository;
import com.secondhand.repository.GroupBuyRecordRepository;
import com.secondhand.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 拼团服务类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Service
public class GroupBuyService {

    @Autowired
    private GroupBuyActivityRepository activityRepository;

    @Autowired
    private GroupBuyRecordRepository recordRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * 创建拼团活动
     */
    public GroupBuyActivity createActivity(GroupBuyActivity activity) {
        Product product = productRepository.findByIdAndIsDeletedFalse(activity.getProductId()).orElse(null);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        return activityRepository.save(activity);
    }

    /**
     * 获取所有有效拼团活动
     */
    public List<GroupBuyActivity> getActiveActivities() {
        List<GroupBuyActivity> activities = activityRepository.findByStatusOrderByCreateTimeDesc("ACTIVE");
        for (GroupBuyActivity activity : activities) {
            Product product = productRepository.findByIdAndIsDeletedFalse(activity.getProductId()).orElse(null);
            activity.setProduct(product);
        }
        return activities;
    }

    /**
     * 参与拼团
     */
    @Transactional
    public GroupBuyRecord joinActivity(Long userId, Long activityId, Long groupRecordId) {
        GroupBuyActivity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null || !"ACTIVE".equals(activity.getStatus())) {
            throw new RuntimeException("拼团活动不存在或已结束");
        }

        if (activity.getEndTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("拼团活动已结束");
        }

        GroupBuyRecord record;
        if (groupRecordId != null) {
            // 加入已有拼团
            record = recordRepository.findById(groupRecordId).orElse(null);
            if (record == null) {
                throw new RuntimeException("拼团不存在");
            }
            if ("SUCCESS".equals(record.getStatus())) {
                throw new RuntimeException("拼团已成团，不能再加入");
            }

            // 检查是否已经参与
            boolean alreadyJoined = recordRepository.existsByActivityIdAndUserId(activityId, userId);
            if (alreadyJoined) {
                throw new RuntimeException("您已经参与过此拼团");
            }

            int currentSize = recordRepository.countByGroupNo(record.getGroupNo());
            if (currentSize >= activity.getGroupSize()) {
                throw new RuntimeException("拼团人数已满");
            }

            // 创建参与记录
            GroupBuyRecord newRecord = new GroupBuyRecord();
            newRecord.setActivityId(activityId);
            newRecord.setGroupNo(record.getGroupNo());
            newRecord.setLeaderId(record.getLeaderId());
            newRecord.setUserId(userId);
            newRecord.setCurrentSize(currentSize + 1);
            newRecord = recordRepository.save(newRecord);

            // 检查是否成团
            if (newRecord.getCurrentSize() >= activity.getGroupSize()) {
                recordRepository.updateGroupStatus(record.getGroupNo(), "SUCCESS");
            }

            return newRecord;
        } else {
            // 开新团
            boolean alreadyJoined = recordRepository.existsByActivityIdAndUserId(activityId, userId);
            if (alreadyJoined) {
                throw new RuntimeException("您已经参与过此拼团");
            }

            String groupNo = "GB" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

            record = new GroupBuyRecord();
            record.setActivityId(activityId);
            record.setGroupNo(groupNo);
            record.setLeaderId(userId);
            record.setUserId(userId);
            record.setCurrentSize(1);
            record = recordRepository.save(record);
        }

        return record;
    }

    /**
     * 获取我的拼团记录
     */
    public List<GroupBuyRecord> getMyGroupRecords(Long userId) {
        return recordRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 获取拼团详情
     */
    public List<GroupBuyRecord> getGroupMembers(String groupNo) {
        return recordRepository.findByGroupNoOrderByCreateTimeAsc(groupNo);
    }

}
