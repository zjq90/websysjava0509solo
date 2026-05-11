package com.hospital.repository;

import com.hospital.entity.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 通知设置Repository接口
 * 提供通知设置数据访问层的基本操作
 * 
 * @author hospital
 * @version 1.0.0
 */
@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long>, JpaSpecificationExecutor<NotificationSetting> {

    /**
     * 根据用户ID查询通知设置
     * 
     * @param userId 用户ID
     * @return 通知设置对象
     */
    Optional<NotificationSetting> findByUserId(Long userId);
}
