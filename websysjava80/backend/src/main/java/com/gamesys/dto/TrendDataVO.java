package com.gamesys.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrendDataVO {
    private List<String> dates;
    private List<Integer> activeUsers;
    private List<Integer> gameLaunches;
}
