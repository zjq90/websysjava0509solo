package com.music.platform.repository;

import com.music.platform.entity.PlaylistMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, Long> {
    List<PlaylistMusic> findByPlaylistIdOrderByPosition(Long playlistId);
    void deleteByPlaylistId(Long playlistId);
}
