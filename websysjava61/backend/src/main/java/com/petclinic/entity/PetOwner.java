package com.petclinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 宠物主人实体类
 */
@Data
@Entity
@Table(name = "pet_owner")
public class PetOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 手机号
     */
    @Column(nullable = false, length = 20)
    private String phone;

    /**
     * 身份证号
     */
    @Column(length = 20)
    private String idCard;

    /**
     * 实名认证状态：0-未认证，1-认证中，2-认证通过，3-认证驳回
     */
    @Column(nullable = false)
    private Integer realNameStatus = 0;

    /**
     * 信用分
     */
    @Column(nullable = false)
    private Integer creditScore = 100;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 地址
     */
    @Column(length = 500)
    private String address;

    /**
     * 头像URL
     */
    @Column(length = 500)
    private String avatarUrl;

    /**
     * 状态：0-禁用，1-正常
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
