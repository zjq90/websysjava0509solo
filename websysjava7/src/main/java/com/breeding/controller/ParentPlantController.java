package com.breeding.controller;

import com.breeding.entity.ParentPlant;
import com.breeding.service.ParentPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 亲本植物控制器
 */
@Controller
@RequestMapping("/parents")
public class ParentPlantController {

    @Autowired
    private ParentPlantService parentService;

    @GetMapping
    public String listPage(Model model, 
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<ParentPlant> parentPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<ParentPlant> allResults = parentService.search(keyword);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<ParentPlant> pageContent = start > allResults.size() ? 
                new ArrayList<>() : allResults.subList(start, end);
            parentPage = new PageImpl<>(pageContent, pageable, allResults.size());
        } else {
            parentPage = parentService.findAll(pageable);
        }
        model.addAttribute("page", parentPage);
        model.addAttribute("parents", parentPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", parentPage.getTotalPages());
        model.addAttribute("totalItems", parentPage.getTotalElements());
        return "parent/list";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("parent", new ParentPlant());
        return "parent/form";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        return parentService.findById(id)
                .map(parent -> {
                    model.addAttribute("parent", parent);
                    return "parent/form";
                })
                .orElse("redirect:/parents");
    }

    @GetMapping("/api")
    @ResponseBody
    public List<ParentPlant> getAll() {
        return parentService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ParentPlant> getById(@PathVariable Long id) {
        return parentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody ParentPlant parent) {
        try {
            ParentPlant saved = parentService.save(parent);
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

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ParentPlant parent) {
        try {
            ParentPlant updated = parentService.update(id, parent);
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

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = parentService.delete(id);
        Map<String, Object> result = new HashMap<>();
        if (deleted) {
            result.put("success", true);
            result.put("message", "删除成功");
            return ResponseEntity.ok(result);
        }
        result.put("success", false);
        result.put("message", "记录不存在");
        return ResponseEntity.notFound().build();
    }
}
