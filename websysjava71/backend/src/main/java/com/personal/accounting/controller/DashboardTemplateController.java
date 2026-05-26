package com.personal.accounting.controller;

import com.personal.accounting.entity.DashboardTemplate;
import com.personal.accounting.repository.DashboardTemplateRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 看板模板控制器
 * 提供自定义看板模板的管理API
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/dashboard-templates")
@RequiredArgsConstructor
@Tag(name = "看板模板管理", description = "自定义看板模板的保存与加载API")
public class DashboardTemplateController {

    private final DashboardTemplateRepository dashboardTemplateRepository;

    @GetMapping
    @Operation(summary = "获取所有看板模板", description = "获取所有保存的看板模板")
    public ResponseEntity<List<DashboardTemplate>> findAll() {
        return ResponseEntity.ok(dashboardTemplateRepository.findAll());
    }

    @GetMapping("/default")
    @Operation(summary = "获取默认模板", description = "获取标记为默认的看板模板")
    public ResponseEntity<DashboardTemplate> getDefaultTemplate() {
        Optional<DashboardTemplate> template = dashboardTemplateRepository.findByIsDefaultTrue();
        return template.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取模板", description = "根据ID获取看板模板详细信息")
    public ResponseEntity<DashboardTemplate> findById(@PathVariable Long id) {
        return dashboardTemplateRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "保存看板模板", description = "保存自定义的看板布局为模板")
    public ResponseEntity<DashboardTemplate> save(@RequestBody DashboardTemplate template) {
        return new ResponseEntity<>(dashboardTemplateRepository.save(template), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新看板模板", description = "根据ID更新看板模板信息")
    public ResponseEntity<DashboardTemplate> update(
            @PathVariable Long id,
            @RequestBody DashboardTemplate template) {
        
        return dashboardTemplateRepository.findById(id)
                .map(existing -> {
                    existing.setName(template.getName());
                    existing.setDescription(template.getDescription());
                    existing.setLayoutConfig(template.getLayoutConfig());
                    existing.setIsDefault(template.getIsDefault());
                    return ResponseEntity.ok(dashboardTemplateRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除看板模板", description = "根据ID删除看板模板")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (dashboardTemplateRepository.existsById(id)) {
            dashboardTemplateRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/set-default")
    @Operation(summary = "设为默认模板", description = "将指定模板设为默认模板")
    public ResponseEntity<DashboardTemplate> setDefault(@PathVariable Long id) {
        return dashboardTemplateRepository.findById(id)
                .map(template -> {
                    // 取消其他默认模板
                    dashboardTemplateRepository.findByIsDefaultTrue()
                            .ifPresent(defaultTemplate -> {
                                defaultTemplate.setIsDefault(false);
                                dashboardTemplateRepository.save(defaultTemplate);
                            });
                    // 设置新的默认模板
                    template.setIsDefault(true);
                    return ResponseEntity.ok(dashboardTemplateRepository.save(template));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
