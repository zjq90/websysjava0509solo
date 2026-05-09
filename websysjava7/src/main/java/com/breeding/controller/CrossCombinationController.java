package com.breeding.controller;

import com.breeding.entity.CrossCombination;
import com.breeding.service.CrossCombinationService;
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
 * 杂交组合控制器
 */
@Controller
@RequestMapping("/combinations")
public class CrossCombinationController {

    @Autowired
    private CrossCombinationService combinationService;

    @GetMapping
    public String listPage(Model model, 
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<CrossCombination> combinationPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<CrossCombination> allResults = combinationService.search(keyword);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<CrossCombination> pageContent = start > allResults.size() ? 
                new ArrayList<>() : allResults.subList(start, end);
            combinationPage = new PageImpl<>(pageContent, pageable, allResults.size());
        } else {
            combinationPage = combinationService.findAll(pageable);
        }
        model.addAttribute("page", combinationPage);
        model.addAttribute("combinations", combinationPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", combinationPage.getTotalPages());
        model.addAttribute("totalItems", combinationPage.getTotalElements());
        return "combination/list";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("combination", new CrossCombination());
        return "combination/form";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        return combinationService.findById(id)
                .map(combination -> {
                    model.addAttribute("combination", combination);
                    return "combination/form";
                })
                .orElse("redirect:/combinations");
    }

    @GetMapping("/api")
    @ResponseBody
    public List<CrossCombination> getAll() {
        return combinationService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<CrossCombination> getById(@PathVariable Long id) {
        return combinationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody CrossCombination combination) {
        try {
            CrossCombination saved = combinationService.save(combination);
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CrossCombination combination) {
        try {
            CrossCombination updated = combinationService.update(id, combination);
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
        boolean deleted = combinationService.delete(id);
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
