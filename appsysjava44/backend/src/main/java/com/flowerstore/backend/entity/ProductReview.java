package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 产品评价实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_review")
@Schema(description = "产品评价")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "评价ID")
    private Long id;

    @Schema(description = "产品ID")
    private Long productId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "评分：1-5星")
    private Integer rating;

    @Column(length = 1000)
    @Schema(description = "评价内容")
    private String content;

    @Column(length = 1000)
    @Schema(description = "评价图片URL，多个用逗号分隔")
    private String images;

    @Schema(description = "是否匿名：0-否，1-是")
    private Integer isAnonymous;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "状态：0-待审核，1-已显示，2-已删除")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (status == null) status = 1;
        if (likeCount == null) likeCount = 0;
        if (isAnonymous == null) isAnonymous = 0;
    }
}
