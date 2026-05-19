package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.Heritage;
import com.heritage.repository.HeritageRepository;
import com.heritage.service.CrawlerService;
import com.heritage.service.DataCleaningService;
import com.heritage.service.MasterDataService;
import com.heritage.service.ValueEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/heritages")
@Tag(name = "文物管理", description = "文物数据CRUD操作")
@CrossOrigin(origins = "*")
public class HeritageController {

    @Autowired
    private HeritageRepository heritageRepository;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private DataCleaningService dataCleaningService;

    @Autowired
    private MasterDataService masterDataService;

    @Autowired
    private ValueEvaluationService valueEvaluationService;

    @GetMapping
    @Operation(summary = "获取文物列表")
    public Result<Page<Heritage>> getAllHeritages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(heritageRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取单个文物信息")
    public Result<Heritage> getHeritageById(@PathVariable Long id) {
        return heritageRepository.findById(id)
                .map(Result::success)
                .orElse(Result.error("文物不存在"));
    }

    @PostMapping
    @Operation(summary = "创建文物记录")
    public Result<Heritage> createHeritage(@RequestBody Heritage heritage) {
        heritage = dataCleaningService.correctData(heritage);
        Heritage saved = heritageRepository.save(heritage);
        return Result.success("创建成功", saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文物记录")
    public Result<Heritage> updateHeritage(@PathVariable Long id, @RequestBody Heritage heritageDetails) {
        return heritageRepository.findById(id)
                .map(heritage -> {
                    heritage.setName(heritageDetails.getName());
                    heritage.setCategory(heritageDetails.getCategory());
                    heritage.setDynasty(heritageDetails.getDynasty());
                    heritage.setMaterial(heritageDetails.getMaterial());
                    heritage.setSize(heritageDetails.getSize());
                    heritage.setOrigin(heritageDetails.getOrigin());
                    heritage.setLevel(heritageDetails.getLevel());
                    heritage.setDescription(heritageDetails.getDescription());
                    heritage = dataCleaningService.correctData(heritage);
                    return Result.success(heritageRepository.save(heritage));
                })
                .orElse(Result.error("文物不存在"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文物记录")
    public Result<Void> deleteHeritage(@PathVariable Long id) {
        if (heritageRepository.existsById(id)) {
            heritageRepository.deleteById(id);
            return Result.success();
        }
        return Result.error("文物不存在");
    }

    @GetMapping("/search")
    @Operation(summary = "搜索文物")
    public Result<Page<Heritage>> searchHeritages(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(heritageRepository.searchByKeyword(keyword, pageable));
    }

    @PostMapping("/generate-test-data")
    @Operation(summary = "生成测试数据")
    public Result<List<Heritage>> generateTestData(@RequestParam(defaultValue = "20") int count) {
        for (int i = 1; i <= count; i++) {
            Heritage heritage = crawlerService.generateMockHeritage(i);
            heritage = dataCleaningService.correctData(heritage);
            heritageRepository.save(heritage);
        }
        return Result.success("成功生成" + count + "条测试数据", heritageRepository.findAll());
    }

    @PostMapping("/{id}/convert-master")
    @Operation(summary = "转换为主数据")
    public Result<Heritage> convertToMaster(@PathVariable Long id) {
        return (Result<Heritage>) heritageRepository.findById(id)
                .map(heritage -> {
                    if (masterDataService.validateMasterData(heritage)) {
                        heritage = masterDataService.convertToMasterData(heritage);
                        return Result.success(heritageRepository.save(heritage));
                    }
                    return Result.error("数据质量不满足主数据要求");
                })
                .orElse(Result.error("文物不存在"));
    }

    @GetMapping("/{id}/evaluate-value")
    @Operation(summary = "文物价值评估")
    public Result<Map<String, Object>> evaluateValue(@PathVariable Long id) {
        return heritageRepository.findById(id)
                .map(heritage -> Result.success(valueEvaluationService.getValueEvaluationReport(heritage)))
                .orElse(Result.error("文物不存在"));
    }

    @GetMapping("/master-data")
    @Operation(summary = "获取主数据列表")
    public Result<Page<Heritage>> getMasterData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(heritageRepository.findMasterData(pageable));
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取文物统计信息")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<Heritage> all = heritageRepository.findAll();
        stats.put("total", all.size());
        stats.put("masterDataCount", all.stream().filter(h -> Boolean.TRUE.equals(h.getIsMasterData())).count());
        stats.put("highQualityCount", all.stream().filter(h -> h.getQualityScore() != null && h.getQualityScore() >= 80).count());
        return Result.success(stats);
    }

    @GetMapping("/clean/{id}")
    @Operation(summary = "数据清洗")
    public Result<Heritage> cleanData(@PathVariable Long id) {
        return heritageRepository.findById(id)
                .map(heritage -> {
                    heritage = dataCleaningService.correctData(heritage);
                    return Result.success(heritageRepository.save(heritage));
                })
                .orElse(Result.error("文物不存在"));
    }
}
