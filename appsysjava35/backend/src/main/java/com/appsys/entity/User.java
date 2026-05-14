package com.appsys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_user")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "用户实体")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "用户名")
    private String username;

    @Column(nullable = false)
    @Schema(description = "密码")
    private String password;

    @Column(length = 20)
    @Schema(description = "手机号（加密存储）")
    private String phone;

    @Column(length = 50)
    @Schema(description = "邮箱")
    private String email;

    @Column(length = 50)
    @Schema(description = "真实姓名")
    private String realName;

    @Column(length = 18)
    @Schema(description = "身份证号（加密存储）")
    private String idCard;

    @Column(length = 255)
    @Schema(description = "头像URL")
    private String avatar;

    @Column(name = "member_level_id")
    @Schema(description = "会员等级ID")
    private Long memberLevelId;

    @Column(nullable = false)
    @Schema(description = "当前积分")
    private Integer points = 0;

    @Column(nullable = false)
    @Schema(description = "成长值")
    private Integer growth = 0;

    @Column(length = 20)
    @Schema(description = "专属客户经理")
    private String accountManager;

    @Column(nullable = false)
    @Schema(description = "是否开启长辈模式")
    private Boolean elderMode = false;

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

    @Transient
    @Schema(description = "会员等级信息")
    private MemberLevel memberLevel;
}
