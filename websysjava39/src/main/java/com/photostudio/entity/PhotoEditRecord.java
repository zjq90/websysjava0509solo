package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 修图记录实体类
 * 用于统计修图师的工作量和返修率，用于绩效考核
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_photo_edit_record")
@Schema(description = "修图记录")
public class PhotoEditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Schema(description = "关联订单")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_id", nullable = false)
    @Schema(description = "修图师")
    private Employee editor;

    @Column(name = "photo_count", nullable = false)
    @Schema(description = "修图数量")
    private Integer photoCount;

    @Column(name = "rework_count")
    @Schema(description = "返修次数")
    private Integer reworkCount = 0;

    @Column(name = "is_reworked")
    @Schema(description = "是否返修")
    private Boolean reworked = false;

    @Column(name = "start_time")
    @Schema(description = "开始修图时间")
    private LocalDateTime startTime;

    @Column(name = "complete_time")
    @Schema(description = "完成修图时间")
    private LocalDateTime completeTime;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
