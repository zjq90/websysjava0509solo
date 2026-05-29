package com.musicplayer.service;

import com.musicplayer.entity.DownloadRecord;
import com.musicplayer.entity.Music;
import com.musicplayer.entity.User;
import com.musicplayer.repository.DownloadRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DownloadService {

    private final DownloadRecordRepository downloadRecordRepository;
    private final UserService userService;
    private final MusicService musicService;

    @Value("${download.free-limit}")
    private int freeLimit;

    @Value("${download.premium-limit}")
    private int premiumLimit;

    @Transactional
    public DownloadRecord createDownloadRecord(Long userId, Long musicId, String qualityStr) {
        User user = userService.getUserById(userId);
        Music music = musicService.getMusicById(musicId);

        DownloadRecord.Quality quality;
        try {
            quality = DownloadRecord.Quality.valueOf("QUALITY_" + qualityStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("无效的音质参数");
        }

        if (!canDownload(user, music, quality)) {
            throw new RuntimeException("无法下载该音乐");
        }

        DownloadRecord record = new DownloadRecord();
        record.setUser(user);
        record.setMusic(music);
        record.setQuality(quality);

        userService.incrementDownloadCount(user);

        music.setDownloadCount(music.getDownloadCount() + 1);

        return downloadRecordRepository.save(record);
    }

    public boolean canDownload(User user, Music music, DownloadRecord.Quality quality) {
        userService.resetDownloadCountIfNeeded(user);

        int limit = user.getRole() == User.UserRole.PREMIUM ? premiumLimit : freeLimit;
        if (user.getDownloadCountToday() >= limit) {
            return false;
        }

        if (music.getIsPremium() && user.getRole() == User.UserRole.FREE) {
            return false;
        }

        if (quality == DownloadRecord.Quality.QUALITY_FLAC && user.getRole() == User.UserRole.FREE) {
            return false;
        }

        return true;
    }

    public Page<DownloadRecord> getUserDownloadRecords(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "downloadedAt"));
        return downloadRecordRepository.findByUserId(userId, pageable);
    }

    public long getTodayDownloadCount(Long userId) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        return downloadRecordRepository.countTodayDownloadsByUser(userId, startOfDay);
    }
}
