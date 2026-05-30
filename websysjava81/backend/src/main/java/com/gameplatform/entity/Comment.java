package com.gameplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String commentId;
    private String gameId;
    private String userId;
    private String content;
    private Integer auditStatus;
    private Integer hasSensitiveWord;
    private LocalDateTime createTime;
    private LocalDateTime auditTime;
    private String auditorId;
    @TableLogic
    private Integer deleted;
}
