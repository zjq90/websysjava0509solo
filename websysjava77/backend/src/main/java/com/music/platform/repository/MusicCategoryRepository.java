package com.music.platform.repository;

import com.music.platform.entity.MusicCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MusicCategoryRepository extends JpaRepository<MusicCategory, Long> {
    List<MusicCategory> findByMusicId(Long musicId);
    List<MusicCategory> findByCategoryId(Long categoryId);
    void deleteByMusicId(Long musicId);
}
