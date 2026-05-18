package com.heritage.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * AI内容审核服务
 * 模拟AI审核功能，实际项目中可接入百度AI、阿里云内容安全等服务
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class AIReviewService {

    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
            "走私", "倒卖", "非法", "黑市", "违禁品", "国家一级文物",
            "盗墓", "出土", "地下文物", "私下交易", "避税"
    );

    private static final List<String> TRADING_KEYWORDS = Arrays.asList(
            "出售", "卖", "转让", "价格", "多少钱", "收购",
            "收", "购买", "求购", "联系方式", "微信", "电话"
    );

    /**
     * 审核社区内容
     *
     * @param content 待审核内容
     * @return 审核结果: 1-通过 2-拒绝
     */
    public ReviewResult reviewContent(String content) {
        log.info("开始AI内容审核: {}", content);

        if (content == null || content.trim().isEmpty()) {
            return new ReviewResult(1, "内容为空，审核通过", "low", null);
        }

        StringBuilder riskKeywords = new StringBuilder();
        boolean hasTradingInfo = false;

        for (String word : SENSITIVE_WORDS) {
            if (content.contains(word)) {
                if (riskKeywords.length() > 0) {
                    riskKeywords.append(",");
                }
                riskKeywords.append(word);
            }
        }

        for (String word : TRADING_KEYWORDS) {
            if (content.contains(word)) {
                hasTradingInfo = true;
                if (riskKeywords.length() > 0) {
                    riskKeywords.append(",");
                }
                riskKeywords.append(word);
            }
        }

        if (riskKeywords.length() > 0) {
            String riskLevel = hasTradingInfo ? "high" : "medium";
            String result = "检测到风险关键词: " + riskKeywords;
            log.warn("AI审核不通过: {}", result);
            return new ReviewResult(2, result, riskLevel, riskKeywords.toString());
        }

        log.info("AI审核通过");
        return new ReviewResult(1, "审核通过", "low", null);
    }

    public static class ReviewResult {
        private final Integer status;
        private final String message;
        private final String riskLevel;
        private final String riskKeywords;

        public ReviewResult(Integer status, String message, String riskLevel, String riskKeywords) {
            this.status = status;
            this.message = message;
            this.riskLevel = riskLevel;
            this.riskKeywords = riskKeywords;
        }

        public Integer getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getRiskLevel() {
            return riskLevel;
        }

        public String getRiskKeywords() {
            return riskKeywords;
        }
    }
}
