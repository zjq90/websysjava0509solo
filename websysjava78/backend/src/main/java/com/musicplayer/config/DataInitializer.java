package com.musicplayer.config;

import com.musicplayer.entity.Lyrics;
import com.musicplayer.entity.Music;
import com.musicplayer.entity.User;
import com.musicplayer.repository.LyricsRepository;
import com.musicplayer.repository.MusicRepository;
import com.musicplayer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MusicRepository musicRepository;
    private final UserRepository userRepository;
    private final LyricsRepository lyricsRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${music.upload-dir}")
    private String uploadDir;

    @Override
    public void run(String... args) {
        createUploadDirectory();
        createDemoAudioFiles();
        
        if (userRepository.count() == 0) {
            initUsers();
        }
        if (musicRepository.count() == 0) {
            initMusic();
        }
    }

    private void createUploadDirectory() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            log.info("Upload directory created: {}", uploadDir);
        } catch (IOException e) {
            log.error("Failed to create upload directory", e);
        }
    }

    private void createDemoAudioFiles() {
        try {
            createSilentMp3(Paths.get(uploadDir, "demo_128.mp3"), 3);
            createSilentMp3(Paths.get(uploadDir, "demo_320.mp3"), 8);
            createSilentFlac(Paths.get(uploadDir, "demo_flac.flac"), 25);
            log.info("Demo audio files created");
        } catch (IOException e) {
            log.error("Failed to create demo audio files", e);
        }
    }

    private void createSilentMp3(Path path, int approxSizeMB) throws IOException {
        createPlaceholderFile(path, approxSizeMB);
    }

    private void createSilentFlac(Path path, int approxSizeMB) throws IOException {
        createPlaceholderFile(path, approxSizeMB);
    }

    private void createPlaceholderFile(Path path, int approxSizeMB) throws IOException {
        if (Files.exists(path)) {
            return;
        }
        int fileSize = approxSizeMB * 1024 * 1024;
        try (var out = Files.newOutputStream(path)) {
            byte[] buffer = new byte[8192];
            int remaining = fileSize;
            while (remaining > 0) {
                int toWrite = Math.min(buffer.length, remaining);
                out.write(buffer, 0, toWrite);
                remaining -= toWrite;
            }
        }
    }

    private void initUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@example.com");
        admin.setNickname("管理员");
        admin.setRole(User.UserRole.ADMIN);
        userRepository.save(admin);

        User premium = new User();
        premium.setUsername("vip");
        premium.setPassword(passwordEncoder.encode("vip123"));
        premium.setEmail("vip@example.com");
        premium.setNickname("VIP用户");
        premium.setRole(User.UserRole.PREMIUM);
        userRepository.save(premium);

        User free = new User();
        free.setUsername("user");
        free.setPassword(passwordEncoder.encode("user123"));
        free.setEmail("user@example.com");
        free.setNickname("普通用户");
        free.setRole(User.UserRole.FREE);
        userRepository.save(free);
    }

    private void initMusic() {
        List<Music> musicList = new ArrayList<>();

        String[][] sampleMusic = {
            {"夜曲", "周杰伦", "十一月的萧邦", "流行", "265", "false"},
            {"稻香", "周杰伦", "魔杰座", "流行", "223", "false"},
            {"晴天", "周杰伦", "叶惠美", "流行", "269", "false"},
            {"七里香", "周杰伦", "七里香", "流行", "299", "false"},
            {"青花瓷", "周杰伦", "我很忙", "流行", "239", "true"},
            {"告白气球", "周杰伦", "周杰伦的床边故事", "流行", "215", "true"},
            {"演员", "薛之谦", "绅士", "流行", "257", "false"},
            {"丑八怪", "薛之谦", "意外", "流行", "258", "false"},
            {"刚刚好", "薛之谦", "初学者", "流行", "245", "true"},
            {"富士山下", "陈奕迅", "What's Going On...?", "粤语", "278", "false"},
            {"十年", "陈奕迅", "黑白灰", "流行", "205", "false"},
            {"浮夸", "陈奕迅", "U87", "粤语", "296", "true"},
            {"光年之外", "邓紫棋", "光年之外", "流行", "235", "false"},
            {"泡沫", "邓紫棋", "Xposed", "流行", "276", "false"},
            {"再见", "邓紫棋", "新的心跳", "流行", "234", "true"},
            {"起风了", "买辣椒也用券", "起风了", "流行", "326", "false"},
            {"漠河舞厅", "柳爽", "漠河舞厅", "流行", "289", "false"},
            {"孤勇者", "陈奕迅", "孤勇者", "流行", "262", "true"},
            {"稻香", "周杰伦", "魔杰座", "流行", "223", "false"},
            {"七里香", "周杰伦", "七里香", "流行", "299", "false"}
        };

        String sampleLrc = "[00:00.00]示例歌词\n" +
                "[00:05.00]这是一首示例歌曲\n" +
                "[00:10.00]用于演示歌词同步功能\n" +
                "[00:15.00]播放器会自动高亮当前歌词\n" +
                "[00:20.00]让您跟着唱更方便\n" +
                "[00:25.00]支持LRC格式歌词\n" +
                "[00:30.00]点击歌词可以跳转到对应时间\n" +
                "[00:35.00]享受音乐，享受生活\n" +
                "[00:40.00]音乐是人类最美的语言\n" +
                "[00:45.00]它能够跨越国界\n" +
                "[00:50.00]连接每一颗热爱生活的心\n" +
                "[00:55.00]让我们一起沉浸在音乐的海洋中\n" +
                "[01:00.00]感受每一个音符的跳动\n" +
                "[01:05.00]体验每一段旋律的情感\n" +
                "[01:10.00]这就是音乐的魔力\n" +
                "[01:15.00]感谢您使用我们的音乐播放器";

        for (int i = 0; i < sampleMusic.length; i++) {
            String[] info = sampleMusic[i];
            Music music = new Music();
            music.setTitle(info[0]);
            music.setArtist(info[1]);
            music.setAlbum(info[2]);
            music.setGenre(info[3]);
            music.setDuration(Integer.parseInt(info[4]));
            music.setIsPremium(Boolean.parseBoolean(info[5]));
            music.setPlayCount((int) (Math.random() * 10000));
            music.setDownloadCount((int) (Math.random() * 1000));
            music.setFilePath128("demo_128.mp3");
            music.setFilePath320("demo_320.mp3");
            music.setFileSize128(1024L * 1024 * 3);
            music.setFileSize320(1024L * 1024 * 8);

            if (music.getIsPremium()) {
                music.setFilePathFlac("demo_flac.flac");
                music.setFileSizeFlac(1024L * 1024 * 25);
            }

            Music saved = musicRepository.save(music);
            musicList.add(saved);

            Lyrics lyrics = new Lyrics();
            lyrics.setMusic(saved);
            lyrics.setLrcContent(sampleLrc);
            lyrics.setHasSync(true);
            lyricsRepository.save(lyrics);
        }
    }
}
