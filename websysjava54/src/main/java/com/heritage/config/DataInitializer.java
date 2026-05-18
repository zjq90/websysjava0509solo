package com.heritage.config;

import com.heritage.entity.*;
import com.heritage.enums.AuditStatus;
import com.heritage.enums.RiskLevel;
import com.heritage.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HeritageRepository heritageRepository;

    @Autowired
    private CategoryTagRepository categoryTagRepository;

    @Autowired
    private DesensitizationRuleRepository desensitizationRuleRepository;

    @Autowired
    private MonitoringRuleRepository monitoringRuleRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReportRepository reportRepository;

    private final Random random = new Random();

    private final String[] provinces = {"北京市", "上海市", "广东省", "浙江省", "江苏省", "四川省", "河南省", "湖北省", "陕西省", "山东省"};
    private final String[] dynasties = {"商朝", "周朝", "秦朝", "汉朝", "三国", "晋朝", "隋朝", "唐朝", "宋朝", "元朝", "明朝", "清朝"};
    private final String[] materials = {"青铜", "瓷器", "玉器", "金器", "银器", "铁器", "木器", "漆器", "丝绸", "字画"};
    private final String[] usages = {"礼器", "兵器", "乐器", "饮食器", "酒器", "水器", "陈设", "文具", "饰品", "冥器"};
    private final String[] heritageNames = {"司母戊鼎", "四羊方尊", "越王勾践剑", "曾侯乙编钟", "马踏飞燕", "金缕玉衣", "长信宫灯", "铜奔马", "兵马俑", "青花瓷瓶"};

    @Override
    public void run(String... args) throws Exception {
        initCategoryTags();
        initDesensitizationRules();
        initMonitoringRules();
        initUsers();
        initHeritage();
        initTransactions();
        initReports();
    }

    private void initCategoryTags() {
        for (int i = 0; i < dynasties.length; i++) {
            CategoryTag tag = new CategoryTag();
            tag.setName(dynasties[i]);
            tag.setType("dynasty");
            tag.setSortOrder(i);
            tag.setEnabled(true);
            categoryTagRepository.save(tag);
        }

        for (int i = 0; i < materials.length; i++) {
            CategoryTag tag = new CategoryTag();
            tag.setName(materials[i]);
            tag.setType("material");
            tag.setSortOrder(i);
            tag.setEnabled(true);
            categoryTagRepository.save(tag);
        }

        for (int i = 0; i < usages.length; i++) {
            CategoryTag tag = new CategoryTag();
            tag.setName(usages[i]);
            tag.setType("usage");
            tag.setSortOrder(i);
            tag.setEnabled(true);
            categoryTagRepository.save(tag);
        }
    }

    private void initDesensitizationRules() {
        DesensitizationRule rule1 = new DesensitizationRule();
        rule1.setFieldName("phone");
        rule1.setDescription("手机号脱敏");
        rule1.setRegexPattern("(\\d{3})\\d{4}(\\d{4})");
        rule1.setReplacement("$1****$2");
        rule1.setEnabled(true);
        rule1.setSortOrder(1);
        desensitizationRuleRepository.save(rule1);

        DesensitizationRule rule2 = new DesensitizationRule();
        rule2.setFieldName("idCard");
        rule2.setDescription("身份证号脱敏");
        rule2.setRegexPattern("(\\d{6})\\d{8}(\\d{4})");
        rule2.setReplacement("$1********$2");
        rule2.setEnabled(true);
        rule2.setSortOrder(2);
        desensitizationRuleRepository.save(rule2);

        DesensitizationRule rule3 = new DesensitizationRule();
        rule3.setFieldName("name");
        rule3.setDescription("姓名脱敏");
        rule3.setRegexPattern("([\\u4e00-\\u9fa5]{1})([\\u4e00-\\u9fa5]+)");
        rule3.setReplacement("$1*");
        rule3.setEnabled(true);
        rule3.setSortOrder(3);
        desensitizationRuleRepository.save(rule3);
    }

    private void initMonitoringRules() {
        MonitoringRule rule1 = new MonitoringRule();
        rule1.setRuleName("高频交易监测");
        rule1.setRuleType("FREQUENCY");
        rule1.setFrequencyThreshold(10);
        rule1.setFrequencyMinutes(60);
        rule1.setEnabled(true);
        monitoringRuleRepository.save(rule1);

        MonitoringRule rule2 = new MonitoringRule();
        rule2.setRuleName("价格偏离监测");
        rule2.setRuleType("PRICE_DEVIATION");
        rule2.setPriceDeviationThreshold(new BigDecimal("50.00"));
        rule2.setEnabled(true);
        monitoringRuleRepository.save(rule2);

        MonitoringRule rule3 = new MonitoringRule();
        rule3.setRuleName("低信用分监测");
        rule3.setRuleType("LOW_CREDIT");
        rule3.setLowCreditScoreThreshold(60);
        rule3.setEnabled(true);
        monitoringRuleRepository.save(rule3);
    }

    private void initUsers() {
        String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十", "郑十一", "王十二"};
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < names.length; i++) {
            User user = new User();
            user.setUsername("user" + (i + 1));
            user.setPassword("password" + (i + 1));
            user.setRealName(names[i]);
            user.setPhone("138" + String.format("%08d", i));
            user.setIdCard("1101011990" + String.format("%02d%02d", i + 1, i + 1) + String.format("%04d", i));
            user.setEmail("user" + (i + 1) + "@example.com");
            user.setProvince(provinces[i % provinces.length]);
            user.setCreditScore(60 + random.nextInt(41));
            user.setEnabled(true);
            user.setRealNameAuditStatus(AuditStatus.APPROVED);
            user.setIdVerified(true);
            user.setFaceVerified(true);

            user.setCreateTime(now.minusDays(random.nextInt(30)));
            user.setUpdateTime(user.getCreateTime());

            if (i < 3) {
                user.setIsExpert(true);
                user.setExpertAuditStatus(AuditStatus.APPROVED);
                user.setExpertCertificate("certificate_" + i + ".pdf");
                user.setWorkProof("workproof_" + i + ".pdf");
            } else if (i < 5) {
                user.setIsExpert(true);
                user.setExpertAuditStatus(AuditStatus.PENDING);
                user.setExpertCertificate("certificate_" + i + ".pdf");
                user.setWorkProof("workproof_" + i + ".pdf");
            }

            if (i == 8) {
                user.setIsSuspicious(true);
                user.setSuspiciousReason("交易行为异常");
                user.setCreditScore(45);
            }

            if (i == 9) {
                user.setFundsFrozen(true);
                user.setFreezeReason("涉嫌非法交易");
                user.setFreezeTime(LocalDateTime.now());
                user.setCreditScore(30);
            }

            userRepository.save(user);
        }
    }

    private void initHeritage() {
        for (int i = 0; i < heritageNames.length; i++) {
            Heritage heritage = new Heritage();
            heritage.setName(heritageNames[i]);
            heritage.setDescription("这是一件珍贵的" + dynasties[i % dynasties.length] + "时期的" +
                    materials[i % materials.length] + "文物，具有极高的历史和艺术价值。");
            heritage.setDynasty(dynasties[i % dynasties.length]);
            heritage.setMaterial(materials[i % materials.length]);
            heritage.setUsageType(usages[i % usages.length]);
            heritage.setEstimatedValue(new BigDecimal(100000 + random.nextInt(900000)));
            heritage.setOrigin(provinces[i % provinces.length] + "某地出土");
            heritage.setProvince(provinces[i % provinces.length]);
            heritage.setCity(provinces[i % provinces.length] + "市");
            heritage.setVerificationCode("HW" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            heritage.setOwnerName("收藏家" + (i + 1));
            heritage.setOwnerIdCard("1101011980" + String.format("%02d%02d", i + 1, i + 1) + String.format("%04d", i));
            heritage.setOwnerPhone("139" + String.format("%08d", i));
            heritage.setProvenance("家族传承，有明确的收藏记录");

            if (i < 5) {
                heritage.setRiskLevel(RiskLevel.values()[i % 3]);
                heritage.setAuditStatus(AuditStatus.PENDING);
                heritage.setApiVerified(false);
            } else {
                heritage.setRiskLevel(RiskLevel.LOW);
                heritage.setAuditStatus(AuditStatus.APPROVED);
                heritage.setApiVerified(true);
                heritage.setApiVerifyResult("国家文物局API验证通过");
            }

            heritageRepository.save(heritage);
        }
    }

    private void initTransactions() {
        for (int i = 0; i < 8; i++) {
            Transaction transaction = new Transaction();
            transaction.setTransactionNo("TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            transaction.setHeritageId((long) (i + 1));
            transaction.setHeritageName(heritageNames[i]);
            transaction.setBuyerId((long) (i % 5 + 1));
            transaction.setBuyerName("user" + (i % 5 + 1));
            transaction.setSellerId((long) ((i + 2) % 5 + 1));
            transaction.setSellerName("user" + ((i + 2) % 5 + 1));
            transaction.setAmount(new BigDecimal(50000 + random.nextInt(500000)));
            transaction.setEstimatedValue(new BigDecimal(100000 + random.nextInt(400000)));
            transaction.setIsAbnormal(i < 2);
            if (i < 2) {
                transaction.setAbnormalReason(i == 0 ? "高频交易预警" : "价格偏离预估");
            }
            transactionRepository.save(transaction);
        }
    }

    private void initReports() {
        for (int i = 0; i < 5; i++) {
            Report report = new Report();
            report.setReportType(i % 2 == 0 ? "文物问题" : "交易问题");
            report.setTargetId((long) (i + 1));
            report.setTargetName(heritageNames[i]);
            report.setReporterId((long) (i % 3 + 1));
            report.setReporterName("user" + (i % 3 + 1));
            report.setReason(i % 2 == 0 ? "文物来源存疑" : "交易价格异常");
            report.setStatus(i < 3 ? AuditStatus.PENDING : AuditStatus.APPROVED);
            if (i >= 3) {
                report.setHandleRemark("已核实，处理完成");
                report.setHandlerId(1L);
                report.setHandleTime(LocalDateTime.now());
            }
            reportRepository.save(report);
        }
    }
}
