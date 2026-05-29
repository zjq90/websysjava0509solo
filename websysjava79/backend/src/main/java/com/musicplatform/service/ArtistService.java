package com.musicplatform.service;

import com.musicplatform.entity.ArtistStats;
import com.musicplatform.entity.Music;
import com.musicplatform.entity.User;
import com.musicplatform.repository.ArtistStatsRepository;
import com.musicplatform.repository.MusicRepository;
import com.musicplatform.repository.PlayHistoryRepository;
import com.musicplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArtistService {

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayHistoryRepository playHistoryRepository;

    @Autowired
    private ArtistStatsRepository artistStatsRepository;

    @Autowired
    private PaymentService paymentService;

    public Map<String, Object> getArtistDashboard(Long artistId) {
        User artist = userRepository.findById(artistId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (artist.getRole() != User.Role.ARTIST) {
            throw new RuntimeException("不是音乐人");
        }

        Map<String, Object> dashboard = new HashMap<>();

        List<Music> musics = musicRepository.findApprovedByArtistId(artistId);
        int totalPlays = musics.stream().mapToInt(Music::getPlayCount).sum();
        int totalDownloads = musics.stream().mapToInt(Music::getDownloadCount).sum();

        dashboard.put("totalMusicCount", musics.size());
        dashboard.put("totalPlays", totalPlays);
        dashboard.put("totalDownloads", totalDownloads);
        dashboard.put("totalRevenue", paymentService.getArtistTotalRevenue(artistId));
        dashboard.put("followerCount", userRepository.countFollowers(artistId));

        return dashboard;
    }

    public Map<String, Object> getMusicStats(Long artistId, int days) {
        Map<String, Object> stats = new HashMap<>();

        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        List<Object[]> playStats = playHistoryRepository.countPlaysByArtistId(artistId, startTime);

        stats.put("period", days + "天");
        stats.put("playData", playStats);

        return stats;
    }

    public Map<String, Object> getFanStats(Long artistId) {
        Map<String, Object> stats = new HashMap<>();

        List<Object[]> regionDistribution = playHistoryRepository.getUserRegionDistribution(artistId);
        stats.put("regionDistribution", regionDistribution);
        stats.put("totalFans", userRepository.countFollowers(artistId));

        return stats;
    }

    public List<ArtistStats> getDailyStats(Long artistId, LocalDate startDate, LocalDate endDate) {
        return artistStatsRepository.findByArtist_IdAndStatsDateBetween(artistId, startDate, endDate);
    }
}
