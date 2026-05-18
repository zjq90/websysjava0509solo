package com.secondhand.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_operation_log")
@Schema(description = "操作日志实体")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "操作人ID")
    @Column(nullable = false)
    private Long operatorId;

    @Schema(description = "操作人名称")
    @Column(nullable = false, length = 50)
    private String operatorName;

    @Schema(description = "操作模块")
    @Column(nullable = false, length = 50)
    private String module;

    @Schema(description = "操作类型: CREATE-新增, UPDATE-修改, DELETE-删除, QUERY-查询, EXPORT-导出, IMPORT-导入")
    @Column(nullable = false, length = 30)
    private String type;

    @Schema(description = "操作描述")
    @Column(nullable = false, length = 500)
    private String description;

    @Schema(description = "请求方法")
    @Column(length = 10)
    private String method;

    @Schema(description = "请求参数")
    @Column(columnDefinition = "TEXT")
    private String params;

    @Schema(description = "执行结果: SUCCESS-成功, FAILURE-失败")
    @Column(nullable = false, length = 20)
    private String result;

    @Schema(description = "错误信息")
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Schema(description = "IP地址")
    @Column(length = 50)
    private String ipAddress;

    @Schema(description = "操作时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime operationTime;

    @PrePersist
    protected void onCreate() {
        operationTime = LocalDateTime.now();
    }

}