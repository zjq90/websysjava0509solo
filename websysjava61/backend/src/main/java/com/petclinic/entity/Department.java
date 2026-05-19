package com.petclinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 科室实体类
 */
@Data
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 科室名称
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 科室编码
     */
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    /**
     * 科室描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 科室位置
     */
    @Column(length = 100)
    private String location;

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
