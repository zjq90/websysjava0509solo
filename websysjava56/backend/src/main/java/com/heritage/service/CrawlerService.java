package com.heritage.service;

import com.heritage.entity.Heritage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CrawlerService {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";

    public List<Heritage> crawlMuseumData(String museumUrl) {
        List<Heritage> heritageList = new ArrayList<>();
        try {
            log.info("开始爬取博物馆数据: {}", museumUrl);
            
            if (!checkRobotsTxt(museumUrl)) {
                log.warn("该网站不允许爬虫访问: {}", museumUrl);
                return heritageList;
            }

            Document doc = Jsoup.connect(museumUrl)
                    .userAgent(USER_AGENT)
                    .timeout(30000)
                    .get();

            log.info("成功获取页面内容");

        } catch (IOException e) {
            log.error("爬取数据失败: {}", e.getMessage());
        }
        return heritageList;
    }

    public List<Heritage> crawlAuctionData(String auctionUrl) {
        List<Heritage> heritageList = new ArrayList<>();
        try {
            log.info("开始爬取拍卖行数据: {}", auctionUrl);

            if (!checkRobotsTxt(auctionUrl)) {
                log.warn("该网站不允许爬虫访问: {}", auctionUrl);
                return heritageList;
            }

            Document doc = Jsoup.connect(auctionUrl)
                    .userAgent(USER_AGENT)
                    .timeout(30000)
                    .get();

            log.info("成功获取拍卖行页面内容");

        } catch (IOException e) {
            log.error("爬取拍卖行数据失败: {}", e.getMessage());
        }
        return heritageList;
    }

    private boolean checkRobotsTxt(String url) {
        try {
            String baseUrl = getBaseUrl(url);
            String robotsUrl = baseUrl + "/robots.txt";
            
            Document doc = Jsoup.connect(robotsUrl)
                    .userAgent(USER_AGENT)
                    .timeout(10000)
                    .ignoreHttpErrors(true)
                    .get();

            String content = doc.text();
            
            if (content.contains("User-agent: *") && content.contains("Disallow: /")) {
                return false;
            }

            return true;
        } catch (Exception e) {
            log.warn("检查robots.txt失败，默认允许访问");
            return true;
        }
    }

    private String getBaseUrl(String url) {
        try {
            int protocolEnd = url.indexOf("://");
            if (protocolEnd > 0) {
                int pathStart = url.indexOf("/", protocolEnd + 3);
                if (pathStart > 0) {
                    return url.substring(0, pathStart);
                }
                return url;
            }
        } catch (Exception e) {
            log.warn("解析URL失败");
        }
        return url;
    }

    public Heritage generateMockHeritage(int index) {
        Heritage heritage = new Heritage();
        heritage.setHeritageCode("HT" + String.format("%06d", index));
        heritage.setName("示例文物" + index);
        heritage.setCategory("瓷器");
        heritage.setSubCategory("青花瓷");
        heritage.setDynasty("明代");
        heritage.setPeriod("万历年间");
        heritage.setMaterial("陶瓷");
        heritage.setSize("高30cm，口径15cm，底径12cm");
        heritage.setOrigin("景德镇");
        heritage.setDiscoveryPlace("江西省景德镇市");
        heritage.setCurrentLocation("故宫博物院");
        heritage.setLevel("一级文物");
        heritage.setDescription("这是一件精美的明代青花瓷，具有重要的历史和艺术价值...");
        heritage.setDataSource("模拟数据");
        heritage.setDataType("结构化数据");
        heritage.setVerificationStatus("已验证");
        heritage.setQualityScore(95);
        return heritage;
    }
}
