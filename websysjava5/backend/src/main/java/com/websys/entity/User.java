package com.websys.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author websys
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_user")
public class User {

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码（加密存储）
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * 真实姓名
     */
    @Column(length = 50)
    private String realName;

    /**
     * 所属代理商ID（超级管理员为null）
     */
    private Long agentId;

    /**
     * 角色类型（ADMIN-超级管理员，AGENT_ADMIN-代理商管理员，AGENT_USER-代理商普通用户）
     */
    @Column(nullable = false, length = 20)
    private String roleType;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 状态（1-正常，0-禁用）
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

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
