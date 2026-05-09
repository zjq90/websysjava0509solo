package com.example.websys.controller;

import com.example.websys.dto.Result;
import com.example.websys.dto.stat.StatItemCreateDTO;
import com.example.websys.dto.stat.StatItemUpdateDTO;
import com.example.websys.entity.StatItem;
import com.example.websys.repository.StatItemRepository;
import com.example.websys.service.StatItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 数据统计项REST API控制器
 * 提供统计项的增删改查接口
 * 新增功能：完整的前后端表单校验
 */
@RestController
@RequestMapping("/api/stat-items")
@CrossOrigin(origins = "*")
@Validated
public class StatItemController {

    @Autowired
    private StatItemService statItemService;

    @Autowired
    private StatItemRepository statItemRepository;

    @GetMapping
    public Result<List<StatItem>> getAll() {
        return Result.success(statItemService.getAll());
    }

    @GetMapping("/enabled")
    public Result<List<StatItem>> getAllEnabled() {
        return Result.success(statItemService.getAllEnabled());
    }

    @GetMapping("/default")
    public Result<List<StatItem>> getDefaultItems() {
        return Result.success(statItemService.getDefaultItems());
    }

    @GetMapping("/{id}")
    public Result<StatItem> getById(@PathVariable Long id) {
        Optional<StatItem> item = statItemService.getById(id);
        return item.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @PostMapping
    public Result<StatItem> save(@Valid @RequestBody StatItemCreateDTO dto) {
        StatItem existing = statItemRepository.findByItemCode(dto.getItemCode().trim());
        if (existing != null) {
            return Result.error("统计项编码已存在，请使用其他编码");
        }

        StatItem statItem = new StatItem();
        statItem.setItemCode(dto.getItemCode().trim());
        statItem.setItemName(dto.getItemName().trim());
        statItem.setItemType(dto.getItemType());
        statItem.setDataSource(dto.getDataSource());
        statItem.setUnit(dto.getUnit());
        statItem.setIcon(dto.getIcon());
        statItem.setColor(dto.getColor());
        statItem.setIsDefault(dto.getIsDefault());
        statItem.setSortOrder(dto.getSortOrder());
        statItem.setStatus(dto.getStatus());
        statItem.setDescription(dto.getDescription());

        StatItem saved = statItemService.save(statItem);
        return Result.success("保存成功", saved);
    }

    @PutMapping
    public Result<StatItem> update(@Valid @RequestBody StatItemUpdateDTO dto) {
        Optional<StatItem> existingOpt = statItemService.getById(dto.getId());
        if (!existingOpt.isPresent()) {
            return Result.error("数据不存在");
        }

        StatItem existing = existingOpt.get();
        
        if (dto.getItemCode() != null && !dto.getItemCode().equals(existing.getItemCode())) {
            StatItem sameCode = statItemRepository.findByItemCode(dto.getItemCode().trim());
            if (sameCode != null) {
                return Result.error("统计项编码已存在，请使用其他编码");
            }
            existing.setItemCode(dto.getItemCode().trim());
        }

        if (dto.getItemName() != null) {
            existing.setItemName(dto.getItemName().trim());
        }
        if (dto.getItemType() != null) {
            existing.setItemType(dto.getItemType());
        }
        if (dto.getDataSource() != null) {
            existing.setDataSource(dto.getDataSource());
        }
        if (dto.getUnit() != null) {
            existing.setUnit(dto.getUnit());
        }
        if (dto.getIcon() != null) {
            existing.setIcon(dto.getIcon());
        }
        if (dto.getColor() != null) {
            existing.setColor(dto.getColor());
        }
        if (dto.getIsDefault() != null) {
            existing.setIsDefault(dto.getIsDefault());
        }
        if (dto.getSortOrder() != null) {
            existing.setSortOrder(dto.getSortOrder());
        }
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }
        if (dto.getDescription() != null) {
            existing.setDescription(dto.getDescription());
        }

        StatItem updated = statItemService.save(existing);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        statItemService.deleteById(id);
        return Result.success();
    }
}
