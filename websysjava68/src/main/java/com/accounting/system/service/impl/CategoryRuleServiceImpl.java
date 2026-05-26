package com.accounting.system.service.impl;

import com.accounting.system.common.BusinessException;
import com.accounting.system.common.ResultCode;
import com.accounting.system.dto.CategoryRuleDTO;
import com.accounting.system.entity.CategoryRule;
import com.accounting.system.mapper.CategoryRuleMapper;
import com.accounting.system.service.CategoryRuleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 分类规则Service实现类
 */
@Service
public class CategoryRuleServiceImpl extends ServiceImpl<CategoryRuleMapper, CategoryRule>
        implements CategoryRuleService {

    @Override
    public List<CategoryRule> listAll() {
        LambdaQueryWrapper<CategoryRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CategoryRule::getPriority)
                .orderByDesc(CategoryRule::getCreateTime);
        return list(wrapper);
    }

    @Override
    public List<CategoryRule> listEnabledRules() {
        return baseMapper.selectEnabledRules();
    }

    @Override
    public CategoryRule getDetailById(Long id) {
        CategoryRule rule = getById(id);
        if (rule == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryRule addRule(CategoryRuleDTO dto) {
        // 如果是正则表达式，验证语法是否正确
        if ("REGEX".equals(dto.getMatchType())) {
            try {
                Pattern.compile(dto.getMatchValue());
            } catch (PatternSyntaxException e) {
                throw new BusinessException("正则表达式语法错误：" + e.getMessage());
            }
        }

        CategoryRule rule = new CategoryRule();
        BeanUtils.copyProperties(dto, rule);
        if (rule.getPriority() == null) {
            rule.setPriority(0);
        }
        if (rule.getIsEnabled() == null) {
            rule.setIsEnabled(1);
        }
        save(rule);
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryRule updateRule(CategoryRuleDTO dto) {
        CategoryRule existRule = getById(dto.getId());
        if (existRule == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        // 如果是正则表达式，验证语法是否正确
        if ("REGEX".equals(dto.getMatchType())) {
            try {
                Pattern.compile(dto.getMatchValue());
            } catch (PatternSyntaxException e) {
                throw new BusinessException("正则表达式语法错误：" + e.getMessage());
            }
        }

        BeanUtils.copyProperties(dto, existRule);
        updateById(existRule);
        return existRule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(Long id) {
        CategoryRule existRule = getById(id);
        if (existRule == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        removeById(id);
    }

    @Override
    public boolean testRule(CategoryRuleDTO dto) {
        String testValue = dto.getMatchValue();
        String matchType = dto.getMatchType();

        // 使用描述字段作为测试值
        String testContent = dto.getRemark();
        if (testContent == null || testContent.isEmpty()) {
            return false;
        }

        switch (matchType) {
            case "CONTAINS":
                return testContent.contains(testValue);
            case "EQUALS":
                return testContent.equals(testValue);
            case "REGEX":
                try {
                    return Pattern.matches(testValue, testContent);
                } catch (PatternSyntaxException e) {
                    throw new BusinessException("正则表达式语法错误：" + e.getMessage());
                }
            default:
                return false;
        }
    }
}
