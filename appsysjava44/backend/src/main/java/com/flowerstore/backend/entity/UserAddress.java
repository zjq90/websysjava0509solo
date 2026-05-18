package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户收货地址实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_address")
@Schema(description = "用户收货地址")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "地址ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Column(length = 20)
    @Schema(description = "收货人姓名")
    private String receiverName;

    @Column(length = 20)
    @Schema(description = "收货人电话")
    private String receiverPhone;

    @Column(length = 100)
    @Schema(description = "省")
    private String province;

    @Column(length = 100)
    @Schema(description = "市")
    private String city;

    @Column(length = 100)
    @Schema(description = "区/县")
    private String district;

    @Column(length = 500)
    @Schema(description = "详细地址")
    private String detailAddress;

    @Column(length = 10)
    @Schema(description = "邮政编码")
    private String zipCode;

    @Column(length = 200)
    @Schema(description = "地址标签（家、公司等）")
    private String tag;

    @Schema(description = "是否默认地址：0-否，1-是")
    private Integer isDefault;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (isDefault == null) isDefault = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
