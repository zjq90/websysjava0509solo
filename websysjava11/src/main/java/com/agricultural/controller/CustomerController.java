package com.agricultural.controller;

import com.agricultural.entity.Customer;
import com.agricultural.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 客户/供应商管理控制器
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String type,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int size,
                       Model model) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Customer> customerPage;
        if (type != null && !type.isEmpty()) {
            model.addAttribute("customers", customerService.findByType(type));
            model.addAttribute("isFiltered", true);
        } else {
            customerPage = customerService.findAll(pageRequest);
            model.addAttribute("customers", customerPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", customerPage.getTotalPages());
            model.addAttribute("totalItems", customerPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("isFiltered", false);
        }
        model.addAttribute("selectedType", type);
        return "customer/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            attributes.addFlashAttribute("error", "客户不存在");
            return "redirect:/customer";
        }
        model.addAttribute("customer", customer.get());
        return "customer/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Customer customer, RedirectAttributes attributes) {
        try {
            customerService.save(customer);
            attributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/customer";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            customerService.deleteById(id);
            attributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/customer";
    }
}
