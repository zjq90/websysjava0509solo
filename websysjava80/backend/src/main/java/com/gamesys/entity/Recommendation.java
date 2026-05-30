package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_recommendation")
public class Recommendation extends BaseEntity {
    private Long gameId;
    private String gameName;
    private String type;
    private Long categoryId;
    private Integer sortWeight;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer displayFrequency;
    private Integer status;
}
