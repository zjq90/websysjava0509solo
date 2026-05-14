package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 照片实体类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "photo")
@Schema(description = "照片信息")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "照片ID", example = "1")
    private Long id;

    @Column(name = "album_id", nullable = false)
    @Schema(description = "相册ID", example = "1")
    private Long albumId;

    @Column(name = "file_name", length = 255)
    @Schema(description = "文件名")
    private String fileName;

    @Column(name = "original_url", nullable = false, length = 500)
    @Schema(description = "原图URL")
    private String originalUrl;

    @Column(name = "thumbnail_url", length = 500)
    @Schema(description = "缩略图URL")
    private String thumbnailUrl;

    @Column(name = "retouched_url", length = 500)
    @Schema(description = "精修图URL")
    private String retouchedUrl;

    @Column(name = "file_size")
    @Schema(description = "文件大小(字节)", example = "5242880")
    private Long fileSize;

    @Column(name = "width")
    @Schema(description = "宽度(像素)", example = "4000")
    private Integer width;

    @Column(name = "height")
    @Schema(description = "高度(像素)", example = "3000")
    private Integer height;

    @Column(name = "is_selected")
    @Schema(description = "是否选中: 0-未选 1-已选", example = "0")
    private Integer isSelected;

    @Column(name = "mark_type", length = 20)
    @Schema(description = "标记类型: like-喜欢 retouch-待修 delete-删除 null-无标记", example = "like")
    private String markType;

    @Column(name = "retouch_status")
    @Schema(description = "修图状态: 0-未修 1-修图中 2-初修完成 3-精修完成", example = "0")
    private Integer retouchStatus;

    @Column(name = "retoucher_id")
    @Schema(description = "修图师ID", example = "1")
    private Long retoucherId;

    @Column(name = "sort_order")
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @Column(name = "remark", length = 500)
    @Schema(description = "备注/修图要求")
    private String remark;

    @Column(name = "retouch_remark", length = 500)
    @Schema(description = "修图备注")
    private String retouchRemark;

    @Column(name = "retouch_start_time")
    @Schema(description = "修图开始时间")
    private LocalDateTime retouchStartTime;

    @Column(name = "retouch_end_time")
    @Schema(description = "修图完成时间")
    private LocalDateTime retouchEndTime;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (isSelected == null) {
            isSelected = 0;
        }
        if (retouchStatus == null) {
            retouchStatus = 0;
        }
        if (sortOrder == null) {
            sortOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
