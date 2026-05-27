package com.club.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * 包含所有实体共有的字段：ID、创建时间、更新时间
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * 主键ID，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间，自动生成
     */
    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间，自动更新
     */
    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
}
