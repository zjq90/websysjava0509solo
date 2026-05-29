package com.musicplatform.repository;

import com.musicplatform.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    Page<Music> findByStatus(Music.MusicStatus status, Pageable pageable);

    Page<Music> findByArtist_Id(Long artistId, Pageable pageable);

    @Query("SELECT m FROM Music m WHERE m.status = 'APPROVED' AND " +
           "(m.title LIKE %:keyword% OR m.artistName LIKE %:keyword% OR m.album LIKE %:keyword%)")
    Page<Music> searchMusic(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM Music m WHERE m.status = 'APPROVED' ORDER BY m.playCount DESC")
    List<Music> findTopByPlayCount(Pageable pageable);

    @Query("SELECT m FROM Music m WHERE m.status = 'APPROVED' AND m.isExclusive = true")
    Page<Music> findExclusiveMusic(Pageable pageable);

    @Query("SELECT m FROM Music m WHERE m.artist.id = :artistId AND m.status = 'APPROVED'")
    List<Music> findApprovedByArtistId(@Param("artistId") Long artistId);

    List<Music> findByTagsContaining(String tag);
}
