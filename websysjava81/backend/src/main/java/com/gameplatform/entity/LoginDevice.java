package com.gameplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("login_device")
public class LoginDevice {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private String deviceType;
    private String deviceModel;
    private String osVersion;
    private String ipAddress;
    private LocalDateTime loginTime;
}
