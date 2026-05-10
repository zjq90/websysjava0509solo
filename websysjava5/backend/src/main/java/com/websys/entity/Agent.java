package com.websys.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 代理商实体类
 * 
 * @author websys
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_agent")
public class Agent {

    /**
     * 代理商ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 代理商名称
     */
    @Column(nullable = false, length = 100)
    private String agentName;

    /**
     * 代理商编码
     */
    @Column(nullable = false, unique = true, length = 50)
    private String agentCode;

    /**
     * 父级代理商ID（顶级代理商为0或null）
     */
    private Long parentId;

    /**
     * 代理商级别（1-顶级，2-二级，3-三级...）
     */
    @Column(nullable = false)
    private Integer level;

    /**
     * 联系人姓名
     */
    @Column(length = 50)
    private String contactName;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Column(length = 100)
    private String contactEmail;

    /**
     * 地址
     */
    @Column(length = 500)
    private String address;

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
