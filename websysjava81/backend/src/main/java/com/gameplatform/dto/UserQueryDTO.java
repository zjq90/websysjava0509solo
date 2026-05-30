package com.gameplatform.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserQueryDTO {
    private String keyword;
    private Integer memberStatus;
    private Integer userStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
