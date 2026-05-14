package com.broadband.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 工单实体类
 * 管理各类业务工单（新装、移机、销户等）
 * 
 * @author broadband
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "biz_work_order")
@Schema(description = "工单信息")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "工单ID")
    private Long id;

    @Schema(description = "工单编号")
    @Column(length = 50, nullable = false, unique = true)
    private String orderNo;

    @Schema(description = "用户ID")
    @Column(nullable = false)
    private Long userId;

    @Schema(description = "工单类型: 1-新装办理 2-套餐变更 3-移机 4-销户 5-续费")
    @Column(nullable = false)
    private Integer type;

    @Schema(description = "工单标题")
    @Column(length = 200)
    private String title;

    @Schema(description = "工单描述")
    @Column(length = 2000)
    private String description;

    @Schema(description = "关联套餐ID")
    private Long packageId;

    @Schema(description = "宽带号码")
    @Column(length = 20)
    private String broadbandNumber;

    @Schema(description = "原安装地址")
    @Column(length = 512)
    private String oldAddress;

    @Schema(description = "新安装地址")
    @Column(length = 512)
    private String newAddress;

    @Schema(description = "地址证明材料路径,逗号分隔")
    @Column(length = 2000)
    private String addressProofs;

    @Schema(description = "预约时间")
    private LocalDateTime appointmentTime;

    @Schema(description = "电子合同路径")
    @Column(length = 512)
    private String contractPath;

    @Schema(description = "签署状态: 0-未签署 1-已签署")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer signStatus = 0;

    @Schema(description = "签署时间")
    private LocalDateTime signTime;

    @Schema(description = "工单状态: 0-待审核 1-待处理 2-处理中 3-已完成 4-已取消 5-已驳回")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer status = 0;

    @Schema(description = "处理人员")
    @Column(length = 50)
    private String handler;

    @Schema(description = "处理人员电话")
    @Column(length = 20)
    private String handlerPhone;

    @Schema(description = "处理备注")
    @Column(length = 2000)
    private String handleRemark;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

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
