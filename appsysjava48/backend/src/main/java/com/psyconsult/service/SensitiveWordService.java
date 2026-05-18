package com.psyconsult.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SensitiveWordService {

    private static final List<String> CRISIS_WORDS = Arrays.asList(
            "自杀", "想死", "不想活", "结束生命", "活不下去",
            "自残", "自伤", "自杀倾向", "自我伤害", "不想活了",
            "跳楼", "割腕", "喝农药", "上吊", "烧炭"
    );

    public boolean containsCrisisWords(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        String lowerText = text.toLowerCase();
        return CRISIS_WORDS.stream().anyMatch(lowerText::contains);
    }

    public String extractTriggerWords(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        String lowerText = text.toLowerCase();
        StringBuilder triggers = new StringBuilder();
        for (String word : CRISIS_WORDS) {
            if (lowerText.contains(word)) {
                if (triggers.length() > 0) {
                    triggers.append(",");
                }
                triggers.append(word);
            }
        }
        return triggers.toString();
    }

    public String determineAlertLevel(String text) {
        if (containsCrisisWords(text)) {
            String triggers = extractTriggerWords(text);
            if (triggers.contains("自杀") || triggers.contains("跳楼") || 
                triggers.contains("割腕") || triggers.contains("喝农药") ||
                triggers.contains("上吊") || triggers.contains("烧炭")) {
                return "critical";
            }
            return "high";
        }
        return "low";
    }
}
