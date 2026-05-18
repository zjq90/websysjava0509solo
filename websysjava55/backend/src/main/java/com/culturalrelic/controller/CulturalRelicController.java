package com.culturalrelic.controller;

import com.culturalrelic.common.Result;
import com.culturalrelic.entity.CulturalRelic;
import com.culturalrelic.service.CulturalRelicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 文物管理控制器
 */
@RestController
@RequestMapping("/relic")
@Tag(name = "文物管理", description = "文物的增删改查等操作")
public class CulturalRelicController {

    @Autowired
    private CulturalRelicService culturalRelicService;

    /**
     * 新增文物
     */
    @PostMapping
    @Operation(summary = "新增文物", description = "创建新的文物记录")
    public Result<CulturalRelic> save(@RequestBody CulturalRelic relic) {
        CulturalRelic saved = culturalRelicService.save(relic);
        return Result.success("新增成功", saved);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询文物详情", description = "根据ID查询文物详细信息")
    public Result<CulturalRelic> findById(@Parameter(description = "文物ID") @PathVariable Long id) {
        Optional<CulturalRelic> relic = culturalRelicService.findById(id);
        if (relic.isPresent()) {
            return Result.success(relic.get());
        }
        return Result.error("文物不存在");
    }

    /**
     * 分页查询所有
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询文物", description = "分页查询所有文物列表")
    public Result<Page<CulturalRelic>> findAll(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<CulturalRelic> result = culturalRelicService.findAll(pageable);
        return Result.success(result);
    }

    /**
     * 查询所有
     */
    @GetMapping
    @Operation(summary = "查询所有文物", description = "获取所有文物列表")
    public Result<List<CulturalRelic>> findAll() {
        List<CulturalRelic> list = culturalRelicService.findAll();
        return Result.success(list);
    }

    /**
     * 更新文物
     */
    @PutMapping
    @Operation(summary = "更新文物", description = "更新文物信息")
    public Result<CulturalRelic> update(@RequestBody CulturalRelic relic) {
        CulturalRelic updated = culturalRelicService.update(relic);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除文物
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除文物", description = "根据ID删除文物（逻辑删除）")
    public Result<Void> delete(@Parameter(description = "文物ID") @PathVariable Long id) {
        boolean success = culturalRelicService.delete(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 根据文物编号查询
     */
    @GetMapping("/no/{relicNo}")
    @Operation(summary = "根据编号查询", description = "根据文物编号查询文物")
    public Result<CulturalRelic> findByRelicNo(@Parameter(description = "文物编号") @PathVariable String relicNo) {
        CulturalRelic relic = culturalRelicService.findByRelicNo(relicNo);
        return relic != null ? Result.success(relic) : Result.error("文物不存在");
    }

    /**
     * 根据类别查询
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "根据类别查询", description = "根据文物类别查询文物列表")
    public Result<List<CulturalRelic>> findByCategory(@Parameter(description = "文物类别") @PathVariable String category) {
        List<CulturalRelic> list = culturalRelicService.findByCategory(category);
        return Result.success(list);
    }

    /**
     * 根据朝代查询
     */
    @GetMapping("/dynasty/{dynasty}")
    @Operation(summary = "根据朝代查询", description = "根据文物朝代查询文物列表")
    public Result<List<CulturalRelic>> findByDynasty(@Parameter(description = "朝代") @PathVariable String dynasty) {
        List<CulturalRelic> list = culturalRelicService.findByDynasty(dynasty);
        return Result.success(list);
    }

    /**
     * 搜索文物
     */
    @GetMapping("/search")
    @Operation(summary = "搜索文物", description = "根据名称模糊搜索文物")
    public Result<List<CulturalRelic>> searchByName(@Parameter(description = "搜索关键词") @RequestParam String keyword) {
        List<CulturalRelic> list = culturalRelicService.searchByName(keyword);
        return Result.success(list);
    }
}
