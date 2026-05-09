package com.management.controller;

import com.management.entity.Customer;
import com.management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 客户控制器
 * 提供客户的增删改查功能
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("customers", customerService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "customer/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Customer> customerOpt = customerService.findById(id);
        if (customerOpt.isPresent()) {
            model.addAttribute("customer", customerOpt.get());
            return "customer/form";
        }
        return "redirect:/customers";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        boolean isNew = customer.getId() == null;
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", isNew ? "客户添加成功！" : "客户修改成功！");
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        customerService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "客户删除成功！");
        return "redirect:/customers";
    }
}
