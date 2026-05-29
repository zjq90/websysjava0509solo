package com.music.platform.repository;

import com.music.platform.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {
    Page<Music> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Music> findByArtistContainingIgnoreCase(String artist, Pageable pageable);
    Page<Music> findByAlbumContainingIgnoreCase(String album, Pageable pageable);

    @Query("SELECT m FROM Music m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(m.artist) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(m.album) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Music> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<Music> findTop10ByOrderByPlayCountDesc();
    List<Music> findTop10ByOrderByCreatedAtDesc();
    List<Music> findTop10ByOrderByLikeCountDesc();

    @Query("SELECT m FROM Music m WHERE m.userId = :userId")
    Page<Music> findByUserId(@Param("userId") Long userId, Pageable pageable);

    long countByUserId(Long userId);
}
