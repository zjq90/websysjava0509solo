package com.music.platform.controller;

import com.music.platform.common.PageResult;
import com.music.platform.common.Result;
import com.music.platform.entity.Category;
import com.music.platform.entity.Music;
import com.music.platform.entity.Tag;
import com.music.platform.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicController {
    private final MusicService musicService;

    @PostMapping("/upload")
    public Result<Music> upload(
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam(value = "album", required = false) String album,
            @RequestParam(value = "lyrics", required = false) String lyrics,
            @RequestParam("format") String format,
            @RequestParam("userId") Long userId,
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam(value = "coverFile", required = false) MultipartFile coverFile) {
        try {
            Music music = musicService.uploadMusic(title, artist, album, lyrics, format, userId, audioFile, coverFile);
            return Result.success(music);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        } finally {
            cleanupMultipart(audioFile);
            cleanupMultipart(coverFile);
        }
    }

    @PutMapping("/{id}")
    public Result<Music> update(@PathVariable Long id,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "artist", required = false) String artist,
                                @RequestParam(value = "album", required = false) String album,
                                @RequestParam(value = "lyrics", required = false) String lyrics,
                                @RequestParam(value = "coverFile", required = false) MultipartFile coverFile) {
        try {
            Music music = musicService.updateMusic(id, title, artist, album, lyrics, coverFile);
            return Result.success(music);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        } finally {
            cleanupMultipart(coverFile);
        }
    }

    private void cleanupMultipart(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                file.getInputStream().close();
            } catch (Exception ignored) {
            }
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        musicService.deleteMusic(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Music> getById(@PathVariable Long id) {
        return musicService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "Music not found"));
    }

    @GetMapping("/list")
    public Result<PageResult<Music>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(musicService.findAll(page, size));
    }

    @GetMapping("/{id}/categories")
    public Result<List<Category>> getCategories(@PathVariable Long id) {
        return Result.success(musicService.getMusicCategories(id));
    }

    @GetMapping("/{id}/tags")
    public Result<List<Tag>> getTags(@PathVariable Long id) {
        return Result.success(musicService.getMusicTags(id));
    }

    @PostMapping("/{id}/categories")
    public Result<Void> assignCategories(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        musicService.assignCategories(id, body.get("categoryIds"));
        return Result.success();
    }

    @PostMapping("/{id}/tags")
    public Result<Void> assignTags(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        musicService.assignTags(id, body.get("tagIds"));
        return Result.success();
    }
}
