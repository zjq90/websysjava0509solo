package com.gameplatform.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BanUserDTO {
    private String userId;
    private Integer banType;
    private String banReason;
    private LocalDateTime unbanTime;
    private String operatorId;
    private String operatorName;
}
