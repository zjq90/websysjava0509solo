package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 短信验证码实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sms_code")
@Schema(description = "短信验证码")
public class SmsCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(length = 20)
    @Schema(description = "手机号")
    private String phone;

    @Column(length = 10)
    @Schema(description = "验证码")
    private String code;

    @Column(length = 20)
    @Schema(description = "类型：register-注册，login-登录，resetpwd-重置密码")
    private String type;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "是否已使用：0-否，1-是")
    private Integer isUsed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (isUsed == null) isUsed = 0;
    }
}
