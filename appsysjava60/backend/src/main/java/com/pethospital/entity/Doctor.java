package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 医生实体类
 * 存储医生的基本信息
 * 
 * @author Pet Hospital Team
 */
@Data
@Entity
@Table(name = "t_doctor")
public class Doctor {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 医生姓名
     */
    @Column(nullable = false, length = 50)
    private String name;
    
    /**
     * 手机号
     */
    @Column(nullable = false, unique = true, length = 20)
    private String phone;
    
    /**
     * 密码
     */
    @Column(nullable = false, length = 100)
    private String password;
    
    /**
     * 职称
     */
    @Column(length = 50)
    private String title;
    
    /**
     * 专长
     */
    @Column(length = 200)
    private String specialty;
    
    /**
     * 头像URL
     */
    @Column(length = 500)
    private String avatar;
    
    /**
     * 状态：0-禁用，1-启用
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
