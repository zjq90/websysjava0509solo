package com.production.controller;

import com.production.entity.*;
import com.production.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 工艺参数控制器
 */
@Controller
@RequestMapping("/parameters")
public class ProcessParameterController {

    @Autowired
    private ProcessParameterService processParameterService;

    @Autowired
    private ProcessingRecordService processingRecordService;

    /**
     * 参数列表页面
     */
    @GetMapping
    public String list(Model model, 
                       @RequestParam(required = false) Long recordId,
                       @RequestParam(defaultValue = "0") int page, 
                       @RequestParam(defaultValue = "10") int size) {
        Page<ProcessParameter> parameters = processParameterService.findByRecordIdPage(recordId, page, size);
        model.addAttribute("parameters", parameters.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", parameters.getTotalPages());
        model.addAttribute("totalElements", parameters.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("records", processingRecordService.findAll());
        model.addAttribute("currentRecordId", recordId);
        return "parameter/list";
    }

    /**
     * 新增参数页面
     */
    @GetMapping("/add")
    public String addForm(Model model, @RequestParam(required = false) Long recordId) {
        ProcessParameter parameter = new ProcessParameter();
        if (recordId != null) {
            Optional<ProcessingRecord> recordOpt = processingRecordService.findById(recordId);
            recordOpt.ifPresent(parameter::setProcessingRecord);
        }
        model.addAttribute("parameter", parameter);
        model.addAttribute("records", processingRecordService.findAll());
        return "parameter/form";
    }

    /**
     * 编辑参数页面
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProcessParameter> parameterOpt = processParameterService.findById(id);
        if (parameterOpt.isPresent()) {
            model.addAttribute("parameter", parameterOpt.get());
            model.addAttribute("records", processingRecordService.findAll());
            return "parameter/form";
        }
        redirectAttributes.addFlashAttribute("error", "参数记录不存在");
        return "redirect:/parameters";
    }

    /**
     * 保存参数
     */
    @PostMapping("/save")
    public String save(@ModelAttribute ProcessParameter parameter,
                       @RequestParam Long recordId,
                       RedirectAttributes redirectAttributes) {
        try {
            Optional<ProcessingRecord> recordOpt = processingRecordService.findById(recordId);
            recordOpt.ifPresent(parameter::setProcessingRecord);
            processParameterService.save(parameter);
            redirectAttributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/parameters";
    }

    /**
     * 删除参数
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            processParameterService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/parameters";
    }
}