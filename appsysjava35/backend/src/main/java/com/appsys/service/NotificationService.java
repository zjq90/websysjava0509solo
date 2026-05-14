package com.appsys.service;

import com.appsys.entity.Notification;
import com.appsys.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrUserIdIsNullOrderByCreateTimeDesc(userId);
    }

    public List<Notification> getUserNotificationsByType(Long userId, Integer type) {
        return notificationRepository.findByUserIdAndTypeOrderByCreateTimeDesc(userId, type);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    public boolean markAsRead(Long id) {
        return notificationRepository.markAsRead(id) > 0;
    }

    public boolean markAllAsRead(Long userId) {
        return notificationRepository.markAllAsRead(userId) > 0;
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void sendBroadcastNotification(String title, String content, Integer type) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }
}
