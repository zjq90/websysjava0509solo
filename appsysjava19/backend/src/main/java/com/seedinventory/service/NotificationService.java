package com.seedinventory.service;

import com.seedinventory.entity.Notification;
import com.seedinventory.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 消息通知服务类
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    /**
     * 查询所有通知
     */
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }
    
    /**
     * 根据状态查询
     */
    public List<Notification> findByStatus(String status) {
        return notificationRepository.findByStatusOrderByCreateTimeDesc(status);
    }
    
    /**
     * 查询未读通知
     */
    public List<Notification> findUnread() {
        return notificationRepository.findByStatusOrderByCreateTimeDesc("UNREAD");
    }
    
    /**
     * 统计未读消息数量
     */
    public long countUnread() {
        return notificationRepository.countByStatus("UNREAD");
    }
    
    /**
     * 标记为已读
     */
    @Transactional(rollbackFor = Exception.class)
    public Notification markAsRead(Long id) {
        Optional<Notification> opt = notificationRepository.findById(id);
        if (opt.isPresent()) {
            Notification notification = opt.get();
            notification.setStatus("READ");
            notification.setReadTime(LocalDateTime.now());
            return notificationRepository.save(notification);
        }
        throw new RuntimeException("通知不存在");
    }
    
    /**
     * 标记为已处理
     */
    @Transactional(rollbackFor = Exception.class)
    public Notification markAsHandled(Long id, String handler, String remark) {
        Optional<Notification> opt = notificationRepository.findById(id);
        if (opt.isPresent()) {
            Notification notification = opt.get();
            notification.setStatus("HANDLED");
            notification.setHandler(handler);
            notification.setHandleTime(LocalDateTime.now());
            notification.setHandleRemark(remark);
            return notificationRepository.save(notification);
        }
        throw new RuntimeException("通知不存在");
    }
    
    /**
     * 批量标记已读
     */
    @Transactional(rollbackFor = Exception.class)
    public int markAllAsRead() {
        List<Notification> unreadList = notificationRepository.findByStatusOrderByCreateTimeDesc("UNREAD");
        for (Notification notification : unreadList) {
            notification.setStatus("READ");
            notification.setReadTime(LocalDateTime.now());
            notificationRepository.save(notification);
        }
        return unreadList.size();
    }
    
    /**
     * 根据ID查询
     */
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }
}
