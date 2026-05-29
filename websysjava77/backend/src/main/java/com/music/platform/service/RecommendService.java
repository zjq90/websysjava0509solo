package com.music.platform.service;

import com.music.platform.entity.Music;
import com.music.platform.entity.Playlist;
import com.music.platform.entity.PlaylistMusic;
import com.music.platform.entity.UserBehavior;
import com.music.platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final MusicRepository musicRepository;
    private final UserBehaviorRepository userBehaviorRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistMusicRepository playlistMusicRepository;
    private final MusicCategoryRepository musicCategoryRepository;
    private final CategoryRepository categoryRepository;

    public List<Music> getPersonalizedRecommendations(Long userId, int limit) {
        List<Long> likedMusicIds = userBehaviorRepository.findLikedMusicIds(userId);
        List<Long> collectedMusicIds = userBehaviorRepository.findCollectedMusicIds(userId);
        List<Long> recentPlayedIds = userBehaviorRepository.findRecentPlayedMusicIds(userId);

        Set<Long> interactedIds = new HashSet<>();
        interactedIds.addAll(likedMusicIds);
        interactedIds.addAll(collectedMusicIds);
        interactedIds.addAll(recentPlayedIds);

        Set<Long> preferredCategoryIds = new HashSet<>();
        for (Long musicId : interactedIds) {
            musicCategoryRepository.findByMusicId(musicId)
                    .forEach(mc -> preferredCategoryIds.add(mc.getCategoryId()));
        }

        List<Music> recommended = new ArrayList<>();
        if (!preferredCategoryIds.isEmpty()) {
            for (Long categoryId : preferredCategoryIds) {
                List<Music> categoryMusic = musicCategoryRepository.findByCategoryId(categoryId)
                        .stream()
                        .map(mc -> musicRepository.findById(mc.getMusicId()))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(m -> !interactedIds.contains(m.getId()))
                        .collect(Collectors.toList());
                recommended.addAll(categoryMusic);
            }
        }

        if (recommended.size() < limit) {
            List<Music> hotMusic = musicRepository.findTop10ByOrderByPlayCountDesc();
            Set<Long> existingIds = recommended.stream().map(Music::getId).collect(Collectors.toSet());
            existingIds.addAll(interactedIds);
            for (Music m : hotMusic) {
                if (!existingIds.contains(m.getId())) {
                    recommended.add(m);
                    existingIds.add(m.getId());
                }
            }
        }

        Collections.shuffle(recommended);
        return recommended.stream().limit(limit).collect(Collectors.toList());
    }

    public List<Music> getHotChart() {
        return musicRepository.findTop10ByOrderByPlayCountDesc();
    }

    public List<Music> getNewChart() {
        return musicRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Music> getRisingChart() {
        return musicRepository.findTop10ByOrderByLikeCountDesc();
    }

    public List<Playlist> getScenePlaylists() {
        return playlistRepository.findByType("scene");
    }

    public Playlist createScenePlaylist(String name, String description, String type, Long userId, List<Long> musicIds) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setType(type);
        playlist.setUserId(userId);
        Playlist saved = playlistRepository.save(playlist);

        int position = 0;
        for (Long musicId : musicIds) {
            PlaylistMusic pm = new PlaylistMusic();
            pm.setPlaylistId(saved.getId());
            pm.setMusicId(musicId);
            pm.setPosition(position++);
            playlistMusicRepository.save(pm);
        }
        return saved;
    }

    public List<Music> getPlaylistMusic(Long playlistId) {
        return playlistMusicRepository.findByPlaylistIdOrderByPosition(playlistId)
                .stream()
                .map(pm -> musicRepository.findById(pm.getMusicId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Playlist> getUserPlaylists(Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    public void recordBehavior(Long userId, Long musicId, String type) {
        if (!userBehaviorRepository.existsByUserIdAndMusicIdAndType(userId, musicId, type)) {
            UserBehavior behavior = new UserBehavior();
            behavior.setUserId(userId);
            behavior.setMusicId(musicId);
            behavior.setType(type);
            userBehaviorRepository.save(behavior);
        }

        if ("like".equals(type)) {
            Music music = musicRepository.findById(musicId).orElse(null);
            if (music != null) {
                music.setLikeCount(music.getLikeCount() + 1);
                musicRepository.save(music);
            }
        } else if ("collect".equals(type)) {
            Music music = musicRepository.findById(musicId).orElse(null);
            if (music != null) {
                music.setCollectCount(music.getCollectCount() + 1);
                musicRepository.save(music);
            }
        } else if ("play".equals(type)) {
            Music music = musicRepository.findById(musicId).orElse(null);
            if (music != null) {
                music.setPlayCount(music.getPlayCount() + 1);
                musicRepository.save(music);
            }
        }
    }

    public void removeBehavior(Long userId, Long musicId, String type) {
        userBehaviorRepository.deleteByUserIdAndMusicIdAndType(userId, musicId, type);
        if ("like".equals(type)) {
            Music music = musicRepository.findById(musicId).orElse(null);
            if (music != null && music.getLikeCount() > 0) {
                music.setLikeCount(music.getLikeCount() - 1);
                musicRepository.save(music);
            }
        } else if ("collect".equals(type)) {
            Music music = musicRepository.findById(musicId).orElse(null);
            if (music != null && music.getCollectCount() > 0) {
                music.setCollectCount(music.getCollectCount() - 1);
                musicRepository.save(music);
            }
        }
    }

    public boolean hasBehavior(Long userId, Long musicId, String type) {
        return userBehaviorRepository.existsByUserIdAndMusicIdAndType(userId, musicId, type);
    }

    public List<Music> getUserLikedMusic(Long userId) {
        return userBehaviorRepository.findLikedMusicIds(userId)
                .stream()
                .map(musicRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Music> getUserCollectedMusic(Long userId) {
        return userBehaviorRepository.findCollectedMusicIds(userId)
                .stream()
                .map(musicRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
