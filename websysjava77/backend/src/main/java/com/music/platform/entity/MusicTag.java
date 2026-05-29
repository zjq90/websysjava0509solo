package com.music.platform.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "music_tags")
public class MusicTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "music_id")
    private Long musicId;

    @Column(name = "tag_id")
    private Long tagId;
}
