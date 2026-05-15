package com.chatsystem.service;

import com.chatsystem.entity.ChatMessage;
import com.chatsystem.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 聊天消息服务类
 */
@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private static final String UPLOAD_DIR = "uploads";

    /**
     * 获取聊天历史记录
     */
    public List<ChatMessage> getChatHistory(Long userId1, Long userId2, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sendTime"));
        Page<ChatMessage> messagePage = chatMessageRepository.findChatHistory(userId1, userId2, pageable);
        return messagePage.getContent();
    }

    /**
     * 标记消息为已读
     */
    @Transactional
    public int markAsRead(Long fromUserId, Long toUserId) {
        return chatMessageRepository.markAsRead(fromUserId, toUserId);
    }

    /**
     * 获取未读消息数量
     */
    public long getUnreadCount(Long fromUserId, Long toUserId) {
        return chatMessageRepository.countByFromUserIdAndToUserIdAndStatus(fromUserId, toUserId, 0);
    }

    /**
     * 撤回消息
     */
    @Transactional
    public ChatMessage recallMessage(Long messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));

        // 检查是否在2分钟内（简化处理）
        message.setStatus(2);
        return chatMessageRepository.save(message);
    }

    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 创建上传目录
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;

        // 保存文件
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        return "/uploads/" + newFilename;
    }

    /**
     * 保存消息（REST方式）
     */
    @Transactional
    public ChatMessage saveMessage(Long fromUserId, Long toUserId, Integer type, String content,
                                   String fileName, String fileUrl, Long fileSize) {
        ChatMessage message = new ChatMessage();
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setType(type);
        message.setContent(content);
        message.setFileName(fileName);
        message.setFileUrl(fileUrl);
        message.setFileSize(fileSize);
        message.setStatus(0);
        message.setSendTime(LocalDateTime.now());

        return chatMessageRepository.save(message);
    }

    /**
     * 批量发送消息
     */
    @Transactional
    public void batchSendMessages(Long fromUserId, List<Long> toUserIds, Integer type, String content) {
        for (Long toUserId : toUserIds) {
            ChatMessage message = new ChatMessage();
            message.setFromUserId(fromUserId);
            message.setToUserId(toUserId);
            message.setType(type);
            message.setContent(content);
            message.setStatus(0);
            message.setSendTime(LocalDateTime.now());
            chatMessageRepository.save(message);
        }
    }

    /**
     * 获取用户所有消息
     */
    public List<ChatMessage> getAllMessagesByUser(Long userId) {
        return chatMessageRepository.findAllByUserId(userId);
    }
}
