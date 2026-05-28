package com.club.management.system.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 年级
     */
    private String grade;

    /**
     * 专业
     */
    private String major;

    /**
     * 学院
     */
    private String college;

    /**
     * 用户类型 0-普通用户 1-管理员 2-社团负责人
     */
    private Integer userType;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 个人简介
     */
    private String bio;
}
