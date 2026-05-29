package com.musicplatform.config;

import com.musicplatform.entity.*;
import com.musicplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("开始初始化测试数据...");

        User admin = createUser("admin", "管理员", "admin@music.com", "13800000000", User.Role.ADMIN);
        User artist1 = createUser("artist1", "周杰伦", "artist1@music.com", "13800000001", User.Role.ARTIST);
        User artist2 = createUser("artist2", "林俊杰", "artist2@music.com", "13800000002", User.Role.ARTIST);
        User user1 = createUser("user1", "小明", "user1@music.com", "13800000003", User.Role.USER);
        User user2 = createUser("user2", "小红", "user2@music.com", "13800000004", User.Role.USER);
        User vipUser = createUser("vipuser", "VIP用户", "vip@music.com", "13800000005", User.Role.VIP_USER);
        vipUser.setVip(true);
        vipUser.setVipExpireTime(LocalDateTime.now().plusMonths(3));
        userRepository.save(vipUser);

        createMusic("晴天", "周杰伦经典歌曲", artist1, "叶惠美", "https://picsum.photos/200/200?random=1", Arrays.asList("流行", "抒情"), true);
        createMusic("七里香", "周杰伦代表作", artist1, "七里香", "https://picsum.photos/200/200?random=2", Arrays.asList("流行", "经典"), true);
        createMusic("稻香", "励志歌曲", artist1, "魔杰座", "https://picsum.photos/200/200?random=3", Arrays.asList("流行", "励志"), true);
        createMusic("江南", "林俊杰经典", artist2, "第二天堂", "https://picsum.photos/200/200?random=4", Arrays.asList("流行", "抒情"), true);
        createMusic("曹操", "中国风歌曲", artist2, "曹操", "https://picsum.photos/200/200?random=5", Arrays.asList("流行", "中国风"), true);
        createMusic("小酒窝", "甜蜜对唱", artist2, "JJ陆", "https://picsum.photos/200/200?random=6", Arrays.asList("流行", "对唱"), true);
        createMusic("夜曲", "经典之作", artist1, "十一月的肖邦", "https://picsum.photos/200/200?random=7", Arrays.asList("流行", "R&B"), true);
        createMusic("青花瓷", "中国风巅峰", artist1, "我很忙", "https://picsum.photos/200/200?random=8", Arrays.asList("流行", "中国风"), false);
        createMusic("修炼爱情", "深情演绎", artist2, "因你而在", "https://picsum.photos/200/200?random=9", Arrays.asList("流行", "抒情"), false);
        createMusic("黑暗骑士", "独家合作", artist2, "因你而在", "https://picsum.photos/200/200?random=10", Arrays.asList("流行", "摇滚"), true);

        List<Music> allMusics = musicRepository.findAll();

        createPlaylist("我最喜欢的歌", "收藏的好听歌曲", user1, allMusics.subList(0, 5), Playlist.PrivacyLevel.PUBLIC);
        createPlaylist("工作学习BGM", "专注时听的音乐", user2, allMusics.subList(2, 7), Playlist.PrivacyLevel.PUBLIC);
        createPlaylist("周杰伦精选", "周杰伦的经典歌曲", user1, allMusics.subList(0, 4), Playlist.PrivacyLevel.PUBLIC);
        createPlaylist("私人收藏", "我的私密歌单", user2, allMusics.subList(4, 8), Playlist.PrivacyLevel.PRIVATE);

        createPost(user1, "今天发现了一首超好听的歌，分享给大家！", Post.PostType.TEXT, null, null, Post.Visibility.PUBLIC);
        createPost(user1, "分享周杰伦的《晴天》，百听不厌！", Post.PostType.MUSIC_SHARE, 1L, null, Post.Visibility.PUBLIC);
        createPost(artist1, "新歌《夜曲》已上线，感谢大家的支持！", Post.PostType.TEXT, null, null, Post.Visibility.PUBLIC);
        createPost(user2, "创建了新歌单，快来听听吧！", Post.PostType.PLAYLIST_SHARE, null, 2L, Post.Visibility.PUBLIC);

        System.out.println("测试数据初始化完成！");
        System.out.println("默认账号: admin / password");
        System.out.println("H2控制台: http://localhost:8080/h2-console");
    }

    private User createUser(String username, String nickname, String email, String phone, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail(email);
        user.setPhone(phone);
        user.setNickname(nickname);
        user.setAvatar("https://picsum.photos/100/100?random=" + username);
        user.setBio("这是" + nickname + "的个人简介");
        user.setRole(role);
        user.setVerified(true);
        return userRepository.save(user);
    }

    private Music createMusic(String title, String description, User artist, String album, String coverUrl, List<String> tags, boolean approved) {
        Music music = new Music();
        music.setTitle(title);
        music.setDescription(description);
        music.setArtist(artist);
        music.setArtistName(artist.getNickname());
        music.setAlbum(album);
        music.setCoverUrl(coverUrl);
        music.setAudioUrl("/audio/sample.mp3");
        music.setDuration("3:45");
        music.setTags(tags);
        music.setPlayCount((int) (Math.random() * 10000));
        music.setLikeCount((int) (Math.random() * 1000));
        music.setPrice(Math.random() > 0.5 ? 2.0 : 0.0);
        music.setStatus(approved ? Music.MusicStatus.APPROVED : Music.MusicStatus.PENDING);
        music.setPublishedAt(LocalDateTime.now());
        return musicRepository.save(music);
    }

    private Playlist createPlaylist(String name, String description, User creator, List<Music> musics, Playlist.PrivacyLevel privacy) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setCreator(creator);
        playlist.setCoverUrl("https://picsum.photos/300/300?random=" + name.hashCode());
        playlist.setPrivacy(privacy);
        playlist.setMusics(musics);
        playlist.setPlayCount((int) (Math.random() * 1000));
        return playlistRepository.save(playlist);
    }

    private Post createPost(User user, String content, Post.PostType type, Long musicId, Long playlistId, Post.Visibility visibility) {
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setType(type);
        post.setMusicId(musicId);
        post.setPlaylistId(playlistId);
        post.setVisibility(visibility);
        post.setLikeCount((int) (Math.random() * 100));
        return postRepository.save(post);
    }
}
