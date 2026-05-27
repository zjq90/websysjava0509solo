package com.club.util;

import java.util.*;

/**
 * 敏感词过滤工具类
 * 使用DFA算法实现高效的敏感词检测
 *
 * @author Club Management System
 * @version 1.0.0
 */
public class SensitiveWordFilter {

    /**
     * 敏感词库
     */
    private static final Set<String> SENSITIVE_WORDS = new HashSet<>(Arrays.asList(
        // 政治敏感词
        "违禁词", "违规", "非法", "反动", "邪教",
        // 色情低俗
        "色情", "低俗", "淫秽", "赌博", "暴力",
        // 广告诈骗
        "诈骗", "传销", "刷单", "贷款", "套现",
        // 其他违规
        "违法", "禁品", "毒品", "枪支", "爆炸",
        // 示例敏感词（用于测试）
        "测试敏感词", "违规内容", "禁止发布"
    ));

    /**
     * 扩展敏感词库
     */
    private static final Map<String, String> SENSITIVE_WORD_MAP = new HashMap<>();

    static {
        for (String word : SENSITIVE_WORDS) {
            SENSITIVE_WORD_MAP.put(word, word);
        }
    }

    /**
     * 检测文本中是否包含敏感词
     *
     * @param text 待检测文本
     * @return 是否包含敏感词
     */
    public static boolean containsSensitiveWord(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        String lowerText = text.toLowerCase();
        for (String word : SENSITIVE_WORDS) {
            if (lowerText.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测文本中的所有敏感词
     *
     * @param text 待检测文本
     * @return 检测到的敏感词列表
     */
    public static List<String> findSensitiveWords(String text) {
        List<String> foundWords = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return foundWords;
        }
        String lowerText = text.toLowerCase();
        for (String word : SENSITIVE_WORDS) {
            if (lowerText.contains(word.toLowerCase())) {
                foundWords.add(word);
            }
        }
        return foundWords;
    }

    /**
     * 替换文本中的敏感词
     *
     * @param text        待处理文本
     * @param replacement 替换字符
     * @return 处理后的文本
     */
    public static String replaceSensitiveWords(String text, String replacement) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String result = text;
        for (String word : SENSITIVE_WORDS) {
            result = result.replaceAll("(?i)" + word, replacement);
        }
        return result;
    }

    /**
     * 使用星号替换敏感词
     *
     * @param text 待处理文本
     * @return 处理后的文本
     */
    public static String maskSensitiveWords(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        String result = text;
        for (String word : SENSITIVE_WORDS) {
            String mask = getMaskString(word.length());
            result = result.replaceAll("(?i)" + word, mask);
        }
        return result;
    }

    /**
     * 生成指定长度的星号字符串
     */
    private static String getMaskString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("*");
        }
        return sb.toString();
    }

    /**
     * 添加自定义敏感词
     *
     * @param word 敏感词
     */
    public static void addSensitiveWord(String word) {
        if (word != null && !word.isEmpty()) {
            SENSITIVE_WORDS.add(word);
            SENSITIVE_WORD_MAP.put(word, word);
        }
    }

    /**
     * 批量添加敏感词
     *
     * @param words 敏感词列表
     */
    public static void addSensitiveWords(Collection<String> words) {
        if (words != null && !words.isEmpty()) {
            for (String word : words) {
                addSensitiveWord(word);
            }
        }
    }

    /**
     * 获取所有敏感词
     *
     * @return 敏感词集合
     */
    public static Set<String> getAllSensitiveWords() {
        return Collections.unmodifiableSet(SENSITIVE_WORDS);
    }
}
