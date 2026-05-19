package com.petclinic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.petclinic.dto.ReviewResult;
import com.petclinic.entity.ReviewRule;
import com.petclinic.repository.ReviewRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 审核规则Service
 * 管理敏感词列表和内容审核规则，提供完整的内容审核和过滤功能
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Service
public class ReviewRuleService {

    @Autowired
    private ReviewRuleRepository reviewRuleRepository;

    /**
     * 敏感词缓存，提高审核性能
     */
    private volatile Set<String> sensitiveWordCache = new HashSet<>();

    /**
     * 缓存最后更新时间
     */
    private volatile long cacheLastUpdateTime = 0;

    /**
     * 缓存有效期（毫秒）
     */
    private static final long CACHE_EXPIRE_TIME = 5 * 60 * 1000; // 5分钟

    /**
     * 查询所有审核规则
     */
    public List<ReviewRule> findAll() {
        return reviewRuleRepository.findAll();
    }

    /**
     * 根据ID查询规则
     */
    public Optional<ReviewRule> findById(Long id) {
        return reviewRuleRepository.findById(id);
    }

    /**
     * 新增审核规则
     */
    public ReviewRule save(ReviewRule rule) {
        if (rule.getRuleType() == null) {
            rule.setRuleType("OTHER");
        }
        if (rule.getEnabled() == null) {
            rule.setEnabled(true);
        }
        if (rule.getConfiguredTime() == null) {
            rule.setConfiguredTime(LocalDateTime.now());
        }
        ReviewRule saved = reviewRuleRepository.save(rule);
        // 清除缓存，下次审核时重新加载
        clearCache();
        return saved;
    }

    /**
     * 更新审核规则
     */
    public ReviewRule update(Long id, ReviewRule rule) {
        Optional<ReviewRule> optional = reviewRuleRepository.findById(id);
        if (optional.isPresent()) {
            ReviewRule existing = optional.get();
            existing.setRuleName(rule.getRuleName());
            existing.setRuleType(rule.getRuleType());
            existing.setRuleContent(rule.getRuleContent());
            existing.setDescription(rule.getDescription());
            existing.setEnabled(rule.getEnabled());
            existing.setConfiguredBy(rule.getConfiguredBy());
            existing.setConfiguredTime(LocalDateTime.now());
            ReviewRule updated = reviewRuleRepository.save(existing);
            clearCache();
            return updated;
        }
        return null;
    }

    /**
     * 删除审核规则
     */
    public void deleteById(Long id) {
        reviewRuleRepository.deleteById(id);
        clearCache();
    }

    /**
     * 根据规则类型查询
     */
    public List<ReviewRule> findByRuleType(String ruleType) {
        return reviewRuleRepository.findByRuleType(ruleType);
    }

    /**
     * 查询启用的规则
     */
    public List<ReviewRule> findByEnabled(Boolean enabled) {
        return reviewRuleRepository.findByEnabled(enabled);
    }

    /**
     * 根据名称搜索
     */
    public List<ReviewRule> searchByName(String ruleName) {
        return reviewRuleRepository.findByRuleNameContaining(ruleName);
    }

    /**
     * 初始化默认敏感词规则
     */
    public ReviewRule initDefaultSensitiveWords() {
        ReviewRule rule = new ReviewRule();
        rule.setRuleName("默认敏感词列表");
        rule.setRuleType("SENSITIVE_WORD");
        rule.setRuleContent("[\"违禁\", \"违法\", \"走私\", \"假\", \"骗\", \"欺诈\", \"赌博\", \"色情\", \"暴力\", \"恐怖\"]");
        rule.setDescription("系统默认的敏感词过滤列表，包含违法违规、色情暴力等敏感词汇");
        rule.setEnabled(true);
        rule.setConfiguredBy("System");
        rule.setConfiguredTime(LocalDateTime.now());
        return reviewRuleRepository.save(rule);
    }

    /**
     * 添加敏感词
     */
    public ReviewRule addSensitiveWord(Long id, String word) {
        Optional<ReviewRule> optional = reviewRuleRepository.findById(id);
        if (optional.isPresent()) {
            ReviewRule rule = optional.get();
            String content = rule.getRuleContent();
            if (content != null && content.endsWith("]")) {
                try {
                    List<String> words = JSON.parseObject(content, new TypeReference<List<String>>() {});
                    if (!words.contains(word)) {
                        words.add(word);
                        rule.setRuleContent(JSON.toJSONString(words));
                        ReviewRule updated = reviewRuleRepository.save(rule);
                        clearCache();
                        return updated;
                    }
                } catch (Exception e) {
                    // JSON解析失败，使用简单的字符串拼接
                    content = content.substring(0, content.length() - 1) + ", \"" + word + "\"]";
                    rule.setRuleContent(content);
                    ReviewRule updated = reviewRuleRepository.save(rule);
                    clearCache();
                    return updated;
                }
            }
        }
        return null;
    }

    /**
     * 移除敏感词
     */
    public ReviewRule removeSensitiveWord(Long id, String word) {
        Optional<ReviewRule> optional = reviewRuleRepository.findById(id);
        if (optional.isPresent()) {
            ReviewRule rule = optional.get();
            String content = rule.getRuleContent();
            if (content != null) {
                try {
                    List<String> words = JSON.parseObject(content, new TypeReference<List<String>>() {});
                    words.remove(word);
                    rule.setRuleContent(JSON.toJSONString(words));
                    ReviewRule updated = reviewRuleRepository.save(rule);
                    clearCache();
                    return updated;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取所有启用的敏感词
     */
    public Set<String> getAllEnabledSensitiveWords() {
        Set<String> words = new HashSet<>();
        List<ReviewRule> rules = reviewRuleRepository.findByRuleType("SENSITIVE_WORD");
        for (ReviewRule rule : rules) {
            if (rule.getEnabled() && rule.getRuleContent() != null) {
                try {
                    List<String> wordList = JSON.parseObject(rule.getRuleContent(), new TypeReference<List<String>>() {});
                    words.addAll(wordList);
                } catch (Exception e) {
                    // 简单解析方式作为后备
                    String content = rule.getRuleContent();
                    content = content.replace("[", "").replace("]", "").replace("\"", "");
                    String[] wordArray = content.split(",");
                    for (String w : wordArray) {
                        if (w.trim().length() > 0) {
                            words.add(w.trim());
                        }
                    }
                }
            }
        }
        return words;
    }

    /**
     * 从缓存获取敏感词（提高性能）
     */
    private Set<String> getSensitiveWordsFromCache() {
        long now = System.currentTimeMillis();
        if (now - cacheLastUpdateTime > CACHE_EXPIRE_TIME || sensitiveWordCache.isEmpty()) {
            synchronized (this) {
                if (now - cacheLastUpdateTime > CACHE_EXPIRE_TIME || sensitiveWordCache.isEmpty()) {
                    sensitiveWordCache = getAllEnabledSensitiveWords();
                    cacheLastUpdateTime = now;
                }
            }
        }
        return sensitiveWordCache;
    }

    /**
     * 清除敏感词缓存
     */
    public void clearCache() {
        sensitiveWordCache.clear();
        cacheLastUpdateTime = 0;
    }

    /**
     * 检查文本是否包含敏感词
     */
    public boolean containsSensitiveWord(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        Set<String> words = getSensitiveWordsFromCache();
        for (String word : words) {
            if (word != null && word.length() > 0 && text.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 完整的内容审核方法
     * 返回详细的审核结果，包含检测到的敏感词、过滤后的内容等
     */
    public ReviewResult reviewContent(String content) {
        ReviewResult result = new ReviewResult();
        result.setOriginalContent(content);

        if (content == null || content.trim().isEmpty()) {
            result.setPassed(true);
            result.setFilteredContent(content);
            result.setMessage("内容为空，无需审核");
            return result;
        }

        Set<String> sensitiveWords = getSensitiveWordsFromCache();
        String filteredContent = content;
        Set<String> foundWords = new TreeSet<>();

        for (String word : sensitiveWords) {
            if (word != null && word.length() > 0 && content.contains(word)) {
                foundWords.add(word);
                // 将敏感词替换为等长度的*号
                String replacement = repeatString("*", word.length());
                filteredContent = filteredContent.replaceAll(Pattern.quote(word), replacement);
            }
        }

        result.setFilteredContent(filteredContent);
        result.getSensitiveWordsFound().addAll(foundWords);

        if (!foundWords.isEmpty()) {
            result.setPassed(false);
            result.setHasSensitiveContent(true);

            // 根据敏感词数量判断违规级别
            if (foundWords.size() >= 5) {
                result.setViolationLevel("HIGH");
                result.setMessage("内容包含大量敏感词汇，已被拒绝发布");
            } else if (foundWords.size() >= 2) {
                result.setViolationLevel("MEDIUM");
                result.setMessage("内容包含敏感词汇，已被自动过滤");
            } else {
                result.setViolationLevel("LOW");
                result.setMessage("内容包含少量敏感词汇，已被自动过滤");
            }
        } else {
            result.setPassed(true);
            result.setHasSensitiveContent(false);
            result.setMessage("内容审核通过");
        }

        return result;
    }

    /**
     * 审核并阻止违规内容
     * 如果发现敏感内容，返回审核结果；如果通过，返回null
     */
    public ReviewResult reviewAndBlock(String content) {
        ReviewResult result = reviewContent(content);
        if (result.isHasSensitiveContent() && 
            ("MEDIUM".equals(result.getViolationLevel()) || "HIGH".equals(result.getViolationLevel()))) {
            // 中高级违规，阻止发布
            return result;
        }
        // 低级违规或无违规，允许发布但已过滤内容
        return null;
    }

    /**
     * 批量审核多个字段内容
     * 用于药品描述、问诊记录等包含多个文本字段的场景
     */
    public ReviewResult reviewMultipleFields(Map<String, String> fields) {
        ReviewResult finalResult = new ReviewResult();
        StringBuilder allContent = new StringBuilder();
        Map<String, String> filteredFields = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            String content = entry.getValue();
            
            ReviewResult fieldResult = reviewContent(content);
            filteredFields.put(fieldName, fieldResult.getFilteredContent());
            
            if (fieldResult.isHasSensitiveContent()) {
                finalResult.getSensitiveWordsFound().addAll(fieldResult.getSensitiveWordsFound());
            }
            
            if (content != null) {
                allContent.append(content).append(" ");
            }
        }

        finalResult.setOriginalContent(allContent.toString());
        finalResult.setFilteredContent(JSON.toJSONString(filteredFields));
        
        if (!finalResult.getSensitiveWordsFound().isEmpty()) {
            finalResult.setHasSensitiveContent(true);
            finalResult.setPassed(false);
            int wordCount = finalResult.getSensitiveWordsFound().size();
            if (wordCount >= 5) {
                finalResult.setViolationLevel("HIGH");
                finalResult.setMessage("多个字段包含大量敏感词汇，已被拒绝");
            } else if (wordCount >= 2) {
                finalResult.setViolationLevel("MEDIUM");
                finalResult.setMessage("内容包含敏感词汇，已被自动过滤");
            } else {
                finalResult.setViolationLevel("LOW");
                finalResult.setMessage("内容包含少量敏感词汇");
            }
        } else {
            finalResult.setPassed(true);
            finalResult.setMessage("所有字段审核通过");
        }

        return finalResult;
    }

    /**
     * 辅助方法：重复字符串
     */
    private String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
