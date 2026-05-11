package com.hospital.service;

import com.hospital.entity.Message;
import com.hospital.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息服务类
 * 处理消息通知业务
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    /**
     * 创建消息
     * 
     * @param userId 接收用户ID
     * @param title 消息标题
     * @param content 消息内容
     * @param messageType 消息类型
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 消息对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Message createMessage(Long userId, String title, String content, 
                                  String messageType, String businessType, Long businessId) {
        Message message = new Message();
        message.setUserId(userId);
        message.setTitle(title);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setBusinessType(businessType);
        message.setBusinessId(businessId);
        message.setIsRead(0);
        message.setSendStatus("SENT");
        message.setSendTime(LocalDateTime.now());
        message.setChannel("IN_APP");
        
        return messageRepository.save(message);
    }

    /**
     * 获取用户消息列表
     * 
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> getUserMessages(Long userId) {
        return messageRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 统计未读消息数量
     * 
     * @param userId 用户ID
     * @return 未读数量
     */
    public Long countUnread(Long userId) {
        return messageRepository.countByUserIdAndIsRead(userId, 0);
    }

    /**
     * 标记消息已读
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));

        if (!message.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }

        if (message.getIsRead() == 0) {
            message.setIsRead(1);
            message.setReadTime(LocalDateTime.now());
            messageRepository.save(message);
        }
    }

    /**
     * 标记所有消息已读
     * 
     * @param userId 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long userId) {
        List<Message> messages = messageRepository.findByUserIdOrderByCreateTimeDesc(userId);
        LocalDateTime now = LocalDateTime.now();
        
        for (Message message : messages) {
            if (message.getIsRead() == 0) {
                message.setIsRead(1);
                message.setReadTime(now);
            }
        }
        
        messageRepository.saveAll(messages);
    }

    /**
     * 根据ID获取消息详情
     * 
     * @param id 消息ID
     * @param userId 用户ID
     * @return 消息详情
     */
    public Message getMessageById(Long id, Long userId) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("消息不存在"));

        if (!message.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看");
        }

        return message;
    }

    /**
     * 创建医生停诊通知
     * 
     * @param appointment 预约信息
     * @param reason 停诊原因
     * @param alternativeDoctors 替代医生列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void createDoctorCancelMessage(com.hospital.entity.Appointment appointment, 
                                           String reason, 
                                           List<com.hospital.entity.Doctor> alternativeDoctors) {
        StringBuilder content = new StringBuilder();
        content.append("您好，您预约的医生因").append(reason).append("临时停诊。");
        
        if (alternativeDoctors != null && !alternativeDoctors.isEmpty()) {
            content.append("为您推荐以下替代医生：");
            for (int i = 0; i < Math.min(alternativeDoctors.size(), 3); i++) {
                com.hospital.entity.Doctor doc = alternativeDoctors.get(i);
                content.append("\n").append(i + 1).append(". ")
                       .append(doc.getDoctorName()).append("（")
                       .append(doc.getTitle() != null ? doc.getTitle() : "").append("）");
            }
        }
        
        createMessage(
                appointment.getPatientId(),
                "医生停诊通知",
                content.toString(),
                "DOCTOR_CANCEL",
                "APPOINTMENT",
                appointment.getId()
        );
    }
}
