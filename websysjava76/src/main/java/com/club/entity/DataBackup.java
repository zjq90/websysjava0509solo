package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 数据备份实体类
 * 存储数据备份记录
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "data_backup")
@Schema(description = "数据备份")
public class DataBackup extends BaseEntity {

    /**
     * 备份名称
     */
    @Column(name = "name", nullable = false, length = 200)
    @Schema(description = "备份名称", example = "20240527_全校社团数据备份")
    private String name;

    /**
     * 备份类型（手动/自动/定时）
     */
    @Column(name = "type", nullable = false, length = 30)
    @Schema(description = "备份类型", example = "MANUAL")
    private String type;

    /**
     * 备份文件路径
     */
    @Column(name = "file_path", length = 500)
    @Schema(description = "备份文件路径", example = "/backup/20240527_backup.sql")
    private String filePath;

    /**
     * 备份文件大小（字节）
     */
    @Column(name = "file_size")
    @Schema(description = "文件大小（字节）", example = "1048576")
    private Long fileSize;

    /**
     * 备份状态（0-进行中，1-成功，2-失败）
     */
    @Column(name = "status", nullable = false)
    @Schema(description = "备份状态", example = "1")
    private Integer status = 1;

    /**
     * 备份开始时间
     */
    @Column(name = "start_time")
    @Schema(description = "备份开始时间")
    private LocalDateTime startTime;

    /**
     * 备份完成时间
     */
    @Column(name = "end_time")
    @Schema(description = "备份完成时间")
    private LocalDateTime endTime;

    /**
     * 操作人ID
     */
    @Column(name = "operator_id")
    @Schema(description = "操作人ID", example = "1")
    private Long operatorId;

    /**
     * 操作人姓名
     */
    @Column(name = "operator_name", length = 50)
    @Schema(description = "操作人姓名", example = "管理员")
    private String operatorName;

    /**
     * 备份描述
     */
    @Column(name = "description", length = 500)
    @Schema(description = "备份描述", example = "每日定时备份")
    private String description;

    /**
     * 错误信息（备份失败时）
     */
    @Column(name = "error_message", length = 1000)
    @Schema(description = "错误信息")
    private String errorMessage;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;
}
