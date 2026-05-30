package com.gameplatform.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private String userId;
    private String nickname;
    private String phone;
    private Integer memberStatus;
    private Integer userStatus;
    private Integer totalGameTime;
    private LocalDateTime registerTime;
    private LocalDateTime lastLoginTime;
}
