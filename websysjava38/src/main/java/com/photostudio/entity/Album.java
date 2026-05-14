package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 云相册实体类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "album")
@Schema(description = "云相册信息")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "相册ID", example = "1")
    private Long id;

    @Column(name = "album_no", nullable = false, length = 50, unique = true)
    @Schema(description = "相册编号", example = "ALB202401010001")
    private String albumNo;

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "相册名称", example = "张三&李四婚纱照")
    private String name;

    @Column(name = "order_id")
    @Schema(description = "关联订单ID", example = "1")
    private Long orderId;

    @Column(name = "customer_id")
    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    @Column(name = "total_photos")
    @Schema(description = "照片总数", example = "100")
    private Integer totalPhotos;

    @Column(name = "selected_count")
    @Schema(description = "已选照片数量", example = "40")
    private Integer selectedCount;

    @Column(name = "retouched_count")
    @Schema(description = "已精修照片数量", example = "20")
    private Integer retouchedCount;

    @Column(name = "cover_image", length = 255)
    @Schema(description = "封面图片")
    private String coverImage;

    @Column(name = "access_code", length = 20)
    @Schema(description = "访问密码", example = "123456")
    private String accessCode;

    @Column(name = "share_link", length = 255)
    @Schema(description = "分享链接")
    private String shareLink;

    @Column(name = "is_encrypted")
    @Schema(description = "是否加密: 0-否 1-是", example = "1")
    private Integer isEncrypted;

    @Column(name = "status")
    @Schema(description = "状态: 0-未上传 1-上传中 2-已上传 3-选片中 4-选片完成 5-精修中 6-精修完成", example = "2")
    private Integer status;

    @Column(name = "selection_deadline")
    @Schema(description = "选片截止日期")
    private LocalDateTime selectionDeadline;

    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;

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
        if (totalPhotos == null) {
            totalPhotos = 0;
        }
        if (selectedCount == null) {
            selectedCount = 0;
        }
        if (retouchedCount == null) {
            retouchedCount = 0;
        }
        if (isEncrypted == null) {
            isEncrypted = 1;
        }
        if (status == null) {
            status = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
