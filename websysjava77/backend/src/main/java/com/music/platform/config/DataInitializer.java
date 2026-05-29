package com.music.platform.config;

import com.music.platform.entity.*;
import com.music.platform.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final CategoryRepository categoryRepository;
    private final MusicCategoryRepository musicCategoryRepository;
    private final TagRepository tagRepository;
    private final MusicTagRepository musicTagRepository;
    private final CopyrightRepository copyrightRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistMusicRepository playlistMusicRepository;
    private final UserBehaviorRepository userBehaviorRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            if (userRepository.count() > 0) {
                log.info("Data already initialized, skipping...");
                return;
            }
            log.info("Initializing test data...");
            initUsers();
            initCategories();
            initTags();
            initMusic();
            initCopyrights();
            initPlaylists();
            initUserBehaviors();
            initSearchHistory();
            log.info("Test data initialization completed!");
        };
    }

    private void initUsers() {
        String[][] userData = {
                {"musiclover", "123456", "music@example.com", "yin yue ai hao zhe"},
                {"rockfan", "123456", "rock@example.com", "yao gun qing nian"},
                {"classical", "123456", "classic@example.com", "gu dian yue mi"},
                {"popsinger", "123456", "pop@example.com", "liu xing ge shou"},
                {"djremix", "123456", "dj@example.com", "DJ da ren"},
                {"indieartist", "123456", "indie@example.com", "du li yin yue ren"},
                {"hiphophead", "123456", "hiphop@example.com", "xi ha kuang ren"},
                {"jazzsoul", "123456", "jazz@example.com", "jue shi ling hun"},
        };
        for (String[] data : userData) {
            User user = new User();
            user.setUsername(data[0]);
            user.setPassword(data[1]);
            user.setEmail(data[2]);
            user.setNickname(data[3]);
            user.setAvatar("/uploads/avatars/default.png");
            userRepository.save(user);
        }
    }

    private void initCategories() {
        String[][] genreData = {
                {"Pop", "genre", "pop"}, {"Rock", "genre", "rock"}, {"Classical", "genre", "classical"},
                {"Electronic", "genre", "electronic"}, {"HipHop", "genre", "hiphop"}, {"Jazz", "genre", "jazz"},
                {"Folk", "genre", "folk"}, {"RnB", "genre", "rnb"}, {"Metal", "genre", "metal"},
                {"Blues", "genre", "blues"}
        };
        for (String[] data : genreData) {
            Category cat = new Category();
            cat.setName(data[0]);
            cat.setType(data[1]);
            cat.setIcon(data[2]);
            categoryRepository.save(cat);
        }

        String[][] langData = {
                {"Chinese", "language", "cn"}, {"English", "language", "en"}, {"Japanese", "language", "jp"},
                {"Korean", "language", "kr"}, {"French", "language", "fr"}, {"Cantonese", "language", "hk"}
        };
        for (String[] data : langData) {
            Category cat = new Category();
            cat.setName(data[0]);
            cat.setType(data[1]);
            cat.setIcon(data[2]);
            categoryRepository.save(cat);
        }

        String[][] sceneData = {
                {"Workout", "scene", "workout"}, {"Sleep", "scene", "sleep"}, {"Work", "scene", "work"},
                {"Study", "scene", "study"}, {"Party", "scene", "party"}, {"Driving", "scene", "driving"},
                {"Relax", "scene", "relax"}, {"Meditation", "scene", "meditation"}
        };
        for (String[] data : sceneData) {
            Category cat = new Category();
            cat.setName(data[0]);
            cat.setType(data[1]);
            cat.setIcon(data[2]);
            categoryRepository.save(cat);
        }
    }

    private void initTags() {
        String[] tagNames = {
                "healing", "sad", "energetic", "light-music", "catchy", "classic",
                "viral", "OST", "game-music", "cover", "original", "live",
                "remix", "soundtrack", "ACG", "trending", "KTV", "running",
                "sleep-aid", "cafe"
        };
        for (String name : tagNames) {
            Tag tag = new Tag();
            tag.setName(name);
            tag.setUserId(1L);
            tagRepository.save(tag);
        }
    }

    private void initMusic() {
        String[][] musicData = {
                {"Sunny Day", "Jay Chou", "Ye Hui Mei", "mp3", "Chinese", "Pop"},
                {"Rice Fragrance", "Jay Chou", "Mo Jie Zuo", "mp3", "Chinese", "Pop"},
                {"Qi Li Xiang", "Jay Chou", "Qi Li Xiang", "mp3", "Chinese", "Pop"},
                {"Glorious Years", "Beyond", "Destiny Party", "mp3", "Chinese", "Rock"},
                {"Boundless Oceans", "Beyond", "Rock & Roll", "mp3", "Chinese", "Rock"},
                {"Hotel California", "Eagles", "Hotel California", "mp3", "English", "Rock"},
                {"Bohemian Rhapsody", "Queen", "A Night at the Opera", "mp3", "English", "Rock"},
                {"Moonlight Sonata", "Beethoven", "Classical Collection", "mp3", "French", "Classical"},
                {"Four Seasons Spring", "Vivaldi", "Four Seasons", "mp3", "French", "Classical"},
                {"Canon", "Pachelbel", "Classical Hits", "mp3", "French", "Classical"},
                {"Faded", "Alan Walker", "Different World", "mp3", "English", "Electronic"},
                {"Closer", "The Chainsmokers", "Collage", "mp3", "English", "Electronic"},
                {"Lose Yourself", "Eminem", "8 Mile", "mp3", "English", "HipHop"},
                {"Chengdu", "Zhao Lei", "Bu Fa Zhang Da", "mp3", "Chinese", "Folk"},
                {"South Mountain South", "Ma Di", "Gu Dao", "mp3", "Chinese", "Folk"},
                {"Take Five", "Dave Brubeck", "Time Out", "mp3", "English", "Jazz"},
                {"Fly Me to the Moon", "Frank Sinatra", "It Might as Well Be Swing", "mp3", "English", "Jazz"},
                {"Light Years Away", "G.E.M.", "Light Years Away", "mp3", "Chinese", "Pop"},
                {"The Wind Rises", "Mai La Jiao", "The Wind Rises", "mp3", "Chinese", "Pop"},
                {"Ordinary Road", "Pu Shu", "Orion", "mp3", "Chinese", "Folk"},
                {"Nocturne", "Jay Chou", "November Chopin", "mp3", "Chinese", "Pop"},
                {"Confession Balloon", "Jay Chou", "Bedtime Stories", "mp3", "Chinese", "Pop"},
                {"Shape of You", "Ed Sheeran", "Divide", "mp3", "English", "Pop"},
                {"Blinding Lights", "The Weeknd", "After Hours", "mp3", "English", "Pop"},
                {"Levitating", "Dua Lipa", "Future Nostalgia", "mp3", "English", "Pop"},
                {"Lone Hero", "Eason Chan", "Lone Hero", "mp3", "Chinese", "Pop"},
                {"Mohe Ballroom", "Liu Shuang", "Mohe Ballroom", "mp3", "Chinese", "Folk"},
                {"Something Just Like This", "Coldplay", "Kaleidoscope", "mp3", "English", "Electronic"},
                {"Spring Breeze", "Lu Xian Sen", "Spring Breeze", "mp3", "Chinese", "Folk"},
                {"DDU-DU DDU-DU", "BLACKPINK", "SQUARE UP", "mp3", "Korean", "Pop"},
        };

        List<Category> allCategories = categoryRepository.findAll();
        Map<String, Long> categoryMap = new HashMap<>();
        for (Category cat : allCategories) {
            categoryMap.put(cat.getName(), cat.getId());
        }

        List<Tag> allTags = tagRepository.findAll();
        Random random = new Random(42);

        long[] playCounts = {
                98000, 95000, 92000, 88000, 85000,
                80000, 78000, 75000, 70000, 68000,
                65000, 63000, 60000, 58000, 55000,
                52000, 50000, 48000, 45000, 43000,
                40000, 38000, 35000, 33000, 30000,
                28000, 25000, 22000, 20000, 18000
        };

        long[] likeCounts = {
                15000, 14000, 13000, 12000, 11000,
                10000, 9500, 9000, 8500, 8000,
                7500, 7000, 6500, 6000, 5500,
                5000, 4800, 4500, 4200, 4000,
                3800, 3500, 3200, 3000, 2800,
                2500, 2200, 2000, 1800, 1500
        };

        String[] sampleLyrics = {
                "The little yellow flower from the year I was born\nThe swing from childhood swaying in my memory",
                "If you have too many complaints about this world\nIf you fall and dare not move forward",
                "The rain falls all night, my love overflows like rainwater\nLeaves in the yard, thick with my thoughts",
                "The bell rings, the signal to go home\nIn his life, it seems a bit sighing",
                "Forgive me for my unbridled love of freedom\nAlso afraid that one day I might fall",
                "Welcome to the Hotel California\nSuch a lovely place",
                "Is this the real life? Is this just fantasy?",
                "Moonlight on the piano\nThe night is as gentle as water",
                "Spring is here, everything comes back to life\nBirds are singing, flowers are blooming",
                "The melody of Canon echoes in the heart",
                "You were the shadow to my light\nDid you feel us?",
                "Hey, I was doing just fine before I met you",
                "You better lose yourself in the music",
                "What makes me cry is more than last night's wine\nWhat makes me reluctant is more than your gentleness",
                "You are in the southern sun, snowflakes flying\nI am in the northern cold night, four seasons like spring",
                "Just take five\nAnd relax a while",
                "Fly me to the moon\nLet me play among the stars",
                "Destiny made us meet beyond the chaos\nFate wants us to love in danger",
                "I once found it hard to escape the vastness of the world\nAlso indulged in its dreamwords",
                "I once crossed mountains and seas\nAlso passed through crowds of people",
        };

        for (int i = 0; i < musicData.length; i++) {
            String[] data = musicData[i];
            Music music = new Music();
            music.setTitle(data[0]);
            music.setArtist(data[1]);
            music.setAlbum(data[2]);
            music.setFormat(data[3]);
            music.setFilePath("/uploads/audio/sample_" + (i + 1) + ".mp3");
            music.setCoverPath("/uploads/covers/cover_" + (i + 1) + ".jpg");
            music.setFileSize(3000000L + random.nextInt(5000000));
            music.setDuration(180.0 + random.nextInt(180));
            music.setUserId((long) (random.nextInt(8) + 1));
            music.setPlayCount(playCounts[i] + random.nextInt(5000));
            music.setLikeCount(likeCounts[i] + random.nextInt(500));
            music.setCollectCount((long) (likeCounts[i] / 2 + random.nextInt(200)));
            music.setCreatedAt(LocalDateTime.now().minusDays(30 - i));
            if (i < sampleLyrics.length) {
                music.setLyrics(sampleLyrics[i]);
            }
            Music saved = musicRepository.save(music);

            Long langCatId = categoryMap.get(data[4]);
            Long genreCatId = categoryMap.get(data[5]);
            if (langCatId != null) {
                MusicCategory mc = new MusicCategory();
                mc.setMusicId(saved.getId());
                mc.setCategoryId(langCatId);
                musicCategoryRepository.save(mc);
            }
            if (genreCatId != null) {
                MusicCategory mc = new MusicCategory();
                mc.setMusicId(saved.getId());
                mc.setCategoryId(genreCatId);
                musicCategoryRepository.save(mc);
            }

            int tagCount = 2 + random.nextInt(3);
            Set<Integer> usedTagIndices = new HashSet<>();
            for (int t = 0; t < tagCount; t++) {
                int tagIdx;
                do {
                    tagIdx = random.nextInt(allTags.size());
                } while (usedTagIndices.contains(tagIdx));
                usedTagIndices.add(tagIdx);

                MusicTag mt = new MusicTag();
                mt.setMusicId(saved.getId());
                mt.setTagId(allTags.get(tagIdx).getId());
                musicTagRepository.save(mt);
            }

            if (i % 3 == 0) {
                String[] scenes = {"Workout", "Sleep", "Work", "Study", "Party", "Driving", "Relax", "Meditation"};
                String scene = scenes[random.nextInt(scenes.length)];
                Long sceneCatId = categoryMap.get(scene);
                if (sceneCatId != null) {
                    MusicCategory mc = new MusicCategory();
                    mc.setMusicId(saved.getId());
                    mc.setCategoryId(sceneCatId);
                    musicCategoryRepository.save(mc);
                }
            }
        }
    }

    private void initCopyrights() {
        List<Music> allMusic = musicRepository.findAll();
        String[] types = {"original", "cover", "licensed", "public-domain"};
        String[] declarations = {
                "This is an original work, all rights reserved by the author",
                "This work is used with authorization from the original author",
                "This work is published under CC license",
                "This work has entered the public domain"
        };
        Random random = new Random(42);

        for (int i = 0; i < allMusic.size(); i++) {
            Copyright copyright = new Copyright();
            copyright.setMusicId(allMusic.get(i).getId());
            copyright.setUserId(allMusic.get(i).getUserId());
            int typeIdx = i < 20 ? 0 : random.nextInt(types.length);
            copyright.setType(types[typeIdx]);
            copyright.setDeclaration(declarations[typeIdx]);
            copyright.setIsOriginal(typeIdx == 0);
            if (typeIdx == 0) {
                copyright.setOriginalCert("CERT-" + System.currentTimeMillis() + "-" + allMusic.get(i).getId());
            }
            copyright.setReportCount(0);
            copyright.setStatus("active");
            copyrightRepository.save(copyright);
        }
    }

    private void initPlaylists() {
        String[][] playlistData = {
                {"Workout Energy", "Burn calories with these beats", "scene"},
                {"Late Night Lullaby", "Calm your thoughts", "scene"},
                {"Focus Mode", "Background music for productivity", "scene"},
                {"Party Night", "Get the party going", "scene"},
                {"Road Trip", "Great songs for the road", "scene"},
                {"New Releases", "Latest hit singles", "chart"},
                {"Hot Chart TOP", "Most played songs", "chart"},
                {"Rising Chart", "Fast climbing songs", "chart"},
        };

        List<Music> allMusic = musicRepository.findAll();
        Random random = new Random(42);

        for (int i = 0; i < playlistData.length; i++) {
            Playlist playlist = new Playlist();
            playlist.setName(playlistData[i][0]);
            playlist.setDescription(playlistData[i][1]);
            playlist.setType(playlistData[i][2]);
            playlist.setUserId((long) (i % 8 + 1));
            playlist.setCoverPath("/uploads/covers/playlist_" + (i + 1) + ".jpg");
            playlist.setPlayCount((long) (10000 + random.nextInt(50000)));
            playlist = playlistRepository.save(playlist);

            int musicCount = 5 + random.nextInt(6);
            Set<Integer> usedIndices = new HashSet<>();
            for (int j = 0; j < musicCount; j++) {
                int musicIdx;
                do {
                    musicIdx = random.nextInt(allMusic.size());
                } while (usedIndices.contains(musicIdx));
                usedIndices.add(musicIdx);

                PlaylistMusic pm = new PlaylistMusic();
                pm.setPlaylistId(playlist.getId());
                pm.setMusicId(allMusic.get(musicIdx).getId());
                pm.setPosition(j);
                playlistMusicRepository.save(pm);
            }
        }
    }

    private void initUserBehaviors() {
        List<Music> allMusic = musicRepository.findAll();
        Random random = new Random(42);

        for (long userId = 1; userId <= 8; userId++) {
            int behaviorCount = 10 + random.nextInt(15);
            Set<String> existing = new HashSet<>();
            for (int i = 0; i < behaviorCount; i++) {
                int musicIdx = random.nextInt(allMusic.size());
                String[] types = {"play", "like", "collect"};
                String type = types[random.nextInt(types.length)];
                String key = userId + "-" + musicIdx + "-" + type;
                if (existing.contains(key)) continue;
                existing.add(key);

                UserBehavior behavior = new UserBehavior();
                behavior.setUserId(userId);
                behavior.setMusicId(allMusic.get(musicIdx).getId());
                behavior.setType(type);
                userBehaviorRepository.save(behavior);
            }
        }
    }

    private void initSearchHistory() {
        String[] keywords = {
                "Jay Chou", "Sunny Day", "Beyond", "Classical", "Workout",
                "Sleep", "Pop", "Rock", "Electronic", "Folk",
                "Faded", "Jazz", "Korean", "Party", "Classic",
                "Jay Chou", "Sunny Day", "Beyond", "Pop", "Jay Chou"
        };
        Random random = new Random(42);
        for (String keyword : keywords) {
            SearchHistory history = new SearchHistory();
            history.setUserId((long) (random.nextInt(8) + 1));
            history.setKeyword(keyword);
            searchHistoryRepository.save(history);
        }
    }
}
