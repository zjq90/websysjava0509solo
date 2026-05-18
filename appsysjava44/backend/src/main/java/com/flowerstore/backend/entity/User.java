package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 存储用户基本信息
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
@Schema(description = "用户信息")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID")
    private Long id;

    @Column(unique = true, length = 50)
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码（加密存储）")
    private String password;

    @Column(unique = true, length = 20)
    @Schema(description = "手机号")
    private String phone;

    @Column(unique = true, length = 100)
    @Schema(description = "邮箱")
    private String email;

    @Column(length = 100)
    @Schema(description = "微信OpenID")
    private String wxOpenid;

    @Column(length = 100)
    @Schema(description = "微信UnionID")
    private String wxUnionid;

    @Column(length = 200)
    @Schema(description = "头像URL")
    private String avatar;

    @Column(length = 50)
    @Schema(description = "昵称")
    private String nickname;

    @Column(length = 20)
    @Schema(description = "真实姓名")
    private String realName;

    @Column(length = 2)
    @Schema(description = "性别：0-未知，1-男，2-女")
    private Integer gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "生日")
    private LocalDate birthday;

    @Column(length = 18)
    @Schema(description = "身份证号（加密）")
    private String idCard;

    @Schema(description = "会员等级ID")
    private Long memberLevelId;

    @Schema(description = "当前积分")
    private Integer currentPoints;

    @Schema(description = "累计消费金额（分）")
    private Long totalConsume;

    @Schema(description = "是否长辈模式：0-否，1-是")
    private Integer elderMode;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) status = 1;
        if (currentPoints == null) currentPoints = 0;
        if (totalConsume == null) totalConsume = 0L;
        if (elderMode == null) elderMode = 0;
        if (gender == null) gender = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
