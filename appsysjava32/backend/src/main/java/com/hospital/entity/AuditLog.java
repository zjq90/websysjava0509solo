package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 审计日志实体类
 * 记录关键操作日志，保留不少于6个月
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_audit_log")
@Schema(description = "审计日志")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID")
    private Long id;

    @Column(length = 50)
    @Schema(description = "操作模块")
    private String module;

    @Column(length = 100)
    @Schema(description = "操作类型：登录 登出 新增 修改 删除 查询 支付 取消")
    private String operation;

    @Column(length = 200)
    @Schema(description = "操作描述")
    private String description;

    @Column
    @Schema(description = "操作用户ID")
    private Long userId;

    @Column(length = 50)
    @Schema(description = "操作用户名")
    private String username;

    @Column(length = 50)
    @Schema(description = "操作用户姓名")
    private String realName;

    @Column(length = 50)
    @Schema(description = "请求方法")
    private String requestMethod;

    @Column(length = 500)
    @Schema(description = "请求URL")
    private String requestUrl;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "请求参数")
    private String requestParams;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "响应结果")
    private String responseResult;

    @Column(length = 50)
    @Schema(description = "操作IP")
    private String ipAddress;

    @Column(length = 200)
    @Schema(description = "操作地点")
    private String location;

    @Column(length = 200)
    @Schema(description = "浏览器/客户端信息")
    private String userAgent;

    @Column(nullable = false)
    @Schema(description = "操作状态：0失败 1成功")
    private Integer status = 1;

    @Column(length = 500)
    @Schema(description = "错误信息")
    private String errorMessage;

    @Column
    @Schema(description = "执行耗时(毫秒)")
    private Long executeTime;

    @Column(nullable = false, updatable = false)
    @Schema(description = "操作时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }
}
