package com.agriculture.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 病虫害图片实体类
 * 存储田间采集的病虫害图像，关联到具体的田间记录
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "pest_images")
public class PestImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联田间记录ID
     */
    @Column(nullable = false)
    private Long fieldRecordId;

    /**
     * 图片名称
     */
    @Column(length = 200)
    private String imageName;

    /**
     * 图片存储路径
     */
    @Column(nullable = false, length = 500)
    private String imagePath;

    /**
     * 图片URL（访问地址）
     */
    @Column(length = 500)
    private String imageUrl;

    /**
     * 图片类型：PEST-病虫害，PLANT-植株整体，LEAF-叶片特写，ROOT-根系
     */
    @Column(length = 20)
    private String imageType;

    /**
     * 病虫害类型
     */
    @Column(length = 100)
    private String pestType;

    /**
     * 图片描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 拍摄时间
     */
    private LocalDateTime captureTime;

    /**
     * 拍摄经度
     */
    @Column(precision = 10, scale = 7)
    private Double longitude;

    /**
     * 拍摄纬度
     */
    @Column(precision = 10, scale = 7)
    private Double latitude;

    /**
     * AI识别结果（预留字段）
     */
    @Column(columnDefinition = "TEXT")
    private String aiRecognitionResult;

    /**
     * 上传人ID
     */
    private Long uploadedBy;

    /**
     * 状态：ACTIVE-正常，DELETED-已删除
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
        if (captureTime == null) {
            captureTime = LocalDateTime.now();
        }
    }
}
