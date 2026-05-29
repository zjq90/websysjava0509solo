package com.musicplayer.repository;

import com.musicplayer.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

    Page<Music> findByTitleContainingOrArtistContaining(String title, String artist, Pageable pageable);

    List<Music> findByIsPremiumFalse();

    @Query("SELECT m FROM Music m WHERE m.isPremium = :isPremium")
    Page<Music> findByIsPremium(@Param("isPremium") Boolean isPremium, Pageable pageable);

    @Query("SELECT m FROM Music m ORDER BY m.playCount DESC")
    List<Music> findTopByPlayCount(Pageable pageable);
}
