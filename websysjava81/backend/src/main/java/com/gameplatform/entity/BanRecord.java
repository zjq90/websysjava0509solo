package com.gameplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("ban_record")
public class BanRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private Integer banType;
    private String banReason;
    private LocalDateTime unbanTime;
    private String operatorId;
    private String operatorName;
    private Integer status;
    private LocalDateTime createTime;
}
