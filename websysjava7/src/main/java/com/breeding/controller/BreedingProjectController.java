package com.breeding.controller;

import com.breeding.entity.BreedingProject;
import com.breeding.service.BreedingProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 育种项目控制器
 * 处理育种项目的页面展示和REST API接口
 */
@Controller
@RequestMapping("/projects")
public class BreedingProjectController {

    @Autowired
    private BreedingProjectService projectService;

    /**
     * 页面路由：项目列表页
     */
    @GetMapping
    public String listPage(Model model, 
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<BreedingProject> projectPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<BreedingProject> allResults = projectService.search(keyword);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<BreedingProject> pageContent = start > allResults.size() ? 
                new java.util.ArrayList<>() : allResults.subList(start, end);
            projectPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, allResults.size());
        } else {
            projectPage = projectService.findAll(pageable);
        }
        model.addAttribute("page", projectPage);
        model.addAttribute("projects", projectPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", projectPage.getTotalPages());
        model.addAttribute("totalItems", projectPage.getTotalElements());
        return "project/list";
    }

    /**
     * 页面路由：新增项目页
     */
    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("project", new BreedingProject());
        return "project/form";
    }

    /**
     * 页面路由：编辑项目页
     */
    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        return projectService.findById(id)
                .map(project -> {
                    model.addAttribute("project", project);
                    return "project/form";
                })
                .orElse("redirect:/projects");
    }

    /**
     * REST API：获取所有项目
     */
    @GetMapping("/api")
    @ResponseBody
    public List<BreedingProject> getAll() {
        return projectService.findAll();
    }

    /**
     * REST API：根据ID获取项目
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<BreedingProject> getById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * REST API：创建项目
     */
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody BreedingProject project) {
        try {
            BreedingProject saved = projectService.save(project);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", saved);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * REST API：更新项目
     */
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BreedingProject project) {
        try {
            BreedingProject updated = projectService.update(id, project);
            if (updated != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("data", updated);
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * REST API：删除项目
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = projectService.delete(id);
        Map<String, Object> result = new HashMap<>();
        if (deleted) {
            result.put("success", true);
            result.put("message", "删除成功");
            return ResponseEntity.ok(result);
        }
        result.put("success", false);
        result.put("message", "项目不存在");
        return ResponseEntity.notFound().build();
    }
}
