package com.music.platform.repository;

import com.music.platform.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT sh.keyword, COUNT(sh) as cnt FROM SearchHistory sh GROUP BY sh.keyword ORDER BY cnt DESC")
    List<Object[]> findHotKeywords();

    void deleteByUserId(Long userId);
}
