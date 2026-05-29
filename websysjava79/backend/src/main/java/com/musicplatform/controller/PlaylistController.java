package com.musicplatform.controller;

import com.musicplatform.entity.Comment;
import com.musicplatform.entity.Playlist;
import com.musicplatform.service.PlaylistService;
import com.musicplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getPublicPlaylists(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size) {
        Page<Playlist> playlists = playlistService.getPublicPlaylists(page, size);
        return ResponseEntity.ok(successResponse(playlists));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylist(@PathVariable Long id) {
        try {
            Playlist playlist = playlistService.getPlaylistById(id);
            return ResponseEntity.ok(successResponse(playlist));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPlaylists(@RequestParam String keyword,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        Page<Playlist> playlists = playlistService.searchPlaylists(keyword, page, size);
        return ResponseEntity.ok(successResponse(playlists));
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularPlaylists(@RequestParam(defaultValue = "10") int limit) {
        List<Playlist> playlists = playlistService.getPopularPlaylists(limit);
        return ResponseEntity.ok(successResponse(playlists));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPlaylists(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        Page<Playlist> playlists = playlistService.getUserPlaylists(userId, page, size);
        return ResponseEntity.ok(successResponse(playlists));
    }

    @PostMapping
    public ResponseEntity<?> createPlaylist(@RequestBody Playlist playlist) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        Playlist created = playlistService.createPlaylist(playlist, currentUserId);
        return ResponseEntity.ok(successResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlaylist(@PathVariable Long id, @RequestBody Playlist updates) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            Playlist updated = playlistService.updatePlaylist(id, updates, currentUserId);
            return ResponseEntity.ok(successResponse(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{id}/musics/{musicId}")
    public ResponseEntity<?> addMusicToPlaylist(@PathVariable Long id, @PathVariable Long musicId) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            playlistService.addMusicToPlaylist(id, musicId, currentUserId);
            return ResponseEntity.ok(successResponse("添加成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/musics/{musicId}")
    public ResponseEntity<?> removeMusicFromPlaylist(@PathVariable Long id, @PathVariable Long musicId) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            playlistService.removeMusicFromPlaylist(id, musicId, currentUserId);
            return ResponseEntity.ok(successResponse("移除成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long id,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        Page<Comment> comments = playlistService.getComments(id, page, size);
        return ResponseEntity.ok(successResponse(comments));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        try {
            Comment comment = playlistService.addComment(id, currentUserId, body.get("content"));
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
        playlistService.toggleLike(id, currentUserId);
        return ResponseEntity.ok(successResponse("操作成功"));
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long id) {
        Long currentUserId = userService.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(errorResponse("请先登录"));
        }
        playlistService.toggleFavorite(id, currentUserId);
        return ResponseEntity.ok(successResponse("操作成功"));
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
