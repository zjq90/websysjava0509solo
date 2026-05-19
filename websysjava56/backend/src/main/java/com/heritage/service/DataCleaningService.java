package com.heritage.service;

import com.heritage.entity.Heritage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataCleaningService {

    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");

    public List<Heritage> removeDuplicates(List<Heritage> heritageList) {
        Set<String> seenCodes = new HashSet<>();
        List<Heritage> uniqueList = new ArrayList<>();

        for (Heritage heritage : heritageList) {
            String key = heritage.getHeritageCode() != null ? 
                    heritage.getHeritageCode() : 
                    heritage.getName() + "_" + heritage.getDynasty();

            if (!seenCodes.contains(key)) {
                seenCodes.add(key);
                uniqueList.add(heritage);
            } else {
                log.info("移除重复文物: {}", heritage.getName());
            }
        }

        log.info("去重完成，原数据{}条，去重后{}条", heritageList.size(), uniqueList.size());
        return uniqueList;
    }

    public Heritage correctData(Heritage heritage) {
        if (heritage.getName() != null) {
            heritage.setName(heritage.getName().trim());
        }

        if (heritage.getDynasty() != null) {
            heritage.setDynasty(normalizeDynasty(heritage.getDynasty()));
        }

        if (heritage.getCategory() != null) {
            heritage.setCategory(normalizeCategory(heritage.getCategory()));
        }

        if (heritage.getOrigin() != null) {
            heritage.setOrigin(heritage.getOrigin().trim());
        }

        if (heritage.getDescription() != null) {
            heritage.setDescription(cleanDescription(heritage.getDescription()));
        }

        int qualityScore = calculateQualityScore(heritage);
        heritage.setQualityScore(qualityScore);

        return heritage;
    }

    private String normalizeDynasty(String dynasty) {
        dynasty = dynasty.trim();
        if (dynasty.contains("明") && !dynasty.equals("明代")) {
            return "明代";
        }
        if (dynasty.contains("清") && !dynasty.equals("清代")) {
            return "清代";
        }
        if (dynasty.contains("宋") && !dynasty.equals("宋代")) {
            return "宋代";
        }
        if (dynasty.contains("元") && !dynasty.equals("元代")) {
            return "元代";
        }
        if (dynasty.contains("唐") && !dynasty.equals("唐代")) {
            return "唐代";
        }
        return dynasty;
    }

    private String normalizeCategory(String category) {
        category = category.trim();
        if (category.contains("瓷")) {
            return "瓷器";
        }
        if (category.contains("玉")) {
            return "玉器";
        }
        if (category.contains("青铜") || category.contains("铜")) {
            return "青铜器";
        }
        if (category.contains("书") || category.contains("画")) {
            return "书画";
        }
        return category;
    }

    private String cleanDescription(String description) {
        description = description.trim();
        description = description.replaceAll("\\s+", " ");
        description = description.replaceAll("[\\r\\n]+", " ");
        return description;
    }

    private int calculateQualityScore(Heritage heritage) {
        int score = 0;
        int maxScore = 100;

        if (heritage.getName() != null && !heritage.getName().isEmpty()) {
            score += 15;
        }
        if (heritage.getHeritageCode() != null && !heritage.getHeritageCode().isEmpty()) {
            score += 15;
        }
        if (heritage.getCategory() != null && !heritage.getCategory().isEmpty()) {
            score += 10;
        }
        if (heritage.getDynasty() != null && !heritage.getDynasty().isEmpty()) {
            score += 10;
        }
        if (heritage.getMaterial() != null && !heritage.getMaterial().isEmpty()) {
            score += 10;
        }
        if (heritage.getSize() != null && !heritage.getSize().isEmpty()) {
            score += 10;
        }
        if (heritage.getDescription() != null && heritage.getDescription().length() > 50) {
            score += 15;
        } else if (heritage.getDescription() != null) {
            score += 5;
        }
        if (heritage.getOrigin() != null && !heritage.getOrigin().isEmpty()) {
            score += 10;
        }
        if (heritage.getLevel() != null && !heritage.getLevel().isEmpty()) {
            score += 5;
        }

        return Math.min(score, maxScore);
    }

    public List<Heritage> batchCorrect(List<Heritage> heritageList) {
        return heritageList.stream()
                .map(this::correctData)
                .collect(Collectors.toList());
    }

    public double calculateQualityRate(List<Heritage> heritageList) {
        if (heritageList == null || heritageList.isEmpty()) {
            return 0.0;
        }
        long highQualityCount = heritageList.stream()
                .filter(h -> h.getQualityScore() != null && h.getQualityScore() >= 80)
                .count();
        return (double) highQualityCount / heritageList.size() * 100;
    }
}
