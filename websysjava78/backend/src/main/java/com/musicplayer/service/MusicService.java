package com.musicplayer.service;

import com.musicplayer.entity.Lyrics;
import com.musicplayer.entity.Music;
import com.musicplayer.repository.LyricsRepository;
import com.musicplayer.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final LyricsRepository lyricsRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${music.upload-dir}")
    private String uploadDir;

    private static final String MUSIC_CACHE_KEY = "music:";
    private static final String HOT_MUSIC_KEY = "hot:music";

    public Page<Music> getAllMusic(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (keyword != null && !keyword.isEmpty()) {
            return musicRepository.findByTitleContainingOrArtistContaining(keyword, keyword, pageable);
        }
        return musicRepository.findAll(pageable);
    }

    public Music getMusicById(Long id) {
        String cacheKey = MUSIC_CACHE_KEY + id;
        Music cached = (Music) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }
        Music music = musicRepository.findById(id).orElseThrow(() -> new RuntimeException("音乐不存在"));
        redisTemplate.opsForValue().set(cacheKey, music, 1, TimeUnit.HOURS);
        return music;
    }

    @Transactional
    public Music createMusic(Music music, MultipartFile file128, MultipartFile file320, MultipartFile fileFlac, MultipartFile lrcFile) throws IOException {
        Files.createDirectories(Paths.get(uploadDir));

        if (file128 != null && !file128.isEmpty()) {
            String fileName = saveFile(file128, "128");
            music.setFilePath128(fileName);
            music.setFileSize128(file128.getSize());
        }

        if (file320 != null && !file320.isEmpty()) {
            String fileName = saveFile(file320, "320");
            music.setFilePath320(fileName);
            music.setFileSize320(file320.getSize());
        }

        if (fileFlac != null && !fileFlac.isEmpty()) {
            String fileName = saveFile(fileFlac, "flac");
            music.setFilePathFlac(fileName);
            music.setFileSizeFlac(fileFlac.getSize());
        }

        Music saved = musicRepository.save(music);

        if (lrcFile != null && !lrcFile.isEmpty()) {
            Lyrics lyrics = new Lyrics();
            lyrics.setMusic(saved);
            lyrics.setLrcContent(new String(lrcFile.getBytes()));
            lyrics.setHasSync(true);
            lyricsRepository.save(lyrics);
            saved.setLyrics(lyrics);
        }

        return saved;
    }

    @Transactional
    public List<Music> batchImport(List<Map<String, Object>> musicDataList) {
        List<Music> importedMusic = new ArrayList<>();
        for (Map<String, Object> data : musicDataList) {
            Music music = new Music();
            music.setTitle((String) data.get("title"));
            music.setArtist((String) data.get("artist"));
            music.setAlbum((String) data.get("album"));
            music.setGenre((String) data.get("genre"));
            music.setDuration(data.get("duration") != null ? ((Number) data.get("duration")).intValue() : null);
            music.setIsPremium(data.get("isPremium") != null && (Boolean) data.get("isPremium"));

            if (data.get("filePath128") != null) {
                music.setFilePath128((String) data.get("filePath128"));
            }
            if (data.get("filePath320") != null) {
                music.setFilePath320((String) data.get("filePath320"));
            }
            if (data.get("filePathFlac") != null) {
                music.setFilePathFlac((String) data.get("filePathFlac"));
            }

            importedMusic.add(musicRepository.save(music));
        }
        return importedMusic;
    }

    @Transactional
    public Music updateMusic(Long id, Music musicDetails) {
        Music music = getMusicById(id);
        music.setTitle(musicDetails.getTitle());
        music.setArtist(musicDetails.getArtist());
        music.setAlbum(musicDetails.getAlbum());
        music.setGenre(musicDetails.getGenre());
        music.setDuration(musicDetails.getDuration());
        music.setCoverUrl(musicDetails.getCoverUrl());
        music.setIsPremium(musicDetails.getIsPremium());

        redisTemplate.delete(MUSIC_CACHE_KEY + id);
        return musicRepository.save(music);
    }

    @Transactional
    public void deleteMusic(Long id) {
        Music music = getMusicById(id);
        deleteFile(music.getFilePath128());
        deleteFile(music.getFilePath320());
        deleteFile(music.getFilePathFlac());
        musicRepository.delete(music);
        redisTemplate.delete(MUSIC_CACHE_KEY + id);
    }

    @Transactional
    public void incrementPlayCount(Long id) {
        Music music = getMusicById(id);
        music.setPlayCount(music.getPlayCount() + 1);
        musicRepository.save(music);
        redisTemplate.opsForZSet().incrementScore(HOT_MUSIC_KEY, id.toString(), 1);
    }

    public List<Music> getHotMusic(int limit) {
        Set<Object> hotIds = redisTemplate.opsForZSet().reverseRange(HOT_MUSIC_KEY, 0, limit - 1);
        List<Music> hotMusic = new ArrayList<>();
        if (hotIds != null && !hotIds.isEmpty()) {
            for (Object id : hotIds) {
                try {
                    hotMusic.add(getMusicById(Long.parseLong(id.toString())));
                } catch (Exception e) {
                }
            }
        }
        if (hotMusic.size() < limit) {
            Pageable pageable = PageRequest.of(0, limit);
            hotMusic = musicRepository.findTopByPlayCount(pageable);
        }
        return hotMusic;
    }

    public byte[] getMusicFile(Long id, String quality) throws IOException {
        Music music = getMusicById(id);
        String filePath = switch (quality) {
            case "320" -> music.getFilePath320();
            case "flac" -> music.getFilePathFlac();
            default -> music.getFilePath128();
        };
        if (filePath == null) {
            throw new RuntimeException("该音质的文件不存在");
        }
        Path path = Paths.get(uploadDir, filePath);
        return Files.readAllBytes(path);
    }

    public Lyrics getLyrics(Long musicId) {
        return lyricsRepository.findByMusicId(musicId).orElse(null);
    }

    private String saveFile(MultipartFile file, String quality) throws IOException {
        String originalName = file.getOriginalFilename();
        String extension = originalName != null ? originalName.substring(originalName.lastIndexOf(".")) : ".mp3";
        String fileName = UUID.randomUUID() + "_" + quality + extension;
        Path path = Paths.get(uploadDir, fileName);
        file.transferTo(path.toFile());
        return fileName;
    }

    private void deleteFile(String fileName) {
        if (fileName != null) {
            try {
                Path path = Paths.get(uploadDir, fileName);
                Files.deleteIfExists(path);
            } catch (IOException e) {
            }
        }
    }

    public long getFileSize(Long id, String quality) {
        Music music = getMusicById(id);
        return switch (quality) {
            case "320" -> music.getFileSize320() != null ? music.getFileSize320() : 0;
            case "flac" -> music.getFileSizeFlac() != null ? music.getFileSizeFlac() : 0;
            default -> music.getFileSize128() != null ? music.getFileSize128() : 0;
        };
    }

    public String getFileName(Long id, String quality) {
        Music music = getMusicById(id);
        String extension = "flac".equals(quality) ? ".flac" : ".mp3";
        return music.getTitle() + " - " + music.getArtist() + "(" + quality + "kbps)" + extension;
    }

    public String getMusicFilePath(Long id, String quality) {
        Music music = getMusicById(id);
        String filePath = switch (quality) {
            case "320" -> music.getFilePath320();
            case "flac" -> music.getFilePathFlac();
            default -> music.getFilePath128();
        };
        if (filePath == null) {
            throw new RuntimeException("该音质的文件不存在");
        }
        Path fullPath = Paths.get(uploadDir, filePath);
        if (!Files.exists(fullPath)) {
            throw new RuntimeException("音频文件不存在");
        }
        return fullPath.toString();
    }
}
