package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 用户实体类
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity {

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码（加密存储）
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 手机号（RSA加密存储）
     */
    @Column(name = "phone", length = 255)
    private String phone;

    /**
     * 邮箱（RSA加密存储）
     */
    @Column(name = "email", length = 255)
    private String email;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 50)
    private String nickname;

    /**
     * 头像URL
     */
    @Column(name = "avatar", length = 500)
    private String avatar;

    /**
     * 用户类型：1-普通用户，2-专家，3-管理员
     */
    @Column(name = "user_type", nullable = false)
    private Integer userType = 1;

    /**
     * 状态：0-禁用，1-启用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 是否长辈模式：0-关闭，1-开启
     */
    @Column(name = "elder_mode", nullable = false)
    private Integer elderMode = 0;
}