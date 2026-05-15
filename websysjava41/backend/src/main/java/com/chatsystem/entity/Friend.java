package com.chatsystem.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 好友关系实体类
 */
@Data
@Entity
@Table(name = "sys_friend")
public class Friend {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 好友ID
     */
    @Column(nullable = false)
    private Long friendId;

    /**
     * 好友备注名
     */
    @Column(length = 50)
    private String remark;

    /**
     * 状态：0-待确认，1-已添加，2-已拒绝
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
