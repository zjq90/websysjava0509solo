package com.gamesys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {
    private String username;
    private String nickname;
    private String email;
    private String avatar;
    private Integer status;
    private String role;
    private LocalDateTime lastLoginTime;
}
