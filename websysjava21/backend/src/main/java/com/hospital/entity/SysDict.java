package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 系统字典实体类
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_dict")
@Schema(description = "系统字典")
public class SysDict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "字典ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "字典类型", example = "DEPARTMENT")
    private String dictType;

    @Column(nullable = false, length = 100)
    @Schema(description = "字典类型名称", example = "科室字典")
    private String dictTypeName;

    @Column(nullable = false, length = 50)
    @Schema(description = "字典编码", example = "001")
    private String dictCode;

    @Column(nullable = false, length = 100)
    @Schema(description = "字典名称", example = "内科")
    private String dictName;

    @Column
    @Schema(description = "字典值", example = "1")
    private String dictValue;

    @Column
    @Schema(description = "父字典ID", example = "0")
    private Long parentId;

    @Column
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;

    @Column(length = 200)
    @Schema(description = "备注")
    private String remark;

    @Column(nullable = false)
    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status = 1;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
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
