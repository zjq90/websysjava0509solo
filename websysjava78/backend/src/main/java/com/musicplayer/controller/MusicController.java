package com.musicplayer.controller;

import com.musicplayer.entity.Lyrics;
import com.musicplayer.entity.Music;
import com.musicplayer.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @GetMapping("/list")
    public ResponseEntity<Page<Music>> getAllMusic(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(musicService.getAllMusic(page, size, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Music> getMusicById(@PathVariable Long id) {
        return ResponseEntity.ok(musicService.getMusicById(id));
    }

    @GetMapping("/hot")
    public ResponseEntity<List<Music>> getHotMusic(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(musicService.getHotMusic(limit));
    }

    @PostMapping
    public ResponseEntity<?> createMusic(
            @RequestPart("music") Music music,
            @RequestPart(value = "file128", required = false) MultipartFile file128,
            @RequestPart(value = "file320", required = false) MultipartFile file320,
            @RequestPart(value = "fileFlac", required = false) MultipartFile fileFlac,
            @RequestPart(value = "lrcFile", required = false) MultipartFile lrcFile) {
        try {
            Music saved = musicService.createMusic(music, file128, file320, fileFlac, lrcFile);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "文件上传失败: " + e.getMessage()));
        }
    }

    @PostMapping("/batch-import")
    public ResponseEntity<?> batchImport(@RequestBody List<Map<String, Object>> musicDataList) {
        try {
            List<Music> imported = musicService.batchImport(musicDataList);
            return ResponseEntity.ok(Map.of(
                    "message", "批量导入成功",
                    "count", imported.size(),
                    "data", imported
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "批量导入失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Music> updateMusic(@PathVariable Long id, @RequestBody Music musicDetails) {
        return ResponseEntity.ok(musicService.updateMusic(id, musicDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMusic(@PathVariable Long id) {
        musicService.deleteMusic(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }

    @GetMapping("/stream/{id}/{quality}")
    public ResponseEntity<StreamingResponseBody> streamMusic(
            @PathVariable Long id,
            @PathVariable String quality) throws IOException {
        musicService.incrementPlayCount(id);
        
        String filePath = musicService.getMusicFilePath(id, quality);
        Path path = Paths.get(filePath);
        long fileSize = Files.size(path);
        
        String contentType = "flac".equals(quality) ? "audio/flac" : "audio/mpeg";
        String fileName = musicService.getFileName(id, quality);

        StreamingResponseBody responseBody = outputStream -> {
            try (InputStream inputStream = Files.newInputStream(path)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentLength(fileSize)
                .body(responseBody);
    }

    @GetMapping("/lyrics/{musicId}")
    public ResponseEntity<Lyrics> getLyrics(@PathVariable Long musicId) {
        Lyrics lyrics = musicService.getLyrics(musicId);
        if (lyrics == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lyrics);
    }

    @PostMapping("/play/{id}")
    public ResponseEntity<?> incrementPlayCount(@PathVariable Long id) {
        musicService.incrementPlayCount(id);
        return ResponseEntity.ok(Map.of("message", "播放计数已更新"));
    }
}
