package com.club.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.entity.ChatMessage;
import com.club.management.mapper.ChatMessageMapper;
import com.club.management.service.ChatService;
import com.club.management.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 聊天服务实现类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatMessage sendMessage(Long clubId, String messageType, String content, String mediaUrl, Integer duration) {
        Long userId = UserContext.getUserId();

        ChatMessage message = new ChatMessage();
        message.setClubId(clubId);
        message.setSenderId(userId);
        message.setMessageType(messageType);
        message.setContent(content);
        message.setMediaUrl(mediaUrl);
        message.setDuration(duration);
        message.setIsRead(0);
        chatMessageMapper.insert(message);

        log.info("发送聊天消息成功, 发送人ID: {}, 社团ID: {}, 消息类型: {}, 消息ID: {}", userId, clubId, messageType, message.getId());

        return message;
    }

    @Override
    public PageResult<ChatMessage> getChatMessages(Page<ChatMessage> page, Long clubId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getClubId, clubId);
        wrapper.orderByDesc(ChatMessage::getCreateTime);

        Page<ChatMessage> messagePage = chatMessageMapper.selectPage(page, wrapper);

        log.debug("分页获取聊天消息, 社团ID: {}, 总数: {}", clubId, messagePage.getTotal());

        return new PageResult<>(messagePage.getTotal(), messagePage.getCurrent(), messagePage.getSize(), messagePage.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markMessagesAsRead(Long clubId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getClubId, clubId)
                .ne(ChatMessage::getSenderId, userId)
                .eq(ChatMessage::getIsRead, 0);

        chatMessageMapper.selectList(wrapper).forEach(message -> {
            message.setIsRead(1);
            chatMessageMapper.updateById(message);
        });

        log.info("标记聊天消息已读, 用户ID: {}, 社团ID: {}", userId, clubId);
    }

    @Override
    public Integer getUnreadCount(Long clubId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getClubId, clubId)
                .ne(ChatMessage::getSenderId, userId)
                .eq(ChatMessage::getIsRead, 0);

        Long count = chatMessageMapper.selectCount(wrapper);

        log.debug("获取未读聊天消息数, 用户ID: {}, 社团ID: {}, 数量: {}", userId, clubId, count);

        return count.intValue();
    }
}
