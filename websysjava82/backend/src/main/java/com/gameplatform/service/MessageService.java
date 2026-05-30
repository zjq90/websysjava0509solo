package com.gameplatform.service;

import com.gameplatform.entity.Message;
import com.gameplatform.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PushTaskService pushTaskService;

    public List<Message> findByUserId(Long userId) {
        return messageRepository.findByUserId(userId);
    }

    public List<Message> findUnreadByUserId(Long userId) {
        return messageRepository.findByUserIdAndRead(userId, false);
    }

    public List<Message> findByTaskId(Long taskId) {
        return messageRepository.findByTaskId(taskId);
    }

    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Message markAsRead(Long id) {
        Optional<Message> opt = messageRepository.findById(id);
        if (opt.isPresent()) {
            Message message = opt.get();
            if (!message.getRead()) {
                message.setRead(true);
                message.setReadTime(LocalDateTime.now());
                message = messageRepository.save(message);
                if (message.getTaskId() != null) {
                    pushTaskService.updateOpenedCount(message.getTaskId());
                }
            }
            return message;
        }
        return null;
    }

    public void markAllAsRead(Long userId) {
        List<Message> messages = messageRepository.findByUserIdAndRead(userId, false);
        for (Message message : messages) {
            message.setRead(true);
            message.setReadTime(LocalDateTime.now());
            messageRepository.save(message);
            if (message.getTaskId() != null) {
                pushTaskService.updateOpenedCount(message.getTaskId());
            }
        }
    }

    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }
}
