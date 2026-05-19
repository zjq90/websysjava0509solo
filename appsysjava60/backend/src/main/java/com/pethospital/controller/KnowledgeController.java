package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.dto.SearchResultDTO;
import com.pethospital.entity.Case;
import com.pethospital.entity.Disease;
import com.pethospital.entity.Medicine;
import com.pethospital.service.KnowledgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库控制器
 * 提供疾病、药品、案例的搜索和查询接口
 * 
 * @author Pet Hospital Team
 */
@Slf4j
@RestController
@RequestMapping("/api/knowledge")
@Tag(name = "知识库管理", description = "疾病、药品、案例相关接口")
public class KnowledgeController {
    
    @Autowired
    private KnowledgeService knowledgeService;
    
    /**
     * 搜索知识库（支持语音输入的症状描述）
     * 
     * @param keyword 搜索关键词/症状描述，如"猫呕吐带血"
     * @return 搜索结果列表，按相关性排序
     */
    @GetMapping("/search")
    @Operation(summary = "搜索知识库", description = "根据关键词或症状描述搜索疾病、药品、案例，支持语音输入转文字后搜索")
    public Result<List<SearchResultDTO>> search(
            @Parameter(description = "搜索关键词/症状描述") @RequestParam String keyword) {
        log.info("搜索知识库，关键词：{}", keyword);
        List<SearchResultDTO> results = knowledgeService.search(keyword);
        return Result.success(results);
    }
    
    /**
     * 获取疾病详情
     * 
     * @param id 疾病ID
     * @return 疾病详情
     */
    @GetMapping("/disease/{id}")
    @Operation(summary = "获取疾病详情", description = "根据疾病ID获取疾病详细信息，包括症状、治疗方案等")
    public Result<Disease> getDiseaseById(
            @Parameter(description = "疾病ID") @PathVariable Long id) {
        Disease disease = knowledgeService.getDiseaseById(id);
        if (disease == null) {
            return Result.error("疾病不存在");
        }
        return Result.success(disease);
    }
    
    /**
     * 获取药品详情
     * 
     * @param id 药品ID
     * @return 药品详情
     */
    @GetMapping("/medicine/{id}")
    @Operation(summary = "获取药品详情", description = "根据药品ID获取药品详细信息，包括用法用量、禁忌等")
    public Result<Medicine> getMedicineById(
            @Parameter(description = "药品ID") @PathVariable Long id) {
        Medicine medicine = knowledgeService.getMedicineById(id);
        if (medicine == null) {
            return Result.error("药品不存在");
        }
        return Result.success(medicine);
    }
    
    /**
     * 获取案例详情
     * 
     * @param id 案例ID
     * @return 案例详情
     */
    @GetMapping("/case/{id}")
    @Operation(summary = "获取案例详情", description = "根据案例ID获取案例详细信息，包括主诉、诊断、治疗方案等")
    public Result<Case> getCaseById(
            @Parameter(description = "案例ID") @PathVariable Long id) {
        Case c = knowledgeService.getCaseById(id);
        if (c == null) {
            return Result.error("案例不存在");
        }
        return Result.success(c);
    }
    
    /**
     * 获取疾病列表
     * 
     * @param petType 宠物类型（可选），如"猫"、"狗"
     * @return 疾病列表
     */
    @GetMapping("/disease/list")
    @Operation(summary = "获取疾病列表", description = "获取所有疾病列表，可按宠物类型筛选")
    public Result<List<Disease>> getDiseaseList(
            @Parameter(description = "宠物类型") @RequestParam(required = false) String petType) {
        List<Disease> list = knowledgeService.getDiseaseList(petType);
        return Result.success(list);
    }
    
    /**
     * 获取药品列表
     * 
     * @param category 药品分类（可选）
     * @return 药品列表
     */
    @GetMapping("/medicine/list")
    @Operation(summary = "获取药品列表", description = "获取所有药品列表，可按分类筛选")
    public Result<List<Medicine>> getMedicineList(
            @Parameter(description = "药品分类") @RequestParam(required = false) String category) {
        List<Medicine> list = knowledgeService.getMedicineList(category);
        return Result.success(list);
    }
    
    /**
     * 获取案例列表
     * 
     * @param petType 宠物类型（可选）
     * @return 案例列表
     */
    @GetMapping("/case/list")
    @Operation(summary = "获取案例列表", description = "获取所有案例列表，可按宠物类型筛选")
    public Result<List<Case>> getCaseList(
            @Parameter(description = "宠物类型") @RequestParam(required = false) String petType) {
        List<Case> list = knowledgeService.getCaseList(petType);
        return Result.success(list);
    }
}
