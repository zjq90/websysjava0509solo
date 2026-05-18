package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 消息通知实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "message")
public class Message extends BaseEntity {

    /**
     * 接收用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 消息类型：1-系统通知，2-鉴定通知，3-保养提醒，4-异常告警
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 消息标题
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 消息内容
     */
    @Column(name = "content", length = 2000)
    private String content;

    /**
     * 关联业务ID
     */
    @Column(name = "business_id")
    private Long businessId;

    /**
     * 关联业务类型
     */
    @Column(name = "business_type", length = 50)
    private String businessType;

    /**
     * 是否已读：0-未读，1-已读
     */
    @Column(name = "is_read", nullable = false)
    private Integer isRead = 0;

    /**
     * 跳转URL
     */
    @Column(name = "jump_url", length = 500)
    private String jumpUrl;
}