package com.musicplayer.controller;

import com.musicplayer.entity.DownloadRecord;
import com.musicplayer.entity.User;
import com.musicplayer.service.DownloadService;
import com.musicplayer.service.MusicService;
import com.musicplayer.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class DownloadController {

    private final DownloadService downloadService;
    private final MusicService musicService;
    private final UserService userService;

    @PostMapping("/{musicId}/{quality}")
    public ResponseEntity<?> downloadMusic(
            HttpServletRequest request,
            @PathVariable Long musicId,
            @PathVariable String quality) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "请先登录"));
        }

        try {
            DownloadRecord record = downloadService.createDownloadRecord(userId, musicId, quality);
            return ResponseEntity.ok(Map.of(
                    "message", "下载记录已创建",
                    "recordId", record.getId(),
                    "downloadUrl", "/api/download/file/" + musicId + "/" + quality
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/file/{musicId}/{quality}")
    public ResponseEntity<StreamingResponseBody> downloadFile(
            HttpServletRequest request,
            @PathVariable Long musicId,
            @PathVariable String quality) throws IOException {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        userService.getUserById(userId);
        String filePath = musicService.getMusicFilePath(musicId, quality);
        Path path = Paths.get(filePath);
        long fileSize = Files.size(path);

        String contentType = "flac".equals(quality) ? "audio/flac" : "audio/mpeg";
        String fileName = musicService.getFileName(musicId, quality);

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
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileSize)
                .body(responseBody);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<DownloadRecord>> getMyDownloads(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(downloadService.getUserDownloadRecords(userId, page, size));
    }

    @GetMapping("/check/{musicId}/{quality}")
    public ResponseEntity<?> canDownload(
            HttpServletRequest request,
            @PathVariable Long musicId,
            @PathVariable String quality) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(Map.of("canDownload", false, "reason", "请先登录"));
        }

        try {
            User user = userService.getUserById(userId);
            var music = musicService.getMusicById(musicId);
            DownloadRecord.Quality q = DownloadRecord.Quality.valueOf("QUALITY_" + quality.toUpperCase());

            boolean canDownload = downloadService.canDownload(user, music, q);
            Map<String, Object> response = new HashMap<>();
            response.put("canDownload", canDownload);

            if (!canDownload) {
                if (music.getIsPremium() && user.getRole() == User.UserRole.FREE) {
                    response.put("reason", "该音乐为付费会员专享，请升级会员");
                } else if (q == DownloadRecord.Quality.QUALITY_FLAC && user.getRole() == User.UserRole.FREE) {
                    response.put("reason", "无损音质为付费会员专享，请升级会员");
                } else {
                    response.put("reason", "今日下载次数已用完，请明天再试");
                }
                response.put("remaining", 0);
            } else {
                response.put("remaining", userService.getRemainingDownloads(user));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("canDownload", false, "reason", e.getMessage()));
        }
    }
}
