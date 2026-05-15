package com.teaching.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "course_comment")
public class CourseComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "course_name", length = 100)
    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", length = 50)
    private String userName;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Column
    private Integer status = 1;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
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
