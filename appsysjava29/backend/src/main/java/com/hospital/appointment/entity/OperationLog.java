package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "operation_log")
@Schema(description = "操作日志")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID")
    private Long id;

    @Column(name = "user_id")
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "username", length = 50)
    @Schema(description = "用户名")
    private String username;

    @Column(name = "module", length = 100)
    @Schema(description = "模块")
    private String module;

    @Column(name = "operation", length = 100)
    @Schema(description = "操作")
    private String operation;

    @Column(name = "method", length = 255)
    @Schema(description = "方法")
    private String method;

    @Column(name = "request_params", columnDefinition = "TEXT")
    @Schema(description = "请求参数")
    private String requestParams;

    @Column(name = "request_uri", length = 255)
    @Schema(description = "请求URI")
    private String requestUri;

    @Column(name = "ip", length = 50)
    @Schema(description = "IP地址")
    private String ip;

    @Column(name = "user_agent", length = 255)
    @Schema(description = "用户代理")
    private String userAgent;

    @Column(name = "status")
    @Schema(description = "状态: 1-成功 0-失败")
    private Integer status;

    @Column(name = "error_msg", columnDefinition = "TEXT")
    @Schema(description = "错误信息")
    private String errorMsg;

    @Column(name = "operation_time", updatable = false)
    @Schema(description = "操作时间")
    private LocalDateTime operationTime;

    @PrePersist
    public void prePersist() {
        operationTime = LocalDateTime.now();
        if (status == null) status = 1;
    }
}
