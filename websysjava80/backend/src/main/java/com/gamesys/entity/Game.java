package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_info")
public class Game extends BaseEntity {
    private String gameId;
    private String name;
    private String cover;
    private String type;
    private Long categoryId;
    private String developer;
    private String description;
    private String playUrl;
    private String tags;

    private Integer status;
    private Integer hotValue;
    private Integer playCount;
    private Integer recommend;
    private LocalDateTime submitTime;
    private LocalDateTime onlineTime;
    private LocalDateTime offlineTime;
}
