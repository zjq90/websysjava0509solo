package com.music.platform.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "user_id")
    private Long userId;
}
