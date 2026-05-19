package com.heritage.service;

import com.heritage.entity.Heritage;
import com.heritage.entity.Transaction;
import com.heritage.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ValueEvaluationService {

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Map<String, BigDecimal> DYNASTY_FACTOR = new HashMap<>();
    private static final Map<String, BigDecimal> CATEGORY_FACTOR = new HashMap<>();
    private static final Map<String, BigDecimal> LEVEL_FACTOR = new HashMap<>();

    static {
        DYNASTY_FACTOR.put("元代", new BigDecimal("1.5"));
        DYNASTY_FACTOR.put("明代", new BigDecimal("1.3"));
        DYNASTY_FACTOR.put("清代", new BigDecimal("1.2"));
        DYNASTY_FACTOR.put("宋代", new BigDecimal("1.8"));
        DYNASTY_FACTOR.put("唐代", new BigDecimal("2.0"));
        DYNASTY_FACTOR.put("汉代", new BigDecimal("1.6"));

        CATEGORY_FACTOR.put("瓷器", new BigDecimal("1.2"));
        CATEGORY_FACTOR.put("玉器", new BigDecimal("1.5"));
        CATEGORY_FACTOR.put("青铜器", new BigDecimal("2.0"));
        CATEGORY_FACTOR.put("书画", new BigDecimal("1.8"));

        LEVEL_FACTOR.put("一级文物", new BigDecimal("2.0"));
        LEVEL_FACTOR.put("二级文物", new BigDecimal("1.5"));
        LEVEL_FACTOR.put("三级文物", new BigDecimal("1.2"));
    }

    private static final BigDecimal MING_BLUE_WHITE_MIN_PRICE = new BigDecimal("100000");

    public BigDecimal evaluateValue(Heritage heritage) {
        BigDecimal basePrice = calculateBasePrice(heritage);
        BigDecimal expertAdjustment = applyExpertRules(heritage);
        BigDecimal historicalFactor = calculateHistoricalFactor(heritage);

        BigDecimal estimatedValue = basePrice
                .multiply(expertAdjustment)
                .multiply(historicalFactor);

        log.info("文物 {} 评估价值: {}", heritage.getName(), estimatedValue);
        return estimatedValue;
    }

    private BigDecimal calculateBasePrice(Heritage heritage) {
        BigDecimal basePrice = new BigDecimal("50000");

        BigDecimal dynastyFactor = DYNASTY_FACTOR.getOrDefault(heritage.getDynasty(), BigDecimal.ONE);
        BigDecimal categoryFactor = CATEGORY_FACTOR.getOrDefault(heritage.getCategory(), BigDecimal.ONE);
        BigDecimal levelFactor = LEVEL_FACTOR.getOrDefault(heritage.getLevel(), BigDecimal.ONE);

        return basePrice.multiply(dynastyFactor)
                .multiply(categoryFactor)
                .multiply(levelFactor);
    }

    private BigDecimal applyExpertRules(Heritage heritage) {
        BigDecimal adjustment = BigDecimal.ONE;

        if ("明代".equals(heritage.getDynasty()) && 
            "青花瓷".equals(heritage.getSubCategory())) {
            
            log.info("应用专家规则: 明代青花瓷价格下限保护");
            adjustment = new BigDecimal("1.2");
        }

        if ("一级文物".equals(heritage.getLevel())) {
            adjustment = adjustment.multiply(new BigDecimal("1.1"));
        }

        if (heritage.getDescription() != null && heritage.getDescription().contains("官窑")) {
            adjustment = adjustment.multiply(new BigDecimal("1.3"));
        }

        return adjustment;
    }

    private BigDecimal calculateHistoricalFactor(Heritage heritage) {
        if (heritage.getHeritageCode() == null) {
            return BigDecimal.ONE;
        }

        BigDecimal avgPrice = transactionRepository.calculateAveragePrice(heritage.getHeritageCode());
        if (avgPrice != null && avgPrice.compareTo(BigDecimal.ZERO) > 0) {
            long transactionCount = transactionRepository.countByHeritageCode(heritage.getHeritageCode());
            if (transactionCount >= 3) {
                log.info("基于历史交易数据计算价值因子");
                return new BigDecimal("1.1");
            }
        }

        return BigDecimal.ONE;
    }

    public Map<String, Object> getValueEvaluationReport(Heritage heritage) {
        Map<String, Object> report = new HashMap<>();
        
        report.put("heritageName", heritage.getName());
        report.put("heritageCode", heritage.getHeritageCode());
        report.put("estimatedValue", evaluateValue(heritage));
        
        if ("明代".equals(heritage.getDynasty()) && "青花瓷".equals(heritage.getSubCategory())) {
            report.put("expertRuleApplied", "明代青花瓷价格下限保护");
            report.put("minPriceGuarantee", MING_BLUE_WHITE_MIN_PRICE);
        }

        long transactionCount = transactionRepository.countByHeritageCode(heritage.getHeritageCode());
        report.put("transactionCount", transactionCount);
        
        return report;
    }

    public BigDecimal getMingBlueWhiteMinPrice() {
        return MING_BLUE_WHITE_MIN_PRICE;
    }
}
