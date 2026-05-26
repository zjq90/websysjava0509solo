package com.accounting.service;

import com.accounting.entity.BillTemplate;
import com.accounting.entity.Category;
import com.accounting.entity.Account;
import com.accounting.repository.BillTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillTemplateService {

    private final BillTemplateRepository billTemplateRepository;
    private final CategoryService categoryService;
    private final AccountService accountService;

    @Cacheable(value = "templates", key = "'all'")
    public List<BillTemplate> getAllTemplates() {
        log.info("获取全部账单模板");
        return billTemplateRepository.findAllByOrderByUseCountDesc();
    }

    public BillTemplate getTemplateById(Long id) {
        log.info("根据ID获取模板: {}", id);
        return billTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("模板不存在: " + id));
    }

    @Transactional
    @CacheEvict(value = "templates", allEntries = true)
    public BillTemplate createTemplate(BillTemplate template) {
        log.info("创建模板: {}", template.getName());
        template.setId(null);
        if (template.getUseCount() == null) {
            template.setUseCount(0);
        }

        Category category = categoryService.getCategoryById(template.getCategory().getId());
        Account account = accountService.getAccountById(template.getAccount().getId());
        template.setCategory(category);
        template.setAccount(account);

        return billTemplateRepository.save(template);
    }

    @Transactional
    @CacheEvict(value = "templates", allEntries = true)
    public BillTemplate updateTemplate(Long id, BillTemplate template) {
        log.info("更新模板: {}", id);
        BillTemplate existing = getTemplateById(id);
        existing.setName(template.getName());
        existing.setType(template.getType());
        existing.setAmount(template.getAmount());
        existing.setMerchant(template.getMerchant());
        existing.setRemark(template.getRemark());
        existing.setSortOrder(template.getSortOrder());

        if (template.getCategory() != null && template.getCategory().getId() != null) {
            Category category = categoryService.getCategoryById(template.getCategory().getId());
            existing.setCategory(category);
        }
        if (template.getAccount() != null && template.getAccount().getId() != null) {
            Account account = accountService.getAccountById(template.getAccount().getId());
            existing.setAccount(account);
        }

        return billTemplateRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "templates", allEntries = true)
    public BillTemplate incrementUseCount(Long id) {
        log.info("增加模板使用次数: {}", id);
        BillTemplate template = getTemplateById(id);
        template.setUseCount(template.getUseCount() + 1);
        return billTemplateRepository.save(template);
    }

    @Transactional
    @CacheEvict(value = "templates", allEntries = true)
    public void deleteTemplate(Long id) {
        log.info("删除模板: {}", id);
        if (!billTemplateRepository.existsById(id)) {
            throw new IllegalArgumentException("模板不存在: " + id);
        }
        billTemplateRepository.deleteById(id);
    }
}
