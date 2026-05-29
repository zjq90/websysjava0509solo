package com.music.platform.controller;

import com.music.platform.common.Result;
import com.music.platform.entity.Music;
import com.music.platform.entity.Playlist;
import com.music.platform.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping("/personalized")
    public Result<List<Music>> personalized(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        return Result.success(recommendService.getPersonalizedRecommendations(userId, limit));
    }

    @GetMapping("/hot-chart")
    public Result<List<Music>> hotChart() {
        return Result.success(recommendService.getHotChart());
    }

    @GetMapping("/new-chart")
    public Result<List<Music>> newChart() {
        return Result.success(recommendService.getNewChart());
    }

    @GetMapping("/rising-chart")
    public Result<List<Music>> risingChart() {
        return Result.success(recommendService.getRisingChart());
    }

    @GetMapping("/scene-playlists")
    public Result<List<Playlist>> scenePlaylists() {
        return Result.success(recommendService.getScenePlaylists());
    }

    @GetMapping("/playlist/{id}/music")
    public Result<List<Music>> playlistMusic(@PathVariable Long id) {
        return Result.success(recommendService.getPlaylistMusic(id));
    }

    @PostMapping("/behavior")
    public Result<Void> recordBehavior(@RequestBody Map<String, Object> body) {
        recommendService.recordBehavior(
                Long.valueOf(body.get("userId").toString()),
                Long.valueOf(body.get("musicId").toString()),
                (String) body.get("type")
        );
        return Result.success();
    }

    @DeleteMapping("/behavior")
    public Result<Void> removeBehavior(@RequestBody Map<String, Object> body) {
        recommendService.removeBehavior(
                Long.valueOf(body.get("userId").toString()),
                Long.valueOf(body.get("musicId").toString()),
                (String) body.get("type")
        );
        return Result.success();
    }

    @GetMapping("/behavior/check")
    public Result<Boolean> checkBehavior(
            @RequestParam Long userId,
            @RequestParam Long musicId,
            @RequestParam String type) {
        return Result.success(recommendService.hasBehavior(userId, musicId, type));
    }

    @GetMapping("/user/{userId}/liked")
    public Result<List<Music>> userLiked(@PathVariable Long userId) {
        return Result.success(recommendService.getUserLikedMusic(userId));
    }

    @GetMapping("/user/{userId}/collected")
    public Result<List<Music>> userCollected(@PathVariable Long userId) {
        return Result.success(recommendService.getUserCollectedMusic(userId));
    }

    @GetMapping("/user/{userId}/playlists")
    public Result<List<Playlist>> userPlaylists(@PathVariable Long userId) {
        return Result.success(recommendService.getUserPlaylists(userId));
    }

    @PostMapping("/playlist")
    public Result<Playlist> createPlaylist(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Long> musicIds = ((List<Number>) body.get("musicIds"))
                .stream().map(Number::longValue).collect(java.util.stream.Collectors.toList());
        Playlist playlist = recommendService.createScenePlaylist(
                (String) body.get("name"),
                (String) body.get("description"),
                (String) body.get("type"),
                Long.valueOf(body.get("userId").toString()),
                musicIds
        );
        return Result.success(playlist);
    }
}
