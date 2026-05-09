package com.sales.controller;

import com.sales.entity.*;
import com.sales.service.CustomerService;
import com.sales.service.PriceStrategyService;
import com.sales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 价格策略管理控制器
 * 处理销售价格策略管理：不同客户不同报价、区域定价
 */
@Controller
@RequestMapping("/price-strategies")
public class PriceStrategyController {

    @Autowired
    private PriceStrategyService priceStrategyService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    /**
     * 价格策略列表页面
     */
    @GetMapping
    public String list(Model model,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PriceStrategy> strategyPage = priceStrategyService.findAll(pageable);

        model.addAttribute("strategies", strategyPage.getContent());
        model.addAttribute("page", strategyPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "pricing/list";
    }

    /**
     * 新建价格策略页面
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("strategy", new PriceStrategy());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("customerTypes", CustomerType.values());
        model.addAttribute("customerLevels", CustomerLevel.values());
        model.addAttribute("priceTypes", PriceType.values());
        return "pricing/form";
    }

    /**
     * 保存价格策略
     */
    @PostMapping
    public String save(@ModelAttribute PriceStrategy strategy, RedirectAttributes redirectAttributes) {
        try {
            priceStrategyService.save(strategy);
            redirectAttributes.addFlashAttribute("success", "价格策略保存成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/price-strategies";
    }

    /**
     * 编辑价格策略页面
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PriceStrategy> strategy = priceStrategyService.findById(id);
        if (strategy.isPresent()) {
            model.addAttribute("strategy", strategy.get());
            model.addAttribute("products", productService.findAll());
            model.addAttribute("customers", customerService.findAll());
            model.addAttribute("customerTypes", CustomerType.values());
            model.addAttribute("customerLevels", CustomerLevel.values());
            model.addAttribute("priceTypes", PriceType.values());
            return "pricing/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "价格策略不存在");
            return "redirect:/price-strategies";
        }
    }

    /**
     * 查看价格策略详情
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PriceStrategy> strategy = priceStrategyService.findById(id);
        if (strategy.isPresent()) {
            model.addAttribute("strategy", strategy.get());
            return "pricing/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "价格策略不存在");
            return "redirect:/price-strategies";
        }
    }

    /**
     * 删除价格策略
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            priceStrategyService.delete(id);
            redirectAttributes.addFlashAttribute("success", "价格策略删除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/price-strategies";
    }

    /**
     * 价格计算测试页面
     */
    @GetMapping("/calculate")
    public String calculateForm(Model model) {
        model.addAttribute("customers", customerService.findAllActive());
        model.addAttribute("products", productService.findAllActive());
        return "pricing/calculate";
    }

    /**
     * 执行价格计算
     */
    @PostMapping("/calculate")
    public String calculatePrice(@RequestParam Long customerId,
                                  @RequestParam Long productId,
                                  @RequestParam Integer quantity,
                                  Model model) {
        Optional<Customer> customer = customerService.findById(customerId);
        Optional<Product> product = productService.findById(productId);

        if (customer.isPresent() && product.isPresent()) {
            BigDecimal calculatedPrice = priceStrategyService.calculatePrice(
                    product.get(), customer.get(), quantity);
            model.addAttribute("customer", customer.get());
            model.addAttribute("product", product.get());
            model.addAttribute("quantity", quantity);
            model.addAttribute("basePrice", product.get().getBasePrice());
            model.addAttribute("calculatedPrice", calculatedPrice);
            model.addAttribute("totalAmount", calculatedPrice.multiply(new BigDecimal(quantity)));
        }

        model.addAttribute("customers", customerService.findAllActive());
        model.addAttribute("products", productService.findAllActive());
        return "pricing/calculate";
    }
}
