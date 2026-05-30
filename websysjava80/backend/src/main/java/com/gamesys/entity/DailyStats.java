package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("daily_stats")
public class DailyStats extends BaseEntity {
    private LocalDate statDate;
    private Integer onlineUsers;
    private Integer newUsers;
    private Integer gameLaunches;
    private BigDecimal adRevenue;
    private Integer activeUsers;
}
