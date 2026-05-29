package com.music.platform.controller;

import com.music.platform.common.PageResult;
import com.music.platform.common.Result;
import com.music.platform.entity.Music;
import com.music.platform.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public Result<PageResult<Music>> search(
            @RequestParam String keyword,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(searchService.search(keyword, userId, page, size));
    }

    @GetMapping("/hot")
    public Result<List<String>> hotKeywords() {
        return Result.success(searchService.getHotKeywords());
    }

    @GetMapping("/history/{userId}")
    public Result<List<String>> searchHistory(@PathVariable Long userId) {
        return Result.success(searchService.getUserSearchHistory(userId));
    }

    @DeleteMapping("/history/{userId}")
    public Result<Void> clearHistory(@PathVariable Long userId) {
        searchService.clearUserSearchHistory(userId);
        return Result.success();
    }
}
