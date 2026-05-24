package com.bikesystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@TableName("user")
@Schema(description = "用户信息")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "性别:0-未知,1-男,2-女")
    private Integer gender;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "身份证验证状态:0-未验证,1-已验证")
    private Integer idCardVerified;

    @Schema(description = "人脸识别状态:0-未验证,1-已验证")
    private Integer faceVerified;

    @Schema(description = "实名认证状态:0-未认证,1-已认证")
    private Integer realNameVerified;

    @Schema(description = "微信OpenID")
    private String wechatOpenid;

    @Schema(description = "支付宝OpenID")
    private String alipayOpenid;

    @Schema(description = "押金状态:0-未缴纳,1-已缴纳,2-免押金")
    private Integer depositStatus;

    @Schema(description = "押金金额")
    private BigDecimal depositAmount;

    @Schema(description = "账户余额")
    private BigDecimal balance;

    @Schema(description = "信用分")
    private Integer creditScore;

    @Schema(description = "信用等级:EXCELLENT-优秀,NORMAL-正常,WARNING-警告,RESTRICTED-限制")
    private String creditLevel;

    @Schema(description = "状态:0-禁用,1-正常")
    private Integer status;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer deleted;
}
