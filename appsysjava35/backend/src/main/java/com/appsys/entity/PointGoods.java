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
@Table(name = "point_goods")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "积分商品实体")
public class PointGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "商品ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "商品名称")
    private String name;

    @Column(length = 500)
    @Schema(description = "商品描述")
    private String description;

    @Column(length = 255)
    @Schema(description = "商品图片")
    private String image;

    @Column(nullable = false)
    @Schema(description = "所需积分")
    private Integer points;

    @Column(nullable = false)
    @Schema(description = "商品类型：1-话费，2-视频会员，3-实物礼品")
    private Integer type;

    @Column(length = 100)
    @Schema(description = "商品价值")
    private String value;

    @Column(nullable = false)
    @Schema(description = "库存数量")
    private Integer stock = 0;

    @Column(nullable = false)
    @Schema(description = "已兑换数量")
    private Integer exchanged = 0;

    @Column(nullable = false)
    @Schema(description = "所需最低会员等级")
    private Integer minMemberLevel = 1;

    @Column(nullable = false)
    @Schema(description = "排序")
    private Integer sort = 0;

    @Column(nullable = false)
    @Schema(description = "状态：0-下架，1-上架")
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
