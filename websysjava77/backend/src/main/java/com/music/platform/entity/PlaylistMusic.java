package com.music.platform.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "playlist_music")
public class PlaylistMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "music_id")
    private Long musicId;

    private Integer position;
}
