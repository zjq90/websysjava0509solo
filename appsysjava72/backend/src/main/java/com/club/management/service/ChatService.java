package com.club.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.entity.ChatMessage;

/**
 * 聊天服务接口
 *
 * @author club-management
 * @since 2024-01-01
 */
public interface ChatService {

    /**
     * 发送消息
     */
    ChatMessage sendMessage(Long clubId, String messageType, String content, String mediaUrl, Integer duration);

    /**
     * 分页获取聊天消息
     */
    PageResult<ChatMessage> getChatMessages(Page<ChatMessage> page, Long clubId);

    /**
     * 标记消息已读
     */
    void markMessagesAsRead(Long clubId);

    /**
     * 获取未读消息数
     */
    Integer getUnreadCount(Long clubId);
}
