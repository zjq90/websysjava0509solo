package com.heritage.service;

import com.heritage.entity.Heritage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MasterDataService {

    private static final Map<String, String> CATEGORY_CODES = new HashMap<>();
    private static final Map<String, String> DYNASTY_CODES = new HashMap<>();

    static {
        CATEGORY_CODES.put("瓷器", "01");
        CATEGORY_CODES.put("玉器", "02");
        CATEGORY_CODES.put("青铜器", "03");
        CATEGORY_CODES.put("书画", "04");
        CATEGORY_CODES.put("陶器", "05");
        CATEGORY_CODES.put("金银器", "06");
        CATEGORY_CODES.put("漆器", "07");
        CATEGORY_CODES.put("木器", "08");
        CATEGORY_CODES.put("其他", "99");

        DYNASTY_CODES.put("先秦", "01");
        DYNASTY_CODES.put("秦代", "02");
        DYNASTY_CODES.put("汉代", "03");
        DYNASTY_CODES.put("魏晋南北朝", "04");
        DYNASTY_CODES.put("隋代", "05");
        DYNASTY_CODES.put("唐代", "06");
        DYNASTY_CODES.put("宋代", "07");
        DYNASTY_CODES.put("元代", "08");
        DYNASTY_CODES.put("明代", "09");
        DYNASTY_CODES.put("清代", "10");
        DYNASTY_CODES.put("民国", "11");
        DYNASTY_CODES.put("近现代", "12");
        DYNASTY_CODES.put("未知", "99");
    }

    public String generateMasterCode(Heritage heritage) {
        StringBuilder codeBuilder = new StringBuilder();

        codeBuilder.append("GW");

        String categoryCode = CATEGORY_CODES.getOrDefault(heritage.getCategory(), "99");
        codeBuilder.append(categoryCode);

        String dynastyCode = DYNASTY_CODES.getOrDefault(heritage.getDynasty(), "99");
        codeBuilder.append(dynastyCode);

        String yearCode = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        codeBuilder.append(yearCode);

        int sequence = (int) (Math.random() * 10000);
        codeBuilder.append(String.format("%04d", sequence));

        String masterCode = codeBuilder.toString();
        log.info("生成主数据编码: {}", masterCode);
        return masterCode;
    }

    public Heritage convertToMasterData(Heritage heritage) {
        String masterCode = generateMasterCode(heritage);
        heritage.setMasterCode(masterCode);
        heritage.setIsMasterData(true);
        heritage.setVerificationStatus("已认证");

        if (heritage.getHeritageCode() == null || heritage.getHeritageCode().isEmpty()) {
            heritage.setHeritageCode(masterCode);
        }

        log.info("文物 {} 已转换为主数据，主编码: {}", heritage.getName(), masterCode);
        return heritage;
    }

    public boolean validateMasterData(Heritage heritage) {
        if (heritage.getName() == null || heritage.getName().isEmpty()) {
            log.warn("主数据验证失败: 名称为空");
            return false;
        }
        if (heritage.getCategory() == null || heritage.getCategory().isEmpty()) {
            log.warn("主数据验证失败: 类别为空");
            return false;
        }
        if (heritage.getDynasty() == null || heritage.getDynasty().isEmpty()) {
            log.warn("主数据验证失败: 年代为空");
            return false;
        }
        if (heritage.getQualityScore() == null || heritage.getQualityScore() < 60) {
            log.warn("主数据验证失败: 数据质量分数过低");
            return false;
        }
        return true;
    }

    public String getNationalStandardCode(String category) {
        return CATEGORY_CODES.getOrDefault(category, "99");
    }

    public String getDynastyStandardCode(String dynasty) {
        return DYNASTY_CODES.getOrDefault(dynasty, "99");
    }

    public Map<String, String> getAllCategoryCodes() {
        return new HashMap<>(CATEGORY_CODES);
    }

    public Map<String, String> getAllDynastyCodes() {
        return new HashMap<>(DYNASTY_CODES);
    }
}
