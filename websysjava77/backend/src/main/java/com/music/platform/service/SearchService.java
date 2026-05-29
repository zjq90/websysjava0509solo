package com.music.platform.service;

import com.music.platform.common.PageResult;
import com.music.platform.config.CacheService;
import com.music.platform.entity.Music;
import com.music.platform.entity.SearchHistory;
import com.music.platform.repository.MusicRepository;
import com.music.platform.repository.SearchHistoryRepository;
import com.music.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final MusicRepository musicRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;
    private final CacheService cacheService;

    public PageResult<Music> search(String keyword, Long userId, int page, int size) {
        if (userId != null && keyword != null && !keyword.trim().isEmpty()) {
            SearchHistory history = new SearchHistory();
            history.setUserId(userId);
            history.setKeyword(keyword);
            searchHistoryRepository.save(history);
        }

        String cacheKey = "search:" + keyword + ":" + page + ":" + size;
        Page<Music> result = musicRepository.searchByKeyword(keyword, PageRequest.of(page, size));
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, size);
    }

    public List<String> getHotKeywords() {
        String cacheKey = "search:hot";
        Optional<String> cached = cacheService.get(cacheKey);
        if (cached.isPresent()) {
            return new ArrayList<>();
        }

        List<Object[]> hotKeywords = searchHistoryRepository.findHotKeywords();
        List<String> result = hotKeywords.stream()
                .limit(10)
                .map(row -> (String) row[0])
                .collect(Collectors.toList());

        if (!result.isEmpty()) {
            cacheService.set(cacheKey, String.join(",", result), 600);
        }
        return result;
    }

    public List<String> getUserSearchHistory(Long userId) {
        return searchHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(SearchHistory::getKeyword)
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
    }

    public void clearUserSearchHistory(Long userId) {
        searchHistoryRepository.deleteByUserId(userId);
    }
}
