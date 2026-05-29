package com.music.platform.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "copyright_reports")
public class CopyrightReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "copyright_id")
    private Long copyrightId;

    @Column(name = "reporter_id")
    private Long reporterId;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(length = 500)
    private String evidence;

    @Column(name = "status", length = 20)
    private String status = "pending";

    @Column(name = "music_id")
    private Long musicId;
}
