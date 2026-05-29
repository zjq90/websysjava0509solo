package com.music.platform.repository;

import com.music.platform.entity.Copyright;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CopyrightRepository extends JpaRepository<Copyright, Long> {
    Optional<Copyright> findByMusicId(Long musicId);
}
