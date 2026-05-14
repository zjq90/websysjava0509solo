package com.broadband.config;

import com.broadband.entity.BroadbandNumber;
import com.broadband.entity.Package;
import com.broadband.repository.BroadbandNumberRepository;
import com.broadband.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据初始化器
 * 系统启动时自动初始化测试数据
 * 
 * @author broadband
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private BroadbandNumberRepository broadbandNumberRepository;

    @Override
    public void run(String... args) throws Exception {
        if (packageRepository.count() == 0) {
            initPackages();
        }
        if (broadbandNumberRepository.count() == 0) {
            initBroadbandNumbers();
        }
        System.out.println("====================================");
        System.out.println("  测试数据初始化完成!");
        System.out.println("====================================");
    }

    private void initPackages() {
        Package p1 = new Package();
        p1.setName("100M光纤宽带");
        p1.setType(1);
        p1.setBandwidth(100);
        p1.setMonthlyFee(new BigDecimal("99"));
        p1.setYearlyFee(new BigDecimal("999"));
        p1.setDescription("100Mbps光纤宽带，畅享高速上网体验，适合家庭日常使用。");
        p1.setFeatures("[\"光猫免费租用\",\"免费安装\",\"24小时客服支持\"]");
        p1.setCanUpgrade(1);
        p1.setCanDowngrade(1);
        p1.setStatus(1);
        p1.setSort(1);
        p1.setCreateTime(LocalDateTime.now());
        p1.setUpdateTime(LocalDateTime.now());
        packageRepository.save(p1);

        Package p2 = new Package();
        p2.setName("300M光纤宽带");
        p2.setType(1);
        p2.setBandwidth(300);
        p2.setMonthlyFee(new BigDecimal("159"));
        p2.setYearlyFee(new BigDecimal("1599"));
        p2.setDescription("300Mbps光纤宽带，流畅观看4K视频，适合多人同时上网。");
        p2.setFeatures("[\"光猫免费租用\",\"免费安装\",\"24小时客服支持\",\"免费提速\"]");
        p2.setCanUpgrade(1);
        p2.setCanDowngrade(1);
        p2.setStatus(1);
        p2.setSort(2);
        p2.setCreateTime(LocalDateTime.now());
        p2.setUpdateTime(LocalDateTime.now());
        packageRepository.save(p2);

        Package p3 = new Package();
        p3.setName("500M光纤宽带");
        p3.setType(1);
        p3.setBandwidth(500);
        p3.setMonthlyFee(new BigDecimal("219"));
        p3.setYearlyFee(new BigDecimal("2199"));
        p3.setDescription("500Mbps光纤宽带，极速上网体验，适合电竞游戏和企业办公。");
        p3.setFeatures("[\"光猫免费租用\",\"免费安装\",\"24小时客服支持\",\"专属客服\"]");
        p3.setCanUpgrade(1);
        p3.setCanDowngrade(1);
        p3.setStatus(1);
        p3.setSort(3);
        p3.setCreateTime(LocalDateTime.now());
        p3.setUpdateTime(LocalDateTime.now());
        packageRepository.save(p3);

        Package p4 = new Package();
        p4.setName("1000M光纤宽带");
        p4.setType(1);
        p4.setBandwidth(1000);
        p4.setMonthlyFee(new BigDecimal("299"));
        p4.setYearlyFee(new BigDecimal("2999"));
        p4.setDescription("1000Mbps千兆光纤宽带，顶级上网体验，适合高端用户和小型企业。");
        p4.setFeatures("[\"光猫免费租用\",\"免费安装\",\"24小时客服支持\",\"专属客服\",\"上门维修\"]");
        p4.setCanUpgrade(0);
        p4.setCanDowngrade(1);
        p4.setStatus(1);
        p4.setSort(4);
        p4.setCreateTime(LocalDateTime.now());
        p4.setUpdateTime(LocalDateTime.now());
        packageRepository.save(p4);

        Package e1 = new Package();
        e1.setName("提速包(+100M)");
        e1.setType(2);
        e1.setMonthlyFee(new BigDecimal("30"));
        e1.setDescription("在原带宽基础上增加100Mbps，立即生效。");
        e1.setParentId(p1.getId());
        e1.setStatus(1);
        e1.setSort(1);
        e1.setCreateTime(LocalDateTime.now());
        e1.setUpdateTime(LocalDateTime.now());
        packageRepository.save(e1);

        Package e2 = new Package();
        e2.setName("网络安全防护包");
        e2.setType(3);
        e2.setMonthlyFee(new BigDecimal("20"));
        e2.setDescription("提供网络安全防护，防病毒、防黑客、防钓鱼网站。");
        e2.setStatus(1);
        e2.setSort(2);
        e2.setCreateTime(LocalDateTime.now());
        e2.setUpdateTime(LocalDateTime.now());
        packageRepository.save(e2);

        Package e3 = new Package();
        e3.setName("家庭WiFi覆盖包");
        e3.setType(3);
        e3.setMonthlyFee(new BigDecimal("25"));
        e3.setDescription("提供全屋WiFi覆盖解决方案，消除信号死角。");
        e3.setStatus(1);
        e3.setSort(3);
        e3.setCreateTime(LocalDateTime.now());
        e3.setUpdateTime(LocalDateTime.now());
        packageRepository.save(e3);
    }

    private void initBroadbandNumbers() {
        String[] regions = {"北京", "上海", "广州", "深圳", "杭州", "南京", "武汉", "成都"};
        
        for (String region : regions) {
            for (int i = 0; i < 20; i++) {
                BroadbandNumber number = new BroadbandNumber();
                String num = generateNumber(region, i);
                number.setNumber(num);
                number.setType(1);
                number.setRegion(region);
                number.setStatus(1);
                number.setCreateTime(LocalDateTime.now());
                number.setUpdateTime(LocalDateTime.now());
                broadbandNumberRepository.save(number);
            }

            String[] fancyNumbers = {
                "0000", "1111", "2222", "3333", "4444",
                "5555", "6666", "7777", "8888", "9999",
                "1234", "4321", "5678", "8765", "6688"
            };

            for (String fancy : fancyNumbers) {
                BroadbandNumber number = new BroadbandNumber();
                number.setNumber(getRegionPrefix(region) + fancy);
                number.setType(2);
                number.setLevel("A级");
                number.setExtraFee(new BigDecimal("200"));
                number.setTags("靓号,A级,吉祥号");
                number.setRegion(region);
                number.setStatus(1);
                number.setCreateTime(LocalDateTime.now());
                number.setUpdateTime(LocalDateTime.now());
                broadbandNumberRepository.save(number);
            }
        }
    }

    private String generateNumber(String region, int index) {
        String prefix = getRegionPrefix(region);
        return prefix + String.format("%04d", index + 1000);
    }

    private String getRegionPrefix(String region) {
        switch (region) {
            case "北京": return "010";
            case "上海": return "021";
            case "广州": return "020";
            case "深圳": return "0755";
            case "杭州": return "0571";
            case "南京": return "025";
            case "武汉": return "027";
            case "成都": return "028";
            default: return "000";
        }
    }
}
