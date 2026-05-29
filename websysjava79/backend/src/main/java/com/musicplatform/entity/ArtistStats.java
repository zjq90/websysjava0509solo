package com.musicplatform.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "artist_stats")
public class ArtistStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private User artist;

    private LocalDate statsDate;

    private int newFans = 0;
    private int totalFans = 0;

    private int totalPlays = 0;
    private int totalDownloads = 0;

    private double totalRevenue = 0.0;
    private double dailyRevenue = 0.0;

    private int musicCount = 0;
}
