package com.music.platform.repository;

import com.music.platform.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    List<Tag> findByNameContaining(String name);
    List<Tag> findByUserId(Long userId);
}
