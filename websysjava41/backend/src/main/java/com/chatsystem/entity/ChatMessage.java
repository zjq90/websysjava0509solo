package com.chatsystem.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 */
@Data
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    /**
     * 消息ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发送者ID
     */
    @Column(nullable = false)
    private Long fromUserId;

    /**
     * 接收者ID
     */
    @Column(nullable = false)
    private Long toUserId;

    /**
     * 消息类型：0-文本，1-图片，2-文件，3-表情
     */
    @Column(nullable = false)
    private Integer type = 0;

    /**
     * 消息内容
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 文件URL（图片/文件消息使用）
     */
    @Column(length = 255)
    private String fileUrl;

    /**
     * 文件名
     */
    @Column(length = 100)
    private String fileName;

    /**
     * 文件大小
     */
    @Column
    private Long fileSize;

    /**
     * 状态：0-未读，1-已读，2-已撤回
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 发送时间
     */
    @Column(nullable = false)
    private LocalDateTime sendTime;

    @PrePersist
    protected void onCreate() {
        sendTime = LocalDateTime.now();
    }
}
