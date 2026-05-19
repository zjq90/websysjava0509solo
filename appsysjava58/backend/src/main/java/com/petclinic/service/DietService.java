package com.petclinic.service;

import com.petclinic.dto.Result;
import com.petclinic.entity.DietSuggestion;
import com.petclinic.entity.Pet;
import com.petclinic.repository.DietSuggestionRepository;
import com.petclinic.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 饮食建议服务类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DietService {

    private final DietSuggestionRepository dietSuggestionRepository;
    private final PetRepository petRepository;

    /**
     * 权威宠物营养学文献引用
     */
    private static final String[] NUTRITION_REFERENCES = {
        "AAFCO (Association of American Feed Control Officials) - Dog and Cat Food Nutrient Profiles, 2023",
        "NRC (National Research Council) - Nutrient Requirements of Dogs and Cats, 2006",
        "WSAVA (World Small Animal Veterinary Association) - Global Nutrition Guidelines, 2022",
        "FEDIAF (European Pet Food Industry Federation) - Nutritional Guidelines for Complete and Complementary Pet Food for Dogs and Cats, 2021"
    };

    /**
     * 生成宠物饮食建议
     * 
     * @param petId 宠物ID
     * @return 饮食建议
     */
    public Result<DietSuggestion> generateDietSuggestion(Long petId) {
        Optional<Pet> petOpt = petRepository.findById(petId);
        if (petOpt.isEmpty()) {
            return Result.error("宠物不存在");
        }

        Pet pet = petOpt.get();
        DietSuggestion suggestion = calculateDietSuggestion(pet);
        DietSuggestion saved = dietSuggestionRepository.save(suggestion);
        
        log.info("生成饮食建议成功，宠物ID: {}", petId);
        return Result.success(saved);
    }

    /**
     * 计算饮食建议
     */
    private DietSuggestion calculateDietSuggestion(Pet pet) {
        DietSuggestion suggestion = new DietSuggestion();
        suggestion.setPet(pet);
        suggestion.setGeneratedTime(LocalDateTime.now());

        String type = pet.getType() != null ? pet.getType().toLowerCase() : "dog";
        BigDecimal weight = pet.getWeight() != null ? pet.getWeight() : new BigDecimal("10");

        int calories = calculateCalories(type, weight);
        suggestion.setCalorieRecommendation(calories);

        String content = generateDietContent(type, weight, calories);
        suggestion.setContent(content);

        suggestion.setTitle(pet.getName() + "的个性化饮食建议");
        suggestion.setFoodType(recommendFoodType(type));
        suggestion.setFeedingFrequency(recommendFeedingFrequency(type));
        suggestion.setReferenceSource(NUTRITION_REFERENCES[0] + "; " + NUTRITION_REFERENCES[1]);
        suggestion.setReferenceUrl("https://www.aafco.org");

        return suggestion;
    }

    /**
     * 计算卡路里需求量
     */
    private int calculateCalories(String type, BigDecimal weight) {
        if ("cat".equals(type)) {
            return (int) (weight.doubleValue() * 40 + 70);
        } else {
            return (int) (weight.doubleValue() * 30 + 70);
        }
    }

    /**
     * 生成详细饮食建议内容
     */
    private String generateDietContent(String type, BigDecimal weight, int calories) {
        StringBuilder content = new StringBuilder();
        
        content.append("【基本营养需求】\n");
        content.append("根据").append(NUTRITION_REFERENCES[0]).append("标准计算：\n");
        content.append("• 每日推荐热量：").append(calories).append(" kcal\n");
        content.append("• 蛋白质需求：占总热量的").append("cat".equals(type) ? "26%" : "18%").append("以上\n");
        content.append("• 脂肪需求：占总热量的").append("cat".equals(type) ? "9%" : "5%").append("以上\n\n");

        content.append("【喂食建议】\n");
        if ("cat".equals(type)) {
            content.append("1. 定时定量：建议每日分3-4次喂食\n");
            content.append("2. 干湿搭配：干粮为主，配合湿粮增加水分摄入\n");
            content.append("3. 牛磺酸：必须确保食物中含有足够的牛磺酸（≥0.1%）\n");
        } else {
            content.append("1. 定时定量：建议每日分2-3次喂食\n");
            content.append("2. 营养均衡：确保蛋白质、脂肪、碳水化合物比例合理\n");
            content.append("3. 钙质补充：大型幼犬需特别注意钙质补充\n");
        }

        content.append("\n【文献依据】\n");
        for (String ref : NUTRITION_REFERENCES) {
            content.append("• ").append(ref).append("\n");
        }

        return content.toString();
    }

    /**
     * 推荐食物类型
     */
    private String recommendFoodType(String type) {
        if ("cat".equals(type)) {
            return "全价猫粮（干湿搭配）";
        } else {
            return "全价狗粮（按体型选择）";
        }
    }

    /**
     * 推荐喂食频率
     */
    private String recommendFeedingFrequency(String type) {
        if ("cat".equals(type)) {
            return "每日3-4次";
        } else {
            return "每日2-3次";
        }
    }

    /**
     * 获取宠物饮食建议历史
     */
    public Result<List<DietSuggestion>> getDietHistory(Long petId) {
        List<DietSuggestion> suggestions = dietSuggestionRepository
                .findByPetIdAndDeletedFalseOrderByGeneratedTimeDesc(petId);
        return Result.success(suggestions);
    }
}