package com.traceability.controller;

import com.traceability.common.Result;
import com.traceability.entity.Packaging;
import com.traceability.service.PackagingService;
import com.traceability.util.QrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 包装REST API控制器
 * 实现"一袋一码"功能
 */
@RestController
@RequestMapping("/api/packaging")
public class PackagingController {

    @Autowired
    private PackagingService packagingService;

    /**
     * 获取所有包装记录（分页）
     */
    @GetMapping
    public Result<Page<Packaging>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Packaging> pageResult = packagingService.findAll(pageable);
        return Result.success(pageResult);
    }

    /**
     * 获取所有包装记录（不分页，用于下拉列表）
     */
    @GetMapping("/all")
    public Result<List<Packaging>> listAll() {
        return Result.success(packagingService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Packaging> getById(@PathVariable Long id) {
        Optional<Packaging> packaging = packagingService.findById(id);
        return packaging.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    @GetMapping("/batch-no/{batchNo}")
    public Result<List<Packaging>> getByBatchNo(@PathVariable String batchNo) {
        return Result.success(packagingService.findByBatchNo(batchNo));
    }

    @GetMapping("/package-no/{packageNo}")
    public Result<Packaging> getByPackageNo(@PathVariable String packageNo) {
        Optional<Packaging> packaging = packagingService.findByPackageNo(packageNo);
        return packaging.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    @GetMapping("/qr-code/{qrCode}")
    public Result<Packaging> getByQrCode(@PathVariable String qrCode) {
        Optional<Packaging> packaging = packagingService.findByQrCode(qrCode);
        return packaging.map(Result::success).orElseGet(() -> Result.error("数据不存在"));
    }

    /**
     * 生成二维码图片（Base64格式）
     */
    @GetMapping("/generate-qr/{content}")
    public Result<String> generateQrCode(@PathVariable String content) {
        String qrImage = QrCodeUtil.generateQrCodeBase64(content);
        if (qrImage != null) {
            return Result.success(qrImage);
        }
        return Result.error("二维码生成失败");
    }

    /**
     * 生成二维码图片（直接返回图片）
     */
    @GetMapping(value = "/qr-image/{content}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getQrImage(@PathVariable String content) {
        return QrCodeUtil.generateQrCodeBytes(content, 200, 200);
    }

    @PostMapping
    public Result<Packaging> add(@RequestBody Packaging packaging) {
        Packaging saved = packagingService.save(packaging);
        return Result.success("新增成功", saved);
    }

    @PutMapping
    public Result<Packaging> update(@RequestBody Packaging packaging) {
        if (packaging.getId() == null) {
            return Result.error("ID不能为空");
        }
        Packaging updated = packagingService.update(packaging);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        packagingService.deleteById(id);
        return Result.success();
    }
}
