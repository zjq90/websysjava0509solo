package com.gameplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("report")
public class Report {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String reportId;
    private String reportType;
    private String targetId;
    private String reasonType;
    private String reasonDetail;
    private String reporterId;
    private Integer status;
    private LocalDateTime handleTime;
    private String handlerId;
    private String handlerName;
    private LocalDateTime createTime;
}
