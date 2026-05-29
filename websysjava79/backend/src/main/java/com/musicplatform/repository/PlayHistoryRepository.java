package com.musicplatform.repository;

import com.musicplatform.entity.PlayHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
    Page<PlayHistory> findByUser_IdOrderByPlayedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT ph.music.id, COUNT(ph) as playCount FROM PlayHistory ph " +
           "WHERE ph.music.artist.id = :artistId AND ph.playedAt >= :startTime " +
           "GROUP BY ph.music.id ORDER BY playCount DESC")
    List<Object[]> countPlaysByArtistId(@Param("artistId") Long artistId, 
                                         @Param("startTime") LocalDateTime startTime);

    @Query("SELECT ph.userRegion, COUNT(ph) FROM PlayHistory ph " +
           "WHERE ph.music.artist.id = :artistId GROUP BY ph.userRegion")
    List<Object[]> getUserRegionDistribution(@Param("artistId") Long artistId);
}
