package com.photostudio.controller;

import com.photostudio.entity.Customer;
import com.photostudio.entity.Tag;
import com.photostudio.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 标签管理控制器
 * 提供标签相关的REST API
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签管理", description = "客户标签的增删改查")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 创建标签
     */
    @PostMapping
    @Operation(summary = "创建标签", description = "创建新的客户标签")
    public ResponseEntity<Tag> createTag(@Valid @RequestBody Tag tag) {
        Tag createdTag = tagService.createTag(tag);
        return ResponseEntity.ok(createdTag);
    }

    /**
     * 更新标签
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新标签", description = "根据ID更新标签信息")
    public ResponseEntity<Tag> updateTag(
            @Parameter(description = "标签ID") @PathVariable Long id,
            @Valid @RequestBody Tag tagDetails) {
        Tag updatedTag = tagService.updateTag(id, tagDetails);
        return ResponseEntity.ok(updatedTag);
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签", description = "根据ID删除标签（软删除）")
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "标签ID") @PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据ID查询标签
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询标签", description = "根据ID查询标签信息")
    public ResponseEntity<Tag> getTagById(
            @Parameter(description = "标签ID") @PathVariable Long id) {
        return tagService.getTagById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 查询所有标签
     */
    @GetMapping
    @Operation(summary = "查询所有标签", description = "获取所有标签列表")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    /**
     * 为客户添加标签
     */
    @PostMapping("/{tagId}/customers/{customerId}")
    @Operation(summary = "添加客户标签", description = "为指定客户添加标签")
    public ResponseEntity<Customer> addTagToCustomer(
            @Parameter(description = "标签ID") @PathVariable Long tagId,
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        Customer customer = tagService.addTagToCustomer(customerId, tagId);
        return ResponseEntity.ok(customer);
    }

    /**
     * 移除客户标签
     */
    @DeleteMapping("/{tagId}/customers/{customerId}")
    @Operation(summary = "移除客户标签", description = "移除指定客户的标签")
    public ResponseEntity<Customer> removeTagFromCustomer(
            @Parameter(description = "标签ID") @PathVariable Long tagId,
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        Customer customer = tagService.removeTagFromCustomer(customerId, tagId);
        return ResponseEntity.ok(customer);
    }
}
