package com.referee.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 比赛实体类
 * 存储比赛项目的详细信息
 *
 * @author Referee System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "competition")
@Schema(description = "比赛实体")
public class Competition {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "比赛ID", example = "1")
    private Long id;

    /**
     * 比赛编号
     */
    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "比赛编号", example = "C001", required = true)
    private String competitionNo;

    /**
     * 比赛名称
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "比赛名称", example = "2024年春季田径运动会", required = true)
    private String name;

    /**
     * 比赛项目
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "比赛项目", example = "100米短跑", required = true)
    private String event;

    /**
     * 比赛地点
     */
    @Column(length = 100)
    @Schema(description = "比赛地点", example = "国家体育场")
    private String location;

    /**
     * 比赛开始时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "比赛开始时间", example = "2024-05-01 09:00:00")
    private Date startTime;

    /**
     * 比赛结束时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "比赛结束时间", example = "2024-05-01 17:00:00")
    private Date endTime;

    /**
     * 裁判长ID
     */
    @Column
    @Schema(description = "裁判长ID", example = "1")
    private Long chiefRefereeId;

    /**
     * 裁判长姓名
     */
    @Column(length = 50)
    @Schema(description = "裁判长姓名", example = "李明")
    private String chiefRefereeName;

    /**
     * 比赛规则说明
     */
    @Column(length = 1000)
    @Schema(description = "比赛规则说明", example = "采用国际田联最新规则")
    private String rules;

    /**
     * 满分
     */
    @Column
    @Schema(description = "满分", example = "100")
    private Double fullScore = 100.0;

    /**
     * 合格分数
     */
    @Column
    @Schema(description = "合格分数", example = "60")
    private Double passScore = 60.0;

    /**
     * 状态：0-未开始, 1-进行中, 2-已结束, 3-已取消
     */
    @Column(nullable = false)
    @Schema(description = "状态：0-未开始, 1-进行中, 2-已结束, 3-已取消", example = "0")
    private Integer status = 0;

    /**
     * 备注
     */
    @Column(length = 500)
    @Schema(description = "备注", example = "年度重要赛事")
    private String remark;
}
