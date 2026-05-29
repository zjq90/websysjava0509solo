package com.musicplatform.repository;

import com.musicplatform.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Page<Playlist> findByCreator_Id(Long creatorId, Pageable pageable);

    Page<Playlist> findByPrivacy(Playlist.PrivacyLevel privacy, Pageable pageable);

    @Query("SELECT p FROM Playlist p WHERE p.privacy = 'PUBLIC' AND " +
           "(p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Playlist> searchPublicPlaylists(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Playlist p WHERE p.privacy = 'PUBLIC' ORDER BY p.playCount DESC")
    List<Playlist> findTopPopular(Pageable pageable);

    @Query("SELECT p FROM Playlist p WHERE p.creator.id = :userId AND p.privacy = 'PUBLIC'")
    Page<Playlist> findPublicPlaylistsByUserId(@Param("userId") Long userId, Pageable pageable);

    long countByCreator_Id(Long creatorId);
}
