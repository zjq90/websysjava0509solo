package com.club.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.ResultCode;
import com.club.management.entity.NotificationSetting;
import com.club.management.entity.SysMessage;
import com.club.management.exception.BusinessException;
import com.club.management.mapper.NotificationSettingMapper;
import com.club.management.mapper.SysMessageMapper;
import com.club.management.service.MessageService;
import com.club.management.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 消息服务实现类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Autowired
    private NotificationSettingMapper notificationSettingMapper;

    @Override
    public PageResult<SysMessage> getMessagePage(Page<SysMessage> page, String type) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMessage::getUserId, userId);

        if (StringUtils.hasText(type)) {
            wrapper.eq(SysMessage::getType, type);
        }

        wrapper.orderByDesc(SysMessage::getCreateTime);

        Page<SysMessage> messagePage = sysMessageMapper.selectPage(page, wrapper);

        log.debug("分页查询消息列表, 用户ID: {}, 类型: {}, 总数: {}", userId, type, messagePage.getTotal());

        return new PageResult<>(messagePage.getTotal(), messagePage.getCurrent(), messagePage.getSize(), messagePage.getRecords());
    }

    @Override
    public SysMessage getMessageDetail(Long id) {
        Long userId = UserContext.getUserId();

        SysMessage message = sysMessageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (!message.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        log.debug("获取消息详情, 消息ID: {}", id);

        return message;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id) {
        Long userId = UserContext.getUserId();

        SysMessage message = sysMessageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (!message.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        message.setIsRead(1);
        sysMessageMapper.updateById(message);

        log.debug("标记消息已读, 用户ID: {}, 消息ID: {}", userId, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(String type) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMessage::getUserId, userId)
                .eq(SysMessage::getIsRead, 0);

        if (StringUtils.hasText(type)) {
            wrapper.eq(SysMessage::getType, type);
        }

        List<SysMessage> messages = sysMessageMapper.selectList(wrapper);
        for (SysMessage message : messages) {
            message.setIsRead(1);
            sysMessageMapper.updateById(message);
        }

        log.info("标记所有消息已读, 用户ID: {}, 类型: {}, 数量: {}", userId, type, messages.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Long id) {
        Long userId = UserContext.getUserId();

        SysMessage message = sysMessageMapper.selectById(id);
        if (message == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }

        if (!message.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        sysMessageMapper.deleteById(id);

        log.info("删除消息成功, 用户ID: {}, 消息ID: {}", userId, id);
    }

    @Override
    public Integer getUnreadCount(String type) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMessage::getUserId, userId)
                .eq(SysMessage::getIsRead, 0);

        if (StringUtils.hasText(type)) {
            wrapper.eq(SysMessage::getType, type);
        }

        Long count = sysMessageMapper.selectCount(wrapper);

        log.debug("获取未读消息数, 用户ID: {}, 类型: {}, 数量: {}", userId, type, count);

        return count.intValue();
    }

    @Override
    public NotificationSetting getNotificationSetting() {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<NotificationSetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NotificationSetting::getUserId, userId);
        NotificationSetting setting = notificationSettingMapper.selectOne(wrapper);

        if (setting == null) {
            setting = new NotificationSetting();
            setting.setUserId(userId);
            setting.setActivityReminder(1);
            setting.setClubAnnouncement(1);
            setting.setNewMemberNotice(1);
            setting.setActivitySignInReminder(1);
            setting.setChatMessageNotice(1);
            notificationSettingMapper.insert(setting);
        }

        log.debug("获取通知设置, 用户ID: {}", userId);

        return setting;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotificationSetting(NotificationSetting setting) {
        Long userId = UserContext.getUserId();

        setting.setUserId(userId);

        LambdaQueryWrapper<NotificationSetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NotificationSetting::getUserId, userId);
        NotificationSetting existing = notificationSettingMapper.selectOne(wrapper);

        if (existing != null) {
            setting.setId(existing.getId());
            notificationSettingMapper.updateById(setting);
        } else {
            notificationSettingMapper.insert(setting);
        }

        log.info("更新通知设置成功, 用户ID: {}", userId);
    }
}
