package com.club.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.club.management.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天消息Mapper接口
 *
 * @author club-management
 * @since 2024-01-01
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
}
