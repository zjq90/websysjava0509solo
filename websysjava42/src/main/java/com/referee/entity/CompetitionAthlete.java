package com.referee.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 比赛运动员关联实体类
 * 存储比赛与参赛运动员的关联信息
 *
 * @author Referee System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "competition_athlete")
@Schema(description = "比赛运动员关联实体")
public class CompetitionAthlete {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "关联ID", example = "1")
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
     * 道次/组别
     */
    @Column(length = 20)
    @Schema(description = "道次/组别", example = "第1道")
    private String lane;

    /**
     * 签到状态：0-未签到, 1-已签到
     */
    @Column
    @Schema(description = "签到状态：0-未签到, 1-已签到", example = "0")
    private Integer checkInStatus = 0;

    /**
     * 签到时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "签到时间", example = "2024-05-01 08:30:00")
    private Date checkInTime;

    /**
     * 状态：1-正常, 0-已取消
     */
    @Column(nullable = false)
    @Schema(description = "状态：1-正常, 0-已取消", example = "1")
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;
}
