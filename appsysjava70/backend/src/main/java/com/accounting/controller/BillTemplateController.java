package com.accounting.controller;

import com.accounting.dto.ApiResponse;
import com.accounting.entity.BillTemplate;
import com.accounting.service.BillTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
@Tag(name = "模板管理", description = "账单模板的增删改查接口")
public class BillTemplateController {

    private final BillTemplateService billTemplateService;

    @GetMapping
    @Operation(summary = "获取全部模板", description = "获取所有账单模板，按使用次数倒序排列")
    public ApiResponse<List<BillTemplate>> getAllTemplates() {
        log.info("API: 获取全部模板");
        return ApiResponse.success(billTemplateService.getAllTemplates());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取模板", description = "根据模板ID获取模板详情")
    public ApiResponse<BillTemplate> getTemplateById(
            @Parameter(description = "模板ID", required = true)
            @PathVariable Long id) {
        log.info("API: 根据ID获取模板: {}", id);
        return ApiResponse.success(billTemplateService.getTemplateById(id));
    }

    @PostMapping
    @Operation(summary = "创建模板", description = "创建新的账单模板，可设置默认分类、账户、金额等")
    public ApiResponse<BillTemplate> createTemplate(
            @RequestBody BillTemplate template) {
        log.info("API: 创建模板: {}", template.getName());
        return ApiResponse.success("模板创建成功", billTemplateService.createTemplate(template));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新模板", description = "更新模板信息")
    public ApiResponse<BillTemplate> updateTemplate(
            @Parameter(description = "模板ID", required = true)
            @PathVariable Long id,
            @RequestBody BillTemplate template) {
        log.info("API: 更新模板: {}", id);
        return ApiResponse.success("模板更新成功", billTemplateService.updateTemplate(id, template));
    }

    @PostMapping("/{id}/use")
    @Operation(summary = "使用模板", description = "使用模板时调用，增加使用次数计数")
    public ApiResponse<BillTemplate> useTemplate(
            @Parameter(description = "模板ID", required = true)
            @PathVariable Long id) {
        log.info("API: 使用模板: {}", id);
        return ApiResponse.success(billTemplateService.incrementUseCount(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板", description = "删除指定模板")
    public ApiResponse<Void> deleteTemplate(
            @Parameter(description = "模板ID", required = true)
            @PathVariable Long id) {
        log.info("API: 删除模板: {}", id);
        billTemplateService.deleteTemplate(id);
        return ApiResponse.success("模板删除成功", null);
    }
}
