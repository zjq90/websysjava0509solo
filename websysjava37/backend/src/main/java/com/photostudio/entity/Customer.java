package com.photostudio.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户实体类
 * 用于存储客户的基本信息
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "客户信息")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "客户ID")
    private Long id;

    @NotBlank(message = "客户姓名不能为空")
    @Column(nullable = false, length = 50)
    @Schema(description = "客户姓名", example = "张三")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(nullable = false, unique = true, length = 20)
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "生日")
    private LocalDate birthday;

    @Column(length = 200)
    @Schema(description = "家庭成员信息", example = "配偶：李四，孩子：张小宝")
    private String familyMembers;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Schema(description = "拍摄类型")
    private PhotoType photoType;

    @Column(length = 500)
    @Schema(description = "消费偏好", example = "喜欢森系风格，偏好外景拍摄")
    private String preference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Schema(description = "客户生命周期阶段")
    private CustomerLifecycle lifecycle = CustomerLifecycle.POTENTIAL;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "customer_tags",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    /**
     * 拍摄类型枚举
     */
    public enum PhotoType {
        WEDDING("婚纱摄影"),
        CHILDREN("儿童摄影"),
        MATERNITY("孕照摄影"),
        FAMILY("全家福"),
        PERSONAL("个人写真"),
        OTHER("其他");

        private final String description;

        PhotoType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 客户生命周期阶段枚举
     */
    public enum CustomerLifecycle {
        POTENTIAL("潜在客户"),
        INTENTION("意向客户"),
        ORDERED("已定单"),
        PHOTOGRAPHED("已拍摄"),
        DELIVERED("已交付"),
        SLEEPING("沉睡客户");

        private final String description;

        CustomerLifecycle(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
