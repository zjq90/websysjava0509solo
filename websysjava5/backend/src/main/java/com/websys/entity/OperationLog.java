package com.websys.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 
 * @author websys
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_operation_log")
public class OperationLog {

    /**
     * 日志ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作用户ID
     */
    private Long userId;

    /**
     * 操作用户名
     */
    @Column(length = 50)
    private String username;

    /**
     * 操作类型（LOGIN-登录，LOGOUT-登出，CREATE-创建，UPDATE-更新，DELETE-删除，QUERY-查询）
     */
    @Column(nullable = false, length = 20)
    private String operationType;

    /**
     * 操作模块（AGENT-代理商管理，USER-用户管理，CONFIG-系统配置，DEVICE-设备管理等）
     */
    @Column(nullable = false, length = 30)
    private String module;

    /**
     * 操作描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 操作对象ID
     */
    private Long targetId;

    /**
     * 操作对象名称
     */
    @Column(length = 200)
    private String targetName;

    /**
     * 请求方法
     */
    @Column(length = 10)
    private String requestMethod;

    /**
     * 请求URL
     */
    @Column(length = 500)
    private String requestUrl;

    /**
     * 请求参数（JSON格式）
     */
    @Column(columnDefinition = "TEXT")
    private String requestParams;

    /**
     * 响应结果（JSON格式）
     */
    @Column(columnDefinition = "TEXT")
    private String responseResult;

    /**
     * 操作IP地址
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * 操作地点
     */
    @Column(length = 100)
    private String location;

    /**
     * 浏览器信息
     */
    @Column(length = 500)
    private String browser;

    /**
     * 操作系统
     */
    @Column(length = 100)
    private String os;

    /**
     * 操作状态（1-成功，0-失败）
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 错误信息
     */
    @Column(columnDefinition = "TEXT")
    private String errorMsg;

    /**
     * 操作耗时（毫秒）
     */
    private Long costTime;

    /**
     * 操作时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
