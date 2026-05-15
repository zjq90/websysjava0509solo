package com.referee.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 评分实体类
 * 存储裁判对运动员的评分信息
 *
 * @author Referee System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "score")
@Schema(description = "评分实体")
public class Score {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "评分ID", example = "1")
    private Long id;

    /**
     * 比赛ID
     */
    @Column(nullable = false)
    @Schema(description = "比赛ID", example = "1", required = true)
    private Long competitionId;

    /**
     * 比赛编号
     */
    @Column(length = 50)
    @Schema(description = "比赛编号", example = "C001")
    private String competitionNo;

    /**
     * 比赛名称
     */
    @Column(length = 100)
    @Schema(description = "比赛名称", example = "2024年春季田径运动会")
    private String competitionName;

    /**
     * 运动员ID
     */
    @Column(nullable = false)
    @Schema(description = "运动员ID", example = "1", required = true)
    private Long athleteId;

    /**
     * 运动员编号
     */
    @Column(length = 50)
    @Schema(description = "运动员编号", example = "A001")
    private String athleteNo;

    /**
     * 运动员姓名
     */
    @Column(length = 50)
    @Schema(description = "运动员姓名", example = "王强")
    private String athleteName;

    /**
     * 裁判ID
     */
    @Column(nullable = false)
    @Schema(description = "裁判ID", example = "1", required = true)
    private Long refereeId;

    /**
     * 裁判编号
     */
    @Column(length = 50)
    @Schema(description = "裁判编号", example = "R001")
    private String refereeNo;

    /**
     * 裁判姓名
     */
    @Column(length = 50)
    @Schema(description = "裁判姓名", example = "李明")
    private String refereeName;

    /**
     * 技术分
     */
    @Column
    @Schema(description = "技术分", example = "45.5")
    private Double technicalScore;

    /**
     * 表现分
     */
    @Column
    @Schema(description = "表现分", example = "42.0")
    private Double performanceScore;

    /**
     * 总分
     */
    @Column
    @Schema(description = "总分", example = "87.5")
    private Double totalScore;

    /**
     * 评分评语
     */
    @Column(length = 1000)
    @Schema(description = "评分评语", example = "动作标准，表现出色")
    private String comment;

    /**
     * 评分时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "评分时间", example = "2024-05-01 10:30:00")
    private Date scoreTime;

    /**
     * 审核状态：0-待审核, 1-已审核通过, 2-已驳回
     */
    @Column(nullable = false)
    @Schema(description = "审核状态：0-待审核, 1-已审核通过, 2-已驳回", example = "0")
    private Integer auditStatus = 0;

    /**
     * 审核人ID
     */
    @Column
    @Schema(description = "审核人ID", example = "1")
    private Long auditorId;

    /**
     * 审核人姓名
     */
    @Column(length = 50)
    @Schema(description = "审核人姓名", example = "张裁判长")
    private String auditorName;

    /**
     * 审核时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "审核时间", example = "2024-05-01 11:00:00")
    private Date auditTime;

    /**
     * 审核意见
     */
    @Column(length = 500)
    @Schema(description = "审核意见", example = "评分合理，同意通过")
    private String auditComment;

    /**
     * 状态：1-有效, 0-无效
     */
    @Column(nullable = false)
    @Schema(description = "状态：1-有效, 0-无效", example = "1")
    private Integer status = 1;
}
