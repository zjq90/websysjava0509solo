package com.club.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.entity.NotificationSetting;
import com.club.management.entity.SysMessage;

/**
 * 消息服务接口
 *
 * @author club-management
 * @since 2024-01-01
 */
public interface MessageService {

    /**
     * 分页查询消息列表
     */
    PageResult<SysMessage> getMessagePage(Page<SysMessage> page, String type);

    /**
     * 获取消息详情
     */
    SysMessage getMessageDetail(Long id);

    /**
     * 标记消息已读
     */
    void markAsRead(Long id);

    /**
     * 标记所有消息已读
     */
    void markAllAsRead(String type);

    /**
     * 删除消息
     */
    void deleteMessage(Long id);

    /**
     * 获取未读消息数
     */
    Integer getUnreadCount(String type);

    /**
     * 获取通知设置
     */
    NotificationSetting getNotificationSetting();

    /**
     * 更新通知设置
     */
    void updateNotificationSetting(NotificationSetting setting);
}
