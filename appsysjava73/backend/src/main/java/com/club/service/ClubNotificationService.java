package com.club.service;

import com.club.common.PageResult;
import com.club.entity.ClubNotification;
import com.club.repository.ClubNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class ClubNotificationService {

    @Autowired
    private ClubNotificationRepository clubNotificationRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PageResult<ClubNotification> getMyNotifications(Long userId, String type, Integer pageNum, Integer pageSize) {
        log.info("获取通知列表 - userId: {}, type: {}, pageNum: {}, pageSize: {}", userId, type, pageNum, pageSize);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubNotification> page;

        if (type != null && !type.isEmpty()) {
            page = clubNotificationRepository.findByUserIdAndTypeAndDeletedFalseOrderByCreateTimeDesc(userId, type, pageable);
        } else {
            page = clubNotificationRepository.findByUserIdAndDeletedFalseOrderByCreateTimeDesc(userId, pageable);
        }

        return PageResult.of(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long notificationId, Long userId) {
        log.info("标记通知已读 - notificationId: {}, userId: {}", notificationId, userId);

        ClubNotification notification = clubNotificationRepository.findByIdAndUserIdAndDeletedFalse(notificationId, userId)
                .orElseThrow(() -> new RuntimeException("通知不存在"));

        notification.setRead(1);
        notification.setReadTime(LocalDateTime.now().format(FORMATTER));
        clubNotificationRepository.save(notification);

        log.info("通知标记已读成功 - notificationId: {}", notificationId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long userId) {
        log.info("标记所有通知已读 - userId: {}", userId);
        clubNotificationRepository.markAllAsRead(userId);
        log.info("所有通知标记已读成功 - userId: {}", userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteNotification(Long notificationId, Long userId) {
        log.info("删除通知 - notificationId: {}, userId: {}", notificationId, userId);

        ClubNotification notification = clubNotificationRepository.findByIdAndUserIdAndDeletedFalse(notificationId, userId)
                .orElseThrow(() -> new RuntimeException("通知不存在"));

        notification.setDeleted(true);
        clubNotificationRepository.save(notification);

        log.info("通知删除成功 - notificationId: {}", notificationId);
    }

    public long getUnreadCount(Long userId) {
        return clubNotificationRepository.countByUserIdAndReadAndDeletedFalse(userId, 0);
    }
}
