package com.hospital.repository;

import com.hospital.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息Repository接口
 * 提供消息数据访问层的基本操作
 * 
 * @author hospital
 * @version 1.0.0
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

    /**
     * 查询用户的消息列表
     * 
     * @param userId 用户ID
     * @return 消息列表
     */
    List<Message> findByUserIdOrderByCreateTimeDesc(Long userId);

    /**
     * 统计用户未读消息数量
     * 
     * @param userId 用户ID
     * @param isRead 是否已读
     * @return 未读数量
     */
    Long countByUserIdAndIsRead(Long userId, Integer isRead);

    /**
     * 查询待发送的消息
     * 
     * @param sendStatus 发送状态
     * @param scheduledTime 预约发送时间
     * @return 消息列表
     */
    List<Message> findBySendStatusAndScheduledTimeLessThanEqualOrderByCreateTimeAsc(String sendStatus, LocalDateTime scheduledTime);
}
