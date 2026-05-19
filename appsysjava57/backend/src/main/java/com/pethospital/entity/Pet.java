package com.pethospital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 宠物实体类
 * 存储宠物的基本信息，支持多宠物管理
 * 
 * @author Pet Hospital Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 50)
    private String species;

    @Column(length = 50)
    private String breed;

    @Column(length = 10)
    private String gender;

    private LocalDate birthDate;

    private Double weight;

    @Column(length = 200)
    private String avatar;

    @Column(length = 500)
    private String description;

    @Column(length = 200)
    private String allergyInfo;

    private Boolean neutered = false;

    @Column(updatable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

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
