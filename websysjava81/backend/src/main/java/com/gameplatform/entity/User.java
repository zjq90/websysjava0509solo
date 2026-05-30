package com.gameplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("`user`")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private String nickname;
    private String phone;
    private String avatar;
    private String email;
    private Integer memberStatus;
    private Integer userStatus;
    private Integer totalGameTime;
    private LocalDateTime registerTime;
    private LocalDateTime lastLoginTime;
    @TableLogic
    private Integer deleted;
}
