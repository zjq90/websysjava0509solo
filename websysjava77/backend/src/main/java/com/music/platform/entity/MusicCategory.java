package com.music.platform.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "music_categories")
public class MusicCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "music_id")
    private Long musicId;

    @Column(name = "category_id")
    private Long categoryId;
}
