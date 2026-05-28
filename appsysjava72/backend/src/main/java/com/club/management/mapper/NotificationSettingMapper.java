package com.club.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.club.management.entity.NotificationSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息通知设置Mapper接口
 *
 * @author club-management
 * @since 2024-01-01
 */
@Mapper
public interface NotificationSettingMapper extends BaseMapper<NotificationSetting> {
}
