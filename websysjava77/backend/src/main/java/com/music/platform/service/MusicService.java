package com.music.platform.service;

import com.music.platform.common.PageResult;
import com.music.platform.config.CacheService;
import com.music.platform.entity.*;
import com.music.platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;
    private final MusicCategoryRepository musicCategoryRepository;
    private final MusicTagRepository musicTagRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final FileStorageService fileStorageService;
    private final CacheService cacheService;

    @Transactional
    public Music uploadMusic(String title, String artist, String album, String lyrics,
                             String format, Long userId, MultipartFile audioFile, MultipartFile coverFile) {
        String filePath = fileStorageService.storeAudioFile(audioFile);
        String coverPath = null;
        if (coverFile != null && !coverFile.isEmpty()) {
            coverPath = fileStorageService.storeCoverFile(coverFile);
        }

        Music music = new Music();
        music.setTitle(title);
        music.setArtist(artist);
        music.setAlbum(album);
        music.setLyrics(lyrics);
        music.setFormat(format);
        music.setFilePath(filePath);
        music.setCoverPath(coverPath);
        music.setFileSize(audioFile.getSize());
        music.setDuration(0.0);
        music.setUserId(userId);

        Music saved = musicRepository.save(music);
        cacheService.delete("music:list");
        return saved;
    }

    public Music updateMusic(Long id, String title, String artist, String album, String lyrics,
                             MultipartFile coverFile) {
        Music music = musicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));

        if (title != null) music.setTitle(title);
        if (artist != null) music.setArtist(artist);
        if (album != null) music.setAlbum(album);
        if (lyrics != null) music.setLyrics(lyrics);
        if (coverFile != null && !coverFile.isEmpty()) {
            if (music.getCoverPath() != null) {
                fileStorageService.deleteFile(music.getCoverPath());
            }
            music.setCoverPath(fileStorageService.storeCoverFile(coverFile));
        }

        cacheService.delete("music:" + id);
        return musicRepository.save(music);
    }

    @Transactional
    public void deleteMusic(Long id) {
        Music music = musicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        fileStorageService.deleteFile(music.getFilePath());
        if (music.getCoverPath() != null) {
            fileStorageService.deleteFile(music.getCoverPath());
        }
        musicCategoryRepository.deleteByMusicId(id);
        musicTagRepository.deleteByMusicId(id);
        musicRepository.deleteById(id);
        cacheService.delete("music:" + id);
    }

    public Optional<Music> findById(Long id) {
        return musicRepository.findById(id);
    }

    public PageResult<Music> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Music> result = musicRepository.findAll(pageable);
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, size);
    }

    public PageResult<Music> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Music> result = musicRepository.searchByKeyword(keyword, pageable);
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, size);
    }

    public List<Music> getHotMusic(int limit) {
        String cacheKey = "music:hot:" + limit;
        Optional<String> cached = cacheService.get(cacheKey);
        if (cached.isPresent()) {
            return Collections.emptyList();
        }
        return musicRepository.findTop10ByOrderByPlayCountDesc();
    }

    public List<Music> getNewMusic() {
        return musicRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Music> getRisingMusic() {
        return musicRepository.findTop10ByOrderByLikeCountDesc();
    }

    public void incrementPlayCount(Long id) {
        Music music = musicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        music.setPlayCount(music.getPlayCount() + 1);
        musicRepository.save(music);
        cacheService.delete("music:" + id);
        cacheService.delete("music:hot:10");
    }

    @Transactional
    public void assignCategories(Long musicId, List<Long> categoryIds) {
        musicCategoryRepository.deleteByMusicId(musicId);
        for (Long categoryId : categoryIds) {
            MusicCategory mc = new MusicCategory();
            mc.setMusicId(musicId);
            mc.setCategoryId(categoryId);
            musicCategoryRepository.save(mc);
        }
    }

    @Transactional
    public void assignTags(Long musicId, List<Long> tagIds) {
        musicTagRepository.deleteByMusicId(musicId);
        for (Long tagId : tagIds) {
            MusicTag mt = new MusicTag();
            mt.setMusicId(musicId);
            mt.setTagId(tagId);
            musicTagRepository.save(mt);
        }
    }

    public List<Category> getMusicCategories(Long musicId) {
        List<MusicCategory> mcs = musicCategoryRepository.findByMusicId(musicId);
        return mcs.stream()
                .map(mc -> categoryRepository.findById(mc.getCategoryId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Tag> getMusicTags(Long musicId) {
        List<MusicTag> mts = musicTagRepository.findByMusicId(musicId);
        return mts.stream()
                .map(mt -> tagRepository.findById(mt.getTagId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Music> getMusicByCategory(Long categoryId) {
        List<MusicCategory> mcs = musicCategoryRepository.findByCategoryId(categoryId);
        return mcs.stream()
                .map(mc -> musicRepository.findById(mc.getMusicId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Music> getMusicByTag(Long tagId) {
        List<MusicTag> mts = musicTagRepository.findByTagId(tagId);
        return mts.stream()
                .map(mt -> musicRepository.findById(mt.getMusicId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
