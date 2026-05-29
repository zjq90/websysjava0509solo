package com.musicplatform.service;

import com.musicplatform.entity.Music;
import com.musicplatform.entity.User;
import com.musicplatform.repository.MusicRepository;
import com.musicplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicRepository musicRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalArtists", userRepository.findByRole(User.Role.ARTIST).size());
        stats.put("totalVipUsers", userRepository.findByRole(User.Role.VIP_USER).size());
        stats.put("totalMusic", musicRepository.count());
        stats.put("pendingMusic", musicRepository.findByStatus(Music.MusicStatus.PENDING, Pageable.unpaged()).getTotalElements());
        stats.put("approvedMusic", musicRepository.findByStatus(Music.MusicStatus.APPROVED, Pageable.unpaged()).getTotalElements());

        return stats;
    }

    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void updateUserRole(Long userId, User.Role newRole) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setRole(newRole);
        userRepository.save(user);
    }

    @Transactional
    public void blockUser(Long userId, boolean blocked) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setBlocked(blocked);
        userRepository.save(user);
    }

    public Page<Music> getPendingMusic(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return musicRepository.findByStatus(Music.MusicStatus.PENDING, pageable);
    }

    @Transactional
    public void approveMusic(Long musicId) {
        Music music = musicRepository.findById(musicId)
            .orElseThrow(() -> new RuntimeException("音乐不存在"));
        music.setStatus(Music.MusicStatus.APPROVED);
        musicRepository.save(music);
    }

    @Transactional
    public void rejectMusic(Long musicId, String reason) {
        Music music = musicRepository.findById(musicId)
            .orElseThrow(() -> new RuntimeException("音乐不存在"));
        music.setStatus(Music.MusicStatus.REJECTED);
        musicRepository.save(music);
    }

    @Transactional
    public void removeMusic(Long musicId) {
        Music music = musicRepository.findById(musicId)
            .orElseThrow(() -> new RuntimeException("音乐不存在"));
        music.setStatus(Music.MusicStatus.REMOVED);
        musicRepository.save(music);
    }
}
