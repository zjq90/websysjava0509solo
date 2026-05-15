package com.teaching.service;

import com.teaching.entity.Message;
import com.teaching.entity.User;
import com.teaching.repository.MessageRepository;
import com.teaching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, String title, String content) {
        User sender = senderId != null ? userRepository.findById(senderId).orElse(null) : null;
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("接收者不存在"));
        
        Message message = new Message();
        message.setSenderId(senderId);
        message.setSenderName(sender != null ? (sender.getRealName() != null ? sender.getRealName() : sender.getUsername()) : "系统");
        message.setReceiverId(receiverId);
        message.setReceiverName(receiver.getRealName() != null ? receiver.getRealName() : receiver.getUsername());
        message.setTitle(title);
        message.setContent(content);
        message.setIsRead(false);
        message.setType(1);
        message.setStatus(1);
        
        return messageRepository.save(message);
    }

    public List<Message> getUserMessages(Long userId) {
        return messageRepository.findByReceiverIdOrderByCreateTimeDesc(userId);
    }

    public List<Message> getUnreadMessages(Long userId) {
        return messageRepository.findByReceiverIdAndIsReadFalse(userId);
    }

    public int getUnreadCount(Long userId) {
        return messageRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    @Transactional
    public Message markAsRead(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new RuntimeException("消息不存在"));
        message.setIsRead(true);
        return messageRepository.save(message);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Message> messages = messageRepository.findByReceiverIdAndIsReadFalse(userId);
        for (Message message : messages) {
            message.setIsRead(true);
        }
        messageRepository.saveAll(messages);
    }

    @Transactional
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
