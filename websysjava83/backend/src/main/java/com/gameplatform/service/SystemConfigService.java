package com.gameplatform.service;

import com.gameplatform.dto.SystemConfigDTO;
import com.gameplatform.entity.SystemConfig;
import com.gameplatform.repository.SystemConfigRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    private static final String SITE_CONFIG_KEY = "site_config";
    private static final String SEO_CONFIG_KEY = "seo_config";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SystemConfigDTO getSystemConfig() {
        SystemConfigDTO dto = new SystemConfigDTO();

        systemConfigRepository.findByConfigKey(SITE_CONFIG_KEY).ifPresent(config -> {
            try {
                @SuppressWarnings("unchecked")
                Map<String, String> siteConfig = objectMapper.readValue(config.getConfigValue(), Map.class);
                dto.setSiteName(siteConfig.get("siteName"));
                dto.setSiteLogo(siteConfig.get("siteLogo"));
                dto.setCopyright(siteConfig.get("copyright"));
                dto.setContactEmail(siteConfig.get("contactEmail"));
                dto.setContactPhone(siteConfig.get("contactPhone"));
                dto.setWechatLink(siteConfig.get("wechatLink"));
                dto.setWeiboLink(siteConfig.get("weiboLink"));
                dto.setTwitterLink(siteConfig.get("twitterLink"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        systemConfigRepository.findByConfigKey(SEO_CONFIG_KEY).ifPresent(config -> {
            try {
                @SuppressWarnings("unchecked")
                Map<String, String> seoConfig = objectMapper.readValue(config.getConfigValue(), Map.class);
                dto.setSeoTitle(seoConfig.get("seoTitle"));
                dto.setSeoDescription(seoConfig.get("seoDescription"));
                dto.setSeoKeywords(seoConfig.get("seoKeywords"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return dto;
    }

    public SystemConfigDTO updateSystemConfig(SystemConfigDTO configDTO) {
        Map<String, String> siteConfig = new HashMap<>();
        siteConfig.put("siteName", configDTO.getSiteName());
        siteConfig.put("siteLogo", configDTO.getSiteLogo());
        siteConfig.put("copyright", configDTO.getCopyright());
        siteConfig.put("contactEmail", configDTO.getContactEmail());
        siteConfig.put("contactPhone", configDTO.getContactPhone());
        siteConfig.put("wechatLink", configDTO.getWechatLink());
        siteConfig.put("weiboLink", configDTO.getWeiboLink());
        siteConfig.put("twitterLink", configDTO.getTwitterLink());

        saveOrUpdateConfig(SITE_CONFIG_KEY, siteConfig, "站点基础配置");

        Map<String, String> seoConfig = new HashMap<>();
        seoConfig.put("seoTitle", configDTO.getSeoTitle());
        seoConfig.put("seoDescription", configDTO.getSeoDescription());
        seoConfig.put("seoKeywords", configDTO.getSeoKeywords());

        saveOrUpdateConfig(SEO_CONFIG_KEY, seoConfig, "SEO配置");

        return configDTO;
    }

    private void saveOrUpdateConfig(String key, Map<String, String> value, String description) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElse(new SystemConfig());
            config.setConfigKey(key);
            config.setConfigValue(jsonValue);
            config.setDescription(description);
            systemConfigRepository.save(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
