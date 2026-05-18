package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.Heritage;
import com.heritage.enums.AuditStatus;
import com.heritage.enums.RiskLevel;
import com.heritage.service.HeritageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/heritage")
@CrossOrigin(origins = "*")
@Tag(name = "文物管理", description = "文物管理相关接口")
public class HeritageController {

    @Autowired
    private HeritageService heritageService;

    @GetMapping
    @Operation(summary = "获取所有文物")
    public Result<List<Heritage>> findAll() {
        return Result.success(heritageService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取文物")
    public Result<Heritage> findById(@PathVariable Long id) {
        return heritageService.findById(id)
                .map(Result::success)
                .orElse(Result.error("文物不存在"));
    }

    @PostMapping
    @Operation(summary = "创建文物")
    public Result<Heritage> create(@RequestBody Heritage heritage) {
        return Result.success(heritageService.save(heritage));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文物")
    public Result<Heritage> update(@PathVariable Long id, @RequestBody Heritage heritage) {
        heritage.setId(id);
        return Result.success(heritageService.save(heritage));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文物")
    public Result<Void> delete(@PathVariable Long id) {
        heritageService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/pending-audit")
    @Operation(summary = "获取待审核文物列表（按风险等级排序）")
    public Result<List<Heritage>> getPendingAudit() {
        return Result.success(heritageService.findPendingAudit());
    }

    @GetMapping("/risk-level/{level}")
    @Operation(summary = "根据风险等级获取文物")
    public Result<List<Heritage>> getByRiskLevel(@PathVariable RiskLevel level) {
        return Result.success(heritageService.findByRiskLevel(level));
    }

    @PostMapping("/{id}/audit")
    @Operation(summary = "审核文物")
    public Result<Heritage> auditHeritage(@PathVariable Long id,
                                           @RequestParam AuditStatus status,
                                           @RequestParam(required = false) String remark,
                                           @RequestParam(required = false) Long auditorId) {
        return Result.success(heritageService.auditHeritage(id, status, remark, auditorId));
    }

    @PostMapping("/{id}/verify-api")
    @Operation(summary = "对接国家文物局API验证来源")
    public Result<Heritage> verifyWithApi(@PathVariable Long id) {
        return Result.success(heritageService.verifyWithApi(id));
    }

    @GetMapping("/dynasty/{dynasty}")
    @Operation(summary = "按朝代分类")
    public Result<List<Heritage>> getByDynasty(@PathVariable String dynasty) {
        return Result.success(heritageService.findByDynasty(dynasty));
    }

    @GetMapping("/material/{material}")
    @Operation(summary = "按材质分类")
    public Result<List<Heritage>> getByMaterial(@PathVariable String material) {
        return Result.success(heritageService.findByMaterial(material));
    }

    @GetMapping("/usage/{usageType}")
    @Operation(summary = "按用途分类")
    public Result<List<Heritage>> getByUsageType(@PathVariable String usageType) {
        return Result.success(heritageService.findByUsageType(usageType));
    }

    @GetMapping("/province/{province}")
    @Operation(summary = "按省份获取文物")
    public Result<List<Heritage>> getByProvince(@PathVariable String province) {
        return Result.success(heritageService.findByProvince(province));
    }
}
