package com.flowerstore.backend.controller;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.EnterpriseCustom;
import com.flowerstore.backend.repository.EnterpriseCustomRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 企业定制控制器
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/enterprise")
@Tag(name = "企业定制", description = "企业定制相关接口")
public class EnterpriseController {

    @Autowired
    private EnterpriseCustomRepository enterpriseCustomRepository;

    /**
     * 提交企业定制需求
     * 
     * @param enterpriseCustom 企业定制信息
     * @return 提交结果
     */
    @PostMapping("/submit")
    @Operation(summary = "提交企业定制需求", description = "提交企业定制需求信息")
    public Result<EnterpriseCustom> submit(@RequestBody EnterpriseCustom enterpriseCustom) {
        enterpriseCustom.setCreateTime(LocalDateTime.now());
        enterpriseCustom.setStatus(0);
        EnterpriseCustom saved = enterpriseCustomRepository.save(enterpriseCustom);
        return Result.success(saved);
    }

    /**
     * 获取企业定制列表
     * 
     * @return 企业定制列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取企业定制列表", description = "获取企业定制需求列表")
    public Result<?> list() {
        return Result.success(enterpriseCustomRepository.findAll());
    }

    /**
     * 获取企业定制详情
     * 
     * @param id 定制ID
     * @return 企业定制详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取企业定制详情", description = "根据ID获取企业定制详情")
    public Result<?> detail(@PathVariable Long id) {
        return enterpriseCustomRepository.findById(id)
                .map(Result::success)
                .orElse(Result.error("企业定制信息不存在"));
    }

    /**
     * 更新企业定制状态
     * 
     * @param id 定制ID
     * @param status 状态
     * @return 更新结果
     */
    @PostMapping("/status/{id}")
    @Operation(summary = "更新企业定制状态", description = "更新企业定制需求状态")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        return enterpriseCustomRepository.findById(id)
                .map(custom -> {
                    custom.setStatus(status);
                    enterpriseCustomRepository.save(custom);
                    return Result.success(custom);
                })
                .orElse(Result.error("企业定制信息不存在"));
    }
}
