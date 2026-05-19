package com.heritage.service;

import com.heritage.entity.Heritage;
import com.heritage.entity.UserBehaviorLog;
import com.heritage.repository.HeritageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecommendationService {

    @Autowired
    private HeritageRepository heritageRepository;

    public List<Heritage> recommendByUserBehavior(Long userId, List<UserBehaviorLog> behaviorLogs) {
        Map<String, Integer> categoryPreference = new HashMap<>();
        Map<String, Integer> dynastyPreference = new HashMap<>();

        for (UserBehaviorLog log : behaviorLogs) {
            if (!userId.equals(log.getUserId())) {
                continue;
            }

            String targetType = log.getTargetType();
            if ("heritage".equals(targetType)) {
                Heritage heritage = heritageRepository.findById(log.getTargetId()).orElse(null);
                if (heritage != null) {
                    int weight = calculateWeight(log.getBehaviorType());
                    categoryPreference.merge(heritage.getCategory(), weight, Integer::sum);
                    dynastyPreference.merge(heritage.getDynasty(), weight, Integer::sum);
                }
            }
        }

        List<String> topCategories = getTopKeys(categoryPreference, 3);
        List<String> topDynasties = getTopKeys(dynastyPreference, 2);

        List<Heritage> recommendations = new ArrayList<>();
        for (String category : topCategories) {
            List<Heritage> categoryHeritages = heritageRepository.findByCategory(category, null).getContent();
            recommendations.addAll(categoryHeritages);
        }

        for (String dynasty : topDynasties) {
            List<Heritage> dynastyHeritages = heritageRepository.findByDynasty(dynasty, null).getContent();
            recommendations.addAll(dynastyHeritages);
        }

        recommendations = recommendations.stream()
                .distinct()
                .limit(10)
                .collect(Collectors.toList());

        log.info("为用户 {} 生成 {} 条推荐", userId, recommendations.size());
        return recommendations;
    }

    private int calculateWeight(String behaviorType) {
        switch (behaviorType) {
            case "purchase":
                return 10;
            case "collect":
                return 8;
            case "view":
                return 3;
            case "click":
                return 1;
            default:
                return 1;
        }
    }

    private List<String> getTopKeys(Map<String, Integer> map, int n) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Heritage> recommendSimilarHeritages(Long heritageId, int limit) {
        Heritage target = heritageRepository.findById(heritageId).orElse(null);
        if (target == null) {
            return Collections.emptyList();
        }

        List<Heritage> allHeritages = heritageRepository.findAll();
        Map<Heritage, Double> similarityScores = new HashMap<>();

        for (Heritage h : allHeritages) {
            if (h.getId().equals(heritageId)) {
                continue;
            }
            double score = calculateSimilarity(target, h);
            similarityScores.put(h, score);
        }

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Heritage, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateSimilarity(Heritage h1, Heritage h2) {
        double score = 0.0;

        if (Objects.equals(h1.getCategory(), h2.getCategory())) {
            score += 0.3;
        }
        if (Objects.equals(h1.getSubCategory(), h2.getSubCategory())) {
            score += 0.2;
        }
        if (Objects.equals(h1.getDynasty(), h2.getDynasty())) {
            score += 0.3;
        }
        if (Objects.equals(h1.getMaterial(), h2.getMaterial())) {
            score += 0.1;
        }
        if (Objects.equals(h1.getLevel(), h2.getLevel())) {
            score += 0.1;
        }

        return score;
    }

    public Map<String, Object> getUserPreferenceProfile(Long userId, List<UserBehaviorLog> behaviorLogs) {
        Map<String, Object> profile = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Integer> dynastyCount = new HashMap<>();

        int viewCount = 0, clickCount = 0, collectCount = 0, purchaseCount = 0;

        for (UserBehaviorLog log : behaviorLogs) {
            if (!userId.equals(log.getUserId())) {
                continue;
            }

            switch (log.getBehaviorType()) {
                case "view":
                    viewCount++;
                    break;
                case "click":
                    clickCount++;
                    break;
                case "collect":
                    collectCount++;
                    break;
                case "purchase":
                    purchaseCount++;
                    break;
            }

            if ("heritage".equals(log.getTargetType())) {
                Heritage heritage = heritageRepository.findById(log.getTargetId()).orElse(null);
                if (heritage != null) {
                    categoryCount.merge(heritage.getCategory(), 1, Integer::sum);
                    dynastyCount.merge(heritage.getDynasty(), 1, Integer::sum);
                }
            }
        }

        profile.put("userId", userId);
        profile.put("viewCount", viewCount);
        profile.put("clickCount", clickCount);
        profile.put("collectCount", collectCount);
        profile.put("purchaseCount", purchaseCount);
        profile.put("topCategories", getTopKeys(categoryCount, 5));
        profile.put("topDynasties", getTopKeys(dynastyCount, 5));

        return profile;
    }
}
