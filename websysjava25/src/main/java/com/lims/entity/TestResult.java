package com.lims.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 检验检查结果实体类
 * 存储技师录入的检验/检查结果信息
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_result")
@Schema(description = "检验检查结果")
public class TestResult {

    /**
     * 结果ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "结果ID", example = "1")
    private Long id;

    /**
     * 关联申请ID
     */
    @Column(nullable = false)
    @Schema(description = "申请ID", example = "1")
    private Long applicationId;

    /**
     * 项目ID
     */
    @Column(nullable = false)
    @Schema(description = "项目ID", example = "1")
    private Long itemId;

    /**
     * 执行技师ID
     */
    @Column(nullable = false)
    @Schema(description = "执行技师ID", example = "2")
    private Long technicianId;

    /**
     * 检验/检查结果值
     */
    @Column(length = 200)
    @Schema(description = "结果值", example = "6.5")
    private String resultValue;

    /**
     * 结果异常标识：NORMAL-正常，HIGH-偏高，LOW-偏低
     */
    @Column(length = 20)
    @Schema(description = "异常标识", example = "NORMAL")
    private String abnormalFlag;

    /**
     * 检验/检查描述
     */
    @Column(length = 2000)
    @Schema(description = "检查描述", example = "各指标正常")
    private String resultDescription;

    /**
     * 影像资料路径（对于检查项目）
     */
    @Column(length = 500)
    @Schema(description = "影像路径", example = "/images/2024/01/01/xxx.jpg")
    private String imagePath;

    /**
     * 设备编号（设备接口对接时记录）
     */
    @Column(length = 50)
    @Schema(description = "设备编号", example = "DEV001")
    private String deviceCode;

    /**
     * 结果来源：MANUAL-人工录入，DEVICE-设备自动采集
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "结果来源", example = "MANUAL")
    private String resultSource;

    /**
     * 操作记录备注
     */
    @Column(length = 500)
    @Schema(description = "操作备注", example = "样本合格")
    private String operationNotes;

    /**
     * 开始处理时间
     */
    @Column
    @Schema(description = "开始处理时间")
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    @Column
    @Schema(description = "完成时间")
    private LocalDateTime completeTime;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        resultSource = "MANUAL";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
