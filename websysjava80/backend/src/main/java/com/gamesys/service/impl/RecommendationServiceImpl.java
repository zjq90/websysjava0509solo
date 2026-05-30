package com.gamesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gamesys.common.GameStatus;
import com.gamesys.entity.Game;
import com.gamesys.entity.Recommendation;
import com.gamesys.mapper.GameMapper;
import com.gamesys.mapper.RecommendationMapper;
import com.gamesys.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String RECOMMEND_CACHE_PREFIX = "recommend:";

    @Override
    @SuppressWarnings("unchecked")
    public List<Recommendation> getRecommendations(String type, Long categoryId) {
        String cacheKey = RECOMMEND_CACHE_PREFIX + type + ":" + (categoryId != null ? categoryId : "all");
        List<Recommendation> cached = (List<Recommendation>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }

        LambdaQueryWrapper<Recommendation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Recommendation::getType, type);
        if (categoryId != null) {
            wrapper.eq(Recommendation::getCategoryId, categoryId);
        }
        wrapper.eq(Recommendation::getStatus, 1);
        wrapper.orderByDesc(Recommendation::getSortWeight);

        List<Recommendation> list = recommendationMapper.selectList(wrapper);
        redisTemplate.opsForValue().set(cacheKey, list, 1, TimeUnit.HOURS);
        return list;
    }

    @Override
    public void saveRecommendation(Recommendation recommendation) {
        validateGameStatus(recommendation.getGameId());
        recommendation.setStatus(1);
        recommendationMapper.insert(recommendation);
        clearCache();
    }

    @Override
    public void updateRecommendation(Recommendation recommendation) {
        validateGameStatus(recommendation.getGameId());
        recommendationMapper.updateById(recommendation);
        clearCache();
    }

    @Override
    public void deleteRecommendation(Long id) {
        recommendationMapper.deleteById(id);
        clearCache();
    }

    @Override
    public List<Recommendation> getHomeRecommendations() {
        return getRecommendations("home", null);
    }

    @Override
    public List<Recommendation> getCategoryRecommendations(Long categoryId) {
        return getRecommendations("category", categoryId);
    }

    @Override
    public Recommendation getPopupRecommendation() {
        List<Recommendation> list = getRecommendations("popup", null);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Scheduled(cron = "0 0 * * * ?")
    public void validateRecommendations() {
        List<Recommendation> recommendations = recommendationMapper.selectList(null);
        LocalDateTime now = LocalDateTime.now();

        for (Recommendation rec : recommendations) {
            boolean invalid = false;

            if (rec.getEndTime() != null && rec.getEndTime().isBefore(now)) {
                invalid = true;
            }

            if (rec.getStartTime() != null && rec.getStartTime().isAfter(now)) {
                continue;
            }

            Game game = gameMapper.selectById(rec.getGameId());
            if (game == null || !GameStatus.ONLINE.getCode().equals(game.getStatus())) {
                invalid = true;
            }

            if (invalid && rec.getStatus() == 1) {
                rec.setStatus(0);
                recommendationMapper.updateById(rec);
            }
        }

        clearCache();
    }

    private void validateGameStatus(Long gameId) {
        Game game = gameMapper.selectById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("游戏不存在");
        }
        if (!GameStatus.ONLINE.getCode().equals(game.getStatus())) {
            throw new IllegalArgumentException("只有已上线的游戏才能被推荐");
        }
    }

    private void clearCache() {
        redisTemplate.delete(redisTemplate.keys(RECOMMEND_CACHE_PREFIX + "*"));
    }
}
