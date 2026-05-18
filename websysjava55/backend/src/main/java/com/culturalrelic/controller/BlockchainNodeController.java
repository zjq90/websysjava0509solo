package com.culturalrelic.controller;

import com.culturalrelic.common.Result;
import com.culturalrelic.entity.BlockchainNode;
import com.culturalrelic.service.BlockchainNodeService;
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
 * 区块链节点管理控制器
 */
@RestController
@RequestMapping("/blockchain-node")
@Tag(name = "区块链节点管理", description = "区块链节点的增删改查，支持高可用跨可用区部署")
public class BlockchainNodeController {

    @Autowired
    private BlockchainNodeService blockchainNodeService;

    /**
     * 新增节点
     */
    @PostMapping
    @Operation(summary = "新增节点", description = "添加新的区块链节点")
    public Result<BlockchainNode> save(@RequestBody BlockchainNode node) {
        BlockchainNode saved = blockchainNodeService.save(node);
        return Result.success("新增成功", saved);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询节点详情", description = "根据ID查询节点详细信息")
    public Result<BlockchainNode> findById(@Parameter(description = "节点ID") @PathVariable Long id) {
        Optional<BlockchainNode> node = blockchainNodeService.findById(id);
        if (node.isPresent()) {
            return Result.success(node.get());
        }
        return Result.error("节点不存在");
    }

    /**
     * 分页查询所有
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询节点", description = "分页查询所有节点列表")
    public Result<Page<BlockchainNode>> findAll(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<BlockchainNode> result = blockchainNodeService.findAll(pageable);
        return Result.success(result);
    }

    /**
     * 查询所有
     */
    @GetMapping
    @Operation(summary = "查询所有节点", description = "获取所有节点列表")
    public Result<List<BlockchainNode>> findAll() {
        List<BlockchainNode> list = blockchainNodeService.findAll();
        return Result.success(list);
    }

    /**
     * 更新节点
     */
    @PutMapping
    @Operation(summary = "更新节点", description = "更新节点信息")
    public Result<BlockchainNode> update(@RequestBody BlockchainNode node) {
        BlockchainNode updated = blockchainNodeService.update(node);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除节点
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除节点", description = "根据ID删除节点（逻辑删除）")
    public Result<Void> delete(@Parameter(description = "节点ID") @PathVariable Long id) {
        boolean success = blockchainNodeService.delete(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 根据可用区查询
     */
    @GetMapping("/zone/{zone}")
    @Operation(summary = "按可用区查询", description = "根据可用区查询节点列表（跨可用区高可用部署）")
    public Result<List<BlockchainNode>> findByAvailabilityZone(@Parameter(description = "可用区") @PathVariable String zone) {
        List<BlockchainNode> list = blockchainNodeService.findByAvailabilityZone(zone);
        return Result.success(list);
    }

    /**
     * 查询启用的节点
     */
    @GetMapping("/enabled")
    @Operation(summary = "查询启用节点", description = "获取所有已启用的节点列表")
    public Result<List<BlockchainNode>> findByEnabled() {
        List<BlockchainNode> list = blockchainNodeService.findByEnabled(1);
        return Result.success(list);
    }
}
