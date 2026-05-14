package com.photostudio.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签实体类
 * 用于客户分类和营销分组
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "客户标签")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "标签ID")
    private Long id;

    @NotBlank(message = "标签名称不能为空")
    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "标签名称", example = "森系风格爱好者")
    private String name;

    @Column(length = 200)
    @Schema(description = "标签描述", example = "偏好森林、自然环境拍摄风格的客户")
    private String description;

    @Column(nullable = false)
    @Schema(description = "是否自动标签")
    private Boolean autoTag = false;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Column(nullable = false)
    @Schema(description = "是否删除")
    private Boolean deleted = false;

    @ManyToMany(mappedBy = "tags")
    private List<Customer> customers = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
