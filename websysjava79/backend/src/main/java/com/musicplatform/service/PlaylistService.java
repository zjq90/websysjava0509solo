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
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

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

    @Transactional
    public Playlist createPlaylist(Playlist playlist, Long creatorId) {
        User creator = userRepository.findById(creatorId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        playlist.setCreator(creator);
        return playlistRepository.save(playlist);
    }

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("歌单不存在"));
    }

    public Page<Playlist> getPublicPlaylists(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return playlistRepository.findByPrivacy(Playlist.PrivacyLevel.PUBLIC, pageable);
    }

    public Page<Playlist> getUserPlaylists(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return playlistRepository.findByCreator_Id(userId, pageable);
    }

    public Page<Playlist> searchPlaylists(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return playlistRepository.searchPublicPlaylists(keyword, pageable);
    }

    @Transactional
    public void addMusicToPlaylist(Long playlistId, Long musicId, Long userId) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getCreator().getId().equals(userId)) {
            throw new RuntimeException("无权限修改此歌单");
        }

        Music music = musicRepository.findById(musicId)
            .orElseThrow(() -> new RuntimeException("音乐不存在"));

        if (!playlist.getMusics().contains(music)) {
            playlist.getMusics().add(music);
            playlistRepository.save(playlist);
        }
    }

    @Transactional
    public void removeMusicFromPlaylist(Long playlistId, Long musicId, Long userId) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getCreator().getId().equals(userId)) {
            throw new RuntimeException("无权限修改此歌单");
        }

        playlist.getMusics().removeIf(m -> m.getId().equals(musicId));
        playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist updatePlaylist(Long playlistId, Playlist updates, Long userId) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getCreator().getId().equals(userId)) {
            throw new RuntimeException("无权限修改此歌单");
        }

        if (updates.getName() != null) {
            playlist.setName(updates.getName());
        }
        if (updates.getDescription() != null) {
            playlist.setDescription(updates.getDescription());
        }
        if (updates.getCoverUrl() != null) {
            playlist.setCoverUrl(updates.getCoverUrl());
        }
        if (updates.getPrivacy() != null) {
            playlist.setPrivacy(updates.getPrivacy());
        }

        return playlistRepository.save(playlist);
    }

    public Page<Comment> getComments(Long playlistId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentRepository.findByTargetTypeAndTargetIdAndParentIsNull(
            Comment.TargetType.PLAYLIST, playlistId, pageable);
    }

    @Transactional
    public Comment addComment(Long playlistId, Long userId, String content) {
        Playlist playlist = getPlaylistById(playlistId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setTargetType(Comment.TargetType.PLAYLIST);
        comment.setTargetId(playlistId);
        comment = commentRepository.save(comment);

        playlist.setCommentCount(playlist.getCommentCount() + 1);
        playlistRepository.save(playlist);

        return comment;
    }

    @Transactional
    public void toggleLike(Long playlistId, Long userId) {
        Playlist playlist = getPlaylistById(playlistId);
        Like.TargetType type = Like.TargetType.PLAYLIST;

        if (likeRepository.existsByUser_IdAndTargetTypeAndTargetId(userId, type, playlistId)) {
            likeRepository.deleteByUser_IdAndTargetTypeAndTargetId(userId, type, playlistId);
            playlist.setLikeCount(Math.max(0, playlist.getLikeCount() - 1));
        } else {
            Like like = new Like();
            like.setUser(userRepository.getById(userId));
            like.setTargetType(type);
            like.setTargetId(playlistId);
            likeRepository.save(like);
            playlist.setLikeCount(playlist.getLikeCount() + 1);
        }
        playlistRepository.save(playlist);
    }

    @Transactional
    public void toggleFavorite(Long playlistId, Long userId) {
        Favorite.TargetType type = Favorite.TargetType.PLAYLIST;
        if (favoriteRepository.existsByUser_IdAndTargetTypeAndTargetId(userId, type, playlistId)) {
            favoriteRepository.deleteByUser_IdAndTargetTypeAndTargetId(userId, type, playlistId);
        } else {
            Favorite favorite = new Favorite();
            favorite.setUser(userRepository.getById(userId));
            favorite.setTargetType(type);
            favorite.setTargetId(playlistId);
            favoriteRepository.save(favorite);
        }
    }

    public List<Playlist> getPopularPlaylists(int limit) {
        return playlistRepository.findTopPopular(PageRequest.of(0, limit));
    }
}
