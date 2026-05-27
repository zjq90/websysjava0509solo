package com.club.entity;

import com.club.enums.ClubStatus;
import com.club.enums.ViolationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 违规记录实体类
 * 存储社团违规处理记录
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "violation_record")
@Schema(description = "违规记录")
public class ViolationRecord extends BaseEntity {

    /**
     * 关联社团ID
     */
    @Column(name = "club_id", nullable = false)
    @Schema(description = "社团ID", example = "1")
    private Long clubId;

    /**
     * 社团名称
     */
    @Column(name = "club_name", length = 100)
    @Schema(description = "社团名称", example = "计算机协会")
    private String clubName;

    /**
     * 违规类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "violation_type", nullable = false, length = 30)
    @Schema(description = "违规类型", example = "INACTIVE")
    private ViolationType violationType;

    /**
     * 违规描述
     */
    @Column(name = "description", nullable = false, length = 1000)
    @Schema(description = "违规描述", example = "连续3个月未开展任何活动")
    private String description;

    /**
     * 处理类型（处理后的社团状态）
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "handle_type", length = 30)
    @Schema(description = "处理类型", example = "WARNING")
    private ClubStatus handleType;

    /**
     * 处理意见
     */
    @Column(name = "handle_opinion", length = 500)
    @Schema(description = "处理意见", example = "限期1个月内整改，恢复正常活动")
    private String handleOpinion;

    /**
     * 处理人ID
     */
    @Column(name = "handler_id")
    @Schema(description = "处理人ID", example = "1")
    private Long handlerId;

    /**
     * 处理人姓名
     */
    @Column(name = "handler_name", length = 50)
    @Schema(description = "处理人姓名", example = "管理员")
    private String handlerName;

    /**
     * 处理时间
     */
    @Column(name = "handle_time")
    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    /**
     * 整改期限
     */
    @Column(name = "rectification_deadline")
    @Schema(description = "整改期限", example = "2024-06-30T23:59:59")
    private LocalDateTime rectificationDeadline;

    /**
     * 是否已整改
     */
    @Column(name = "rectified")
    @Schema(description = "是否已整改", example = "false")
    private Boolean rectified = false;

    /**
     * 整改说明
     */
    @Column(name = "rectification_note", length = 1000)
    @Schema(description = "整改说明")
    private String rectificationNote;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;
}
