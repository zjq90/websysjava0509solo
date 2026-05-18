package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 会员等级实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member_level")
@Schema(description = "会员等级")
public class MemberLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "等级ID")
    private Long id;

    @Column(length = 50)
    @Schema(description = "等级名称：普通会员/VIP/高级VIP")
    private String name;

    @Schema(description = "等级图标URL")
    private String icon;

    @Schema(description = "升级所需累计消费金额（分）")
    private Long upgradeAmount;

    @Schema(description = "折扣率：85表示85折，100表示不打折")
    private Integer discountRate;

    @Schema(description = "是否免运费：0-否，1-是")
    private Integer freeShipping;

    @Schema(description = "是否优先抢购：0-否，1-是")
    private Integer priorityPurchase;

    @Schema(description = "生日折扣率：85表示85折")
    private Integer birthdayDiscount;

    @Schema(description = "积分加速倍数：100表示1倍，150表示1.5倍")
    private Integer pointAccelerator;

    @Column(length = 500)
    @Schema(description = "等级说明")
    private String description;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (status == null) status = 1;
        if (sortOrder == null) sortOrder = 0;
    }
}
