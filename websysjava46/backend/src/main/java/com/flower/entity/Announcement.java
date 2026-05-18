package com.flower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 公告实体类
 * 对应数据库表announcement
 */
@Data
@Entity
@Table(name = "announcement")
public class Announcement {

    /**
     * 公告ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 公告标题
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 公告内容
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 公告类型：1-系统通知，2-活动预告，3-重要提醒
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 是否置顶：0-否，1-是
     */
    @Column(nullable = false)
    private Integer topFlag = 0;

    /**
     * 状态：0-草稿，1-已发布，2-已撤回
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
