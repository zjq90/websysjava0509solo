package com.flower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 文章实体类
 * 对应数据库表article
 */
@Data
@Entity
@Table(name = "article")
public class Article {

    /**
     * 文章ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章标题
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 文章内容
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 文章分类：1-养护知识，2-花语文化，3-花艺教程
     */
    @Column(nullable = false)
    private Integer category;

    /**
     * 封面图片
     */
    @Column(length = 500)
    private String coverImage;

    /**
     * SEO关键词
     */
    @Column(length = 200)
    private String seoKeywords;

    /**
     * SEO描述
     */
    @Column(length = 500)
    private String seoDescription;

    /**
     * 浏览量
     */
    private Integer viewCount = 0;

    /**
     * 状态：0-草稿，1-已发布，2-已下架
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 作者
     */
    @Column(length = 50)
    private String author;

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
