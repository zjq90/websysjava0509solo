package com.appsys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "point_record")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "积分记录实体")
public class PointRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(nullable = false)
    @Schema(description = "变更积分（正数增加，负数减少）")
    private Integer points;

    @Column(nullable = false)
    @Schema(description = "变更类型：1-办理业务，2-签到，3-评价服务，4-兑换礼品，5-系统调整")
    private Integer type;

    @Column(length = 200)
    @Schema(description = "变更描述")
    private String description;

    @Column(length = 100)
    @Schema(description = "关联业务ID")
    private String bizId;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
