package com.breeding.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 育种项目实体类
 * 用于管理育种项目的基本信息，包括项目名称、负责人、开始/结束日期等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "breeding_projects")
public class BreedingProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 项目编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String projectCode;

    /**
     * 项目名称
     */
    @Column(nullable = false, length = 200)
    private String projectName;

    /**
     * 作物类型
     */
    @Column(nullable = false, length = 100)
    private String cropType;

    /**
     * 育种目标
     */
    @Column(length = 1000)
    private String breedingGoal;

    /**
     * 负责人
     */
    @Column(nullable = false, length = 100)
    private String responsiblePerson;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String contactPhone;

    /**
     * 项目开始日期
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * 项目结束日期
     */
    private LocalDate endDate;

    /**
     * 项目状态：进行中、已完成、已暂停、已取消
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 项目描述
     */
    @Column(length = 2000)
    private String description;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDate createDate;

    /**
     * 更新时间
     */
    private LocalDate updateDate;

    /**
     * 与亲本记录的关联关系
     * 一个项目可以有多个亲本
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParentPlant> parents = new ArrayList<>();

    /**
     * 与杂交组合的关联关系
     * 一个项目可以有多个杂交组合
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrossCombination> crossCombinations = new ArrayList<>();

    /**
     * 与田间试验的关联关系
     * 一个项目可以有多个田间试验
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FieldExperiment> experiments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDate.now();
    }
}
