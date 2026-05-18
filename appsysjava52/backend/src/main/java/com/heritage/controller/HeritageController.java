package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.Heritage;
import com.heritage.repository.HeritageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 文物展示Controller
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/heritage")
@Tag(name = "文物展示", description = "文物展示相关接口")
public class HeritageController {

    @Autowired
    private HeritageRepository heritageRepository;

    /**
     * 分页查询文物列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询文物列表")
    public Result<Page<Heritage>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "类别") @RequestParam(required = false) Integer category,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<Heritage> result;
        
        if (keyword != null && !keyword.isEmpty()) {
            result = heritageRepository.findByNameContainingAndStatusAndDeleted(keyword, 1, 0, pageRequest);
        } else if (category != null) {
            result = heritageRepository.findByCategoryAndStatusAndDeleted(category, 1, 0, pageRequest);
        } else {
            result = heritageRepository.findByIsPublicAndStatusAndDeleted(1, 1, 0, pageRequest);
        }
        
        return Result.success(result);
    }

    /**
     * 获取文物详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取文物详情")
    @Transactional
    public Result<Heritage> detail(@PathVariable Long id) {
        Optional<Heritage> optional = heritageRepository.findById(id);
        if (optional.isPresent()) {
            Heritage heritage = optional.get();
            if (heritage.getStatus() == 1 && heritage.getDeleted() == 0) {
                heritageRepository.incrementViewCount(id);
                heritage.setViewCount(heritage.getViewCount() + 1);
                return Result.success(heritage);
            }
        }
        return Result.error("文物不存在或已下架");
    }

    /**
     * 获取热门文物
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门文物")
    public Result<List<Heritage>> hot() {
        List<Heritage> list = heritageRepository.findTop10ByStatusAndDeletedOrderByViewCountDesc(1, 0);
        return Result.success(list);
    }

    /**
     * 新增文物
     */
    @PostMapping
    @Operation(summary = "新增文物")
    public Result<Heritage> add(@RequestBody Heritage heritage) {
        heritage.setStatus(1);
        heritage.setDeleted(0);
        heritage.setViewCount(0);
        heritage.setFavoriteCount(0);
        Heritage saved = heritageRepository.save(heritage);
        return Result.success("添加成功", saved);
    }

    /**
     * 更新文物
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新文物")
    public Result<Heritage> update(@PathVariable Long id, @RequestBody Heritage heritage) {
        Optional<Heritage> optional = heritageRepository.findById(id);
        if (optional.isPresent()) {
            Heritage existing = optional.get();
            existing.setName(heritage.getName());
            existing.setCategory(heritage.getCategory());
            existing.setPeriod(heritage.getPeriod());
            existing.setDescription(heritage.getDescription());
            existing.setMainImage(heritage.getMainImage());
            existing.setDetailImages(heritage.getDetailImages());
            existing.setModel3d(heritage.getModel3d());
            existing.setHistoryStory(heritage.getHistoryStory());
            existing.setRestorationStory(heritage.getRestorationStory());
            existing.setMaterial(heritage.getMaterial());
            existing.setExcavationSite(heritage.getExcavationSite());
            heritageRepository.save(existing);
            return Result.success("更新成功", existing);
        }
        return Result.error("文物不存在");
    }

    /**
     * 删除文物
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除文物")
    public Result<Void> delete(@PathVariable Long id) {
        Optional<Heritage> optional = heritageRepository.findById(id);
        if (optional.isPresent()) {
            Heritage heritage = optional.get();
            heritage.setDeleted(1);
            heritageRepository.save(heritage);
            return Result.success();
        }
        return Result.error("文物不存在");
    }
}