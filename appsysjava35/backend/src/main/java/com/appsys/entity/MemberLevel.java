package com.appsys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "member_level")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "会员等级实体")
public class MemberLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "等级ID")
    private Long id;

    @Column(nullable = false, length = 20)
    @Schema(description = "等级名称")
    private String name;

    @Column(nullable = false, unique = true)
    @Schema(description = "等级级别：1-普通，2-银卡，3-金卡，4-钻石")
    private Integer level;

    @Column(nullable = false)
    @Schema(description = "所需成长值")
    private Integer requiredGrowth;

    @Column(nullable = false)
    @Schema(description = "积分倍率")
    private BigDecimal pointMultiplier = new BigDecimal("1.0");

    @Column(nullable = false)
    @Schema(description = "是否享有优先客服")
    private Boolean priorityService = false;

    @Column(nullable = false)
    @Schema(description = "是否享有专属客户经理")
    private Boolean hasAccountManager = false;

    @Column(length = 255)
    @Schema(description = "等级图标")
    private String icon;

    @Column(length = 500)
    @Schema(description = "等级权益描述")
    private String benefits;

    @Column(nullable = false)
    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status = 1;

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
