package com.gamesys.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Integer status;
    private String type;
    private Long categoryId;
    private String developer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
