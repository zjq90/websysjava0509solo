package com.petclinic.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核结果DTO
 * 用于返回内容审核的详细结果
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
public class ReviewResult {

    /**
     * 是否通过审核
     */
    private boolean passed;

    /**
     * 是否包含敏感内容
     */
    private boolean hasSensitiveContent;

    /**
     * 检测到的敏感词列表
     */
    private List<String> sensitiveWordsFound = new ArrayList<>();

    /**
     * 过滤后的内容（敏感词替换为***）
     */
    private String filteredContent;

    /**
     * 原始内容
     */
    private String originalContent;

    /**
     * 审核消息/说明
     */
    private String message;

    /**
     * 违规级别：LOW-低, MEDIUM-中, HIGH-高
     */
    private String violationLevel;

    public ReviewResult() {
        this.passed = true;
        this.hasSensitiveContent = false;
    }

    public void addSensitiveWord(String word) {
        this.sensitiveWordsFound.add(word);
        this.hasSensitiveContent = true;
        this.passed = false;
    }
}
