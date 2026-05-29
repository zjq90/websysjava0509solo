package com.musicplatform.repository;

import com.musicplatform.entity.ArtistStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ArtistStatsRepository extends JpaRepository<ArtistStats, Long> {
    List<ArtistStats> findByArtist_IdAndStatsDateBetween(Long artistId, LocalDate startDate, LocalDate endDate);
    ArtistStats findByArtist_IdAndStatsDate(Long artistId, LocalDate statsDate);
}
