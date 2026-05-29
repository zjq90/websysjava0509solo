package com.musicplatform.controller;

import com.musicplatform.entity.Comment;
import com.musicplatform.entity.Music;
import com.musicplatform.service.MusicService;
import com.musicplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/musics")
@CrossOrigin(origins = "*")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getMusics(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        Page<Music> musics = musicService.getApprovedMusics(page, size);
        return ResponseEntity.ok(successResponse(musics));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMusic(@PathVariable Long id) {
        try {
            Music music = musicService.getMusicById(id);
            return ResponseEntity.ok(successResponse(music));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMusic(@RequestParam String keyword,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        Page<Music> musics = musicService.searchMusic(keyword, page, size);
        return ResponseEntity.ok(successResponse(musics));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularMusic(@RequestParam(defaultValue = "10") int limit) {
        List<Music> musics = musicService.getPopularMusic(limit);
        return ResponseEntity.ok(successResponse(musics));
    }

    @GetMapping("/exclusive")
    public ResponseEntity<?> getExclusiveMusic(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        Page<Music> musics = musicService.getExclusiveMusic(page, size);
        return ResponseEntity.ok(successResponse(musics));
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<?> getArtistMusics(@PathVariable Long artistId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        Page<Music> musics = musicService.getArtistMusics(artistId, page, size);
        return ResponseEntity.ok(successResponse(musics));
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<?> playMusic(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        musicService.playMusic(id, currentUserId);
        return ResponseEntity.ok(successResponse("播放成功"));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long id,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        Page<Comment> comments = musicService.getComments(id, page, size);
        return ResponseEntity.ok(successResponse(comments));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            Comment comment = musicService.addComment(id, currentUserId, body.get("content"));
            return ResponseEntity.ok(successResponse(comment));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        musicService.toggleLike(id, currentUserId);
        return ResponseEntity.ok(successResponse("操作成功"));
    }

    @GetMapping("/{id}/like-status")
    public ResponseEntity<?> getLikeStatus(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        boolean liked = currentUserId != null && musicService.isLiked(id, currentUserId);
        return ResponseEntity.ok(successResponse(Map.of("liked", liked)));
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        musicService.toggleFavorite(id, currentUserId);
        return ResponseEntity.ok(successResponse("操作成功"));
    }

    @GetMapping("/{id}/favorite-status")
    public ResponseEntity<?> getFavoriteStatus(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        boolean favorited = currentUserId != null && musicService.isFavorited(id, currentUserId);
        return ResponseEntity.ok(successResponse(Map.of("favorited", favorited)));
    }

    @PostMapping
    public ResponseEntity<?> uploadMusic(@RequestBody Music music) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            Music saved = musicService.uploadMusic(music, currentUserId);
            return ResponseEntity.ok(successResponse(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    private Map<String, Object> successResponse(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        return result;
    }

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}
