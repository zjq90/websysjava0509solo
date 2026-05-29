package com.musicplatform.service;

import com.musicplatform.entity.*;
import com.musicplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private PlayHistoryRepository playHistoryRepository;

    @Autowired
    private UserService userService;

    public Page<Music> getApprovedMusics(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return musicRepository.findByStatus(Music.MusicStatus.APPROVED, pageable);
    }

    public Page<Music> searchMusic(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return musicRepository.searchMusic(keyword, pageable);
    }

    public Music getMusicById(Long id) {
        return musicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("音乐不存在"));
    }

    @Transactional
    public Music uploadMusic(Music music, Long artistId) {
        User artist = userRepository.findById(artistId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (artist.getRole() != User.Role.ARTIST && artist.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("只有音乐人才能上传音乐");
        }

        music.setArtist(artist);
        music.setArtistName(artist.getNickname());
        music.setStatus(Music.MusicStatus.PENDING);
        return musicRepository.save(music);
    }

    @Transactional
    public void playMusic(Long musicId, Long userId) {
        Music music = getMusicById(musicId);
        music.setPlayCount(music.getPlayCount() + 1);
        musicRepository.save(music);

        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            PlayHistory history = new PlayHistory();
            history.setUser(user);
            history.setMusic(music);
            playHistoryRepository.save(history);
        }
    }

    public Page<Comment> getComments(Long musicId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentRepository.findByTargetTypeAndTargetIdAndParentIsNull(
            Comment.TargetType.MUSIC, musicId, pageable);
    }

    @Transactional
    public Comment addComment(Long musicId, Long userId, String content) {
        Music music = getMusicById(musicId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setTargetType(Comment.TargetType.MUSIC);
        comment.setTargetId(musicId);
        comment = commentRepository.save(comment);

        music.setCommentCount(music.getCommentCount() + 1);
        musicRepository.save(music);

        return comment;
    }

    @Transactional
    public void toggleLike(Long musicId, Long userId) {
        Music music = getMusicById(musicId);
        Like.TargetType type = Like.TargetType.MUSIC;

        if (likeRepository.existsByUser_IdAndTargetTypeAndTargetId(userId, type, musicId)) {
            likeRepository.deleteByUser_IdAndTargetTypeAndTargetId(userId, type, musicId);
            music.setLikeCount(Math.max(0, music.getLikeCount() - 1));
        } else {
            Like like = new Like();
            like.setUser(userRepository.getById(userId));
            like.setTargetType(type);
            like.setTargetId(musicId);
            likeRepository.save(like);
            music.setLikeCount(music.getLikeCount() + 1);
        }
        musicRepository.save(music);
    }

    public boolean isLiked(Long musicId, Long userId) {
        return likeRepository.existsByUser_IdAndTargetTypeAndTargetId(
            userId, Like.TargetType.MUSIC, musicId);
    }

    @Transactional
    public void toggleFavorite(Long musicId, Long userId) {
        Favorite.TargetType type = Favorite.TargetType.MUSIC;
        if (favoriteRepository.existsByUser_IdAndTargetTypeAndTargetId(userId, type, musicId)) {
            favoriteRepository.deleteByUser_IdAndTargetTypeAndTargetId(userId, type, musicId);
        } else {
            Favorite favorite = new Favorite();
            favorite.setUser(userRepository.getById(userId));
            favorite.setTargetType(type);
            favorite.setTargetId(musicId);
            favoriteRepository.save(favorite);
        }
    }

    public boolean isFavorited(Long musicId, Long userId) {
        return favoriteRepository.existsByUser_IdAndTargetTypeAndTargetId(
            userId, Favorite.TargetType.MUSIC, musicId);
    }

    public List<Music> getPopularMusic(int limit) {
        return musicRepository.findTopByPlayCount(PageRequest.of(0, limit));
    }

    public Page<Music> getArtistMusics(Long artistId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return musicRepository.findByArtist_Id(artistId, pageable);
    }

    public Page<Music> getExclusiveMusic(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return musicRepository.findExclusiveMusic(pageable);
    }
}
