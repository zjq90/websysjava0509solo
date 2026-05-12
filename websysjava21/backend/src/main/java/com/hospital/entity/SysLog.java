package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 系统日志实体类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_log")
@Schema(description = "系统日志")
public class SysLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID", example = "1")
    private Long id;

    @Column(length = 50)
    @Schema(description = "操作人用户名", example = "admin")
    private String username;

    @Column(length = 100)
    @Schema(description = "操作人IP", example = "192.168.1.1")
    private String ip;

    @Column(length = 200)
    @Schema(description = "请求URL", example = "/api/system/user/list")
    private String url;

    @Column(length = 20)
    @Schema(description = "请求方法", example = "GET")
    private String method;

    @Column(length = 500)
    @Schema(description = "请求参数")
    private String params;

    @Column(length = 100)
    @Schema(description = "操作模块", example = "用户管理")
    private String module;

    @Column(length = 100)
    @Schema(description = "操作描述", example = "查询用户列表")
    private String description;

    @Column
    @Schema(description = "执行时长(毫秒)", example = "100")
    private Long duration;

    @Column(length = 20)
    @Schema(description = "日志类型：operation-操作日志，error-错误日志", example = "operation")
    private String logType;

    @Column(length = 2000)
    @Schema(description = "异常信息")
    private String exception;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
