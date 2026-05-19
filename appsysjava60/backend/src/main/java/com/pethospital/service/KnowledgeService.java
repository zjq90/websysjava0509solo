package com.pethospital.service;

import com.pethospital.dto.SearchResultDTO;
import com.pethospital.entity.Case;
import com.pethospital.entity.Disease;
import com.pethospital.entity.Medicine;
import com.pethospital.repository.CaseRepository;
import com.pethospital.repository.DiseaseRepository;
import com.pethospital.repository.MedicineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库服务
 * 提供疾病、药品、案例的搜索和查询功能
 * 
 * @author Pet Hospital Team
 */
@Slf4j
@Service
public class KnowledgeService {
    
    @Autowired
    private DiseaseRepository diseaseRepository;
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    @Autowired
    private CaseRepository caseRepository;
    
    /**
     * 搜索知识库（支持语音输入的症状描述）
     * 
     * @param keyword 搜索关键词/症状描述
     * @return 搜索结果列表，按相关性排序
     */
    @Cacheable(value = "knowledgeSearch", key = "#keyword")
    public List<SearchResultDTO> search(String keyword) {
        log.info("搜索知识库，关键词：{}", keyword);
        List<SearchResultDTO> results = new ArrayList<>();
        
        // 搜索疾病
        List<Disease> diseases = diseaseRepository.searchByKeyword(keyword, 1);
        for (Disease disease : diseases) {
            SearchResultDTO dto = new SearchResultDTO();
            dto.setId(disease.getId());
            dto.setType("disease");
            dto.setPetType(disease.getPetType());
            // 计算相关性分数
            double score = calculateRelevance(keyword, disease.getName(), disease.getSymptoms(), disease.getKeywords());
            dto.setRelevanceScore(score);
            // 高亮关键词
            dto.setTitle(highlightKeyword(disease.getName(), keyword));
            dto.setSummary(highlightKeyword(truncateText(disease.getSymptoms(), 200), keyword));
            results.add(dto);
        }
        
        // 搜索 药品
        List<Medicine> medicines = medicineRepository.searchByKeyword(keyword, 1);
        for (Medicine medicine : medicines) {
            SearchResultDTO dto = new SearchResultDTO();
            dto.setId(medicine.getId());
            dto.setType("medicine");
            dto.setPetType("通用");
            // 计算相关性分数
            double score = calculateRelevance(keyword, medicine.getName(), medicine.getIndication(), medicine.getKeywords());
            dto.setRelevanceScore(score);
            // 高亮关键词
            dto.setTitle(highlightKeyword(medicine.getName(), keyword));
            dto.setSummary(highlightKeyword(truncateText(medicine.getIndication(), 200), keyword));
            results.add(dto);
        }
        
        // 搜索案例
        List<Case> cases = caseRepository.searchByKeyword(keyword, 1);
        for (Case c : cases) {
            SearchResultDTO dto = new SearchResultDTO();
            dto.setId(c.getId());
            dto.setType("case");
            dto.setPetType(c.getPetType());
            // 计算相关性分数
            double score = calculateRelevance(keyword, c.getTitle(), c.getChiefComplaint(), c.getKeywords());
            dto.setRelevanceScore(score);
            // 高亮关键词
            dto.setTitle(highlightKeyword(c.getTitle(), keyword));
            dto.setSummary(highlightKeyword(truncateText(c.getChiefComplaint(), 200), keyword));
            results.add(dto);
        }
        
        // 按相关性分数降序排序
        return results.stream()
                .sorted(Comparator.comparingDouble(SearchResultDTO::getRelevanceScore).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * 计算相关性分数
     * 基于关键词在不同字段中的出现次数和位置
     */
    private double calculateRelevance(String keyword, String... fields) {
        double score = 0.0;
        String lowerKeyword = keyword.toLowerCase();
        
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null) continue;
            String lowerField = fields[i].toLowerCase();
            
            // 完全匹配得分最高
            if (lowerField.equals(lowerKeyword)) {
                score += 100 * (fields.length - i);
            }
            
            // 字段以关键词开头
            if (lowerField.startsWith(lowerKeyword)) {
                score += 50 * (fields.length - i);
            }
            
            // 包含关键词
            if (lowerField.contains(lowerKeyword)) {
                // 计算关键词出现次数
                int count = (lowerField.length() - lowerField.replace(lowerKeyword, "").length()) 
                           / lowerKeyword.length();
                score += count * 10 * (fields.length - i);
            }
        }
        
        return score;
    }
    
    /**
     * 高亮关键词
     */
    private String highlightKeyword(String text, String keyword) {
        if (text == null || keyword == null) {
            return text;
        }
        // 使用HTML标签标记关键词，前端可以自定义样式
        return text.replaceAll("(?i)" + keyword, "<em>$0</em>");
    }
    
    /**
     * 截断文本
     */
    private String truncateText(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
    
    /**
     * 获取疾病详情
     */
    public Disease getDiseaseById(Long id) {
        return diseaseRepository.findById(id).orElse(null);
    }
    
    /**
     * 获取药品详情
     */
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id).orElse(null);
    }
    
    /**
     * 获取案例详情
     */
    public Case getCaseById(Long id) {
        return caseRepository.findById(id).map(c -> {
            c.setViewCount(c.getViewCount() + 1);
            return caseRepository.save(c);
        }).orElse(null);
    }
    
    /**
     * 获取疾病列表
     */
    public List<Disease> getDiseaseList(String petType) {
        if (petType != null && !petType.isEmpty()) {
            return diseaseRepository.findByPetTypeAndStatus(petType, 1);
        }
        return diseaseRepository.findByStatus(1);
    }
    
    /**
     * 获取药品列表
     */
    public List<Medicine> getMedicineList(String category) {
        if (category != null && !category.isEmpty()) {
            return medicineRepository.findByCategoryAndStatus(category, 1);
        }
        return medicineRepository.findByStatus(1);
    }
    
    /**
     * 获取案例列表
     */
    public List<Case> getCaseList(String petType) {
        if (petType != null && !petType.isEmpty()) {
            return caseRepository.findByPetTypeAndStatus(petType, 1);
        }
        return caseRepository.findByStatus(1);
    }
}
