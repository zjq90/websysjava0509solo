package com.accounting.service;

import com.accounting.dto.OcrRecognitionDTO;
import com.accounting.dto.VoiceRecognitionDTO;
import com.accounting.entity.Category;
import com.accounting.enums.BillType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecognitionService {

    private final CategoryService categoryService;

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(\\d+(?:\\.\\d{1,2})?)\\s*元");
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4})[-/年](\\d{1,2})[-/月](\\d{1,2})[日号]?");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2})[:点时](\\d{1,2})[分]?");

    private static final Map<String, String> CATEGORY_KEYWORDS = new HashMap<>();
    private static final Map<String, String> MERCHANT_CATEGORY_MAP = new HashMap<>();

    static {
        CATEGORY_KEYWORDS.put("午餐", "餐饮");
        CATEGORY_KEYWORDS.put("晚餐", "餐饮");
        CATEGORY_KEYWORDS.put("早餐", "餐饮");
        CATEGORY_KEYWORDS.put("吃饭", "餐饮");
        CATEGORY_KEYWORDS.put("餐饮", "餐饮");
        CATEGORY_KEYWORDS.put("咖啡", "餐饮");
        CATEGORY_KEYWORDS.put("奶茶", "餐饮");
        CATEGORY_KEYWORDS.put("饮料", "餐饮");
        CATEGORY_KEYWORDS.put("地铁", "交通");
        CATEGORY_KEYWORDS.put("公交", "交通");
        CATEGORY_KEYWORDS.put("打车", "交通");
        CATEGORY_KEYWORDS.put("出租车", "交通");
        CATEGORY_KEYWORDS.put("加油", "交通");
        CATEGORY_KEYWORDS.put("停车", "交通");
        CATEGORY_KEYWORDS.put("房租", "住房");
        CATEGORY_KEYWORDS.put("水电", "住房");
        CATEGORY_KEYWORDS.put("物业", "住房");
        CATEGORY_KEYWORDS.put("电影", "娱乐");
        CATEGORY_KEYWORDS.put("游戏", "娱乐");
        CATEGORY_KEYWORDS.put("旅游", "娱乐");
        CATEGORY_KEYWORDS.put("衣服", "购物");
        CATEGORY_KEYWORDS.put("鞋子", "购物");
        CATEGORY_KEYWORDS.put("淘宝", "购物");
        CATEGORY_KEYWORDS.put("京东", "购物");
        CATEGORY_KEYWORDS.put("工资", "工资");
        CATEGORY_KEYWORDS.put("奖金", "奖金");
        CATEGORY_KEYWORDS.put("红包", "红包");

        MERCHANT_CATEGORY_MAP.put("星巴克", "餐饮");
        MERCHANT_CATEGORY_MAP.put("麦当劳", "餐饮");
        MERCHANT_CATEGORY_MAP.put("肯德基", "餐饮");
        MERCHANT_CATEGORY_MAP.put("海底捞", "餐饮");
        MERCHANT_CATEGORY_MAP.put("瑞幸", "餐饮");
        MERCHANT_CATEGORY_MAP.put("喜茶", "餐饮");
        MERCHANT_CATEGORY_MAP.put("奈雪", "餐饮");
        MERCHANT_CATEGORY_MAP.put("沃尔玛", "购物");
        MERCHANT_CATEGORY_MAP.put("家乐福", "购物");
        MERCHANT_CATEGORY_MAP.put("永辉", "购物");
        MERCHANT_CATEGORY_MAP.put("滴滴", "交通");
        MERCHANT_CATEGORY_MAP.put("高德打车", "交通");
        MERCHANT_CATEGORY_MAP.put("中国石油", "交通");
        MERCHANT_CATEGORY_MAP.put("中国石化", "交通");
    }

    public VoiceRecognitionDTO recognizeVoice(String text) {
        log.info("开始语音识别: {}", text);
        VoiceRecognitionDTO result = new VoiceRecognitionDTO();
        result.setOriginalText(text);
        result.setConfidence(0.85);

        BigDecimal amount = extractAmount(text);
        result.setAmount(amount);

        String category = extractCategory(text);
        result.setCategory(category);

        String merchant = extractMerchant(text);
        result.setMerchant(merchant);

        LocalDateTime time = extractDateTime(text);
        result.setTransactionTime(time);

        log.info("语音识别完成: 金额={}, 分类={}, 商家={}, 时间={}",
                amount, category, merchant, time);

        return result;
    }

    public OcrRecognitionDTO recognizeOcr(String imageUrl) {
        log.info("开始OCR识别: {}", imageUrl);

        String mockOcrText = generateMockOcrText();
        OcrRecognitionDTO result = new OcrRecognitionDTO();
        result.setOriginalText(mockOcrText);
        result.setConfidence(0.92);

        BigDecimal amount = extractAmount(mockOcrText);
        result.setAmount(amount);

        String merchant = extractMerchant(mockOcrText);
        result.setMerchant(merchant);

        LocalDateTime time = extractDateTime(mockOcrText);
        result.setTransactionTime(time);

        String category = matchCategoryByMerchant(merchant);
        result.setCategory(category);

        log.info("OCR识别完成: 金额={}, 商家={}, 分类={}, 时间={}",
                amount, merchant, category, time);

        return result;
    }

    private BigDecimal extractAmount(String text) {
        Matcher matcher = AMOUNT_PATTERN.matcher(text);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }

        Pattern simpleAmountPattern = Pattern.compile("(\\d+(?:\\.\\d{1,2})?)");
        matcher = simpleAmountPattern.matcher(text);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }

        return BigDecimal.ZERO;
    }

    private String extractCategory(String text) {
        for (Map.Entry<String, String> entry : CATEGORY_KEYWORDS.entrySet()) {
            if (text.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        for (Map.Entry<String, String> entry : MERCHANT_CATEGORY_MAP.entrySet()) {
            if (text.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "其他";
    }

    private String extractMerchant(String text) {
        for (String merchant : MERCHANT_CATEGORY_MAP.keySet()) {
            if (text.contains(merchant)) {
                return merchant;
            }
        }

        Matcher matcher = Pattern.compile("(午餐|晚餐|早餐|奶茶|咖啡|打车|地铁|公交)").matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private LocalDateTime extractDateTime(String text) {
        LocalDateTime now = LocalDateTime.now();

        Matcher dateMatcher = DATE_PATTERN.matcher(text);
        Matcher timeMatcher = TIME_PATTERN.matcher(text);

        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();

        if (dateMatcher.find()) {
            year = Integer.parseInt(dateMatcher.group(1));
            month = Integer.parseInt(dateMatcher.group(2));
            day = Integer.parseInt(dateMatcher.group(3));
        }

        if (timeMatcher.find()) {
            hour = Integer.parseInt(timeMatcher.group(1));
            minute = Integer.parseInt(timeMatcher.group(2));
        }

        if (text.contains("今天")) {
            return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hour, minute);
        }
        if (text.contains("昨天")) {
            LocalDateTime yesterday = now.minusDays(1);
            return LocalDateTime.of(yesterday.getYear(), yesterday.getMonth(), yesterday.getDayOfMonth(), hour, minute);
        }
        if (text.contains("前天")) {
            LocalDateTime dayBefore = now.minusDays(2);
            return LocalDateTime.of(dayBefore.getYear(), dayBefore.getMonth(), dayBefore.getDayOfMonth(), hour, minute);
        }
        if (text.contains("早上") || text.contains("上午")) {
            hour = 8;
        }
        if (text.contains("中午")) {
            hour = 12;
        }
        if (text.contains("下午")) {
            hour = 15;
        }
        if (text.contains("晚上") || text.contains("傍晚")) {
            hour = 19;
        }

        return LocalDateTime.of(year, month, day, hour, minute);
    }

    private String matchCategoryByMerchant(String merchant) {
        if (merchant == null) {
            return "其他";
        }

        for (Map.Entry<String, String> entry : MERCHANT_CATEGORY_MAP.entrySet()) {
            if (merchant.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "其他";
    }

    private String generateMockOcrText() {
        String[] merchants = {"星巴克咖啡", "麦当劳", "肯德基", "瑞幸咖啡", "喜茶", "沃尔玛超市"};
        String merchant = merchants[(int) (Math.random() * merchants.length)];
        int amount = (int) (Math.random() * 200) + 10;
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"));

        return String.format("%s\n消费金额：%d.00元\n时间：%s\n收银员：001\n小票号：%d",
                merchant, amount, dateStr, System.currentTimeMillis());
    }

    public Category getCategoryByName(String categoryName) {
        Optional<Category> categoryOpt = categoryService.findByNameAndType(categoryName, BillType.EXPENSE);
        if (categoryOpt.isPresent()) {
            return categoryOpt.get();
        }

        Optional<Category> incomeCategoryOpt = categoryService.findByNameAndType(categoryName, BillType.INCOME);
        return incomeCategoryOpt.orElse(null);
    }
}
