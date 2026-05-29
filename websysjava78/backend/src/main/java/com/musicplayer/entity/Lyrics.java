package com.musicplayer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lyrics")
public class Lyrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "music_id")
    private Music music;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String lrcContent;

    @Column(name = "has_sync")
    private Boolean hasSync = false;
}
