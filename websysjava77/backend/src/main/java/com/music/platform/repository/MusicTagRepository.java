package com.music.platform.repository;

import com.music.platform.entity.MusicTag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MusicTagRepository extends JpaRepository<MusicTag, Long> {
    List<MusicTag> findByMusicId(Long musicId);
    List<MusicTag> findByTagId(Long tagId);
    void deleteByMusicId(Long musicId);
}
