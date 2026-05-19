package com.petclinic.controller;

import com.petclinic.dto.Result;
import com.petclinic.entity.DietSuggestion;
import com.petclinic.service.DietService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 饮食建议控制器
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/diet")
@RequiredArgsConstructor
@Tag(name = "饮食建议", description = "宠物饮食建议相关接口")
public class DietController {

    private final DietService dietService;

    @PostMapping("/generate/{petId}")
    @Operation(summary = "生成饮食建议", description = "根据宠物品种、体重生成个性化饮食建议，引用权威营养学文献")
    public Result<DietSuggestion> generateDietSuggestion(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return dietService.generateDietSuggestion(petId);
    }

    @GetMapping("/history/{petId}")
    @Operation(summary = "获取饮食建议历史", description = "获取宠物的历史饮食建议记录")
    public Result<List<DietSuggestion>> getDietHistory(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return dietService.getDietHistory(petId);
    }
}