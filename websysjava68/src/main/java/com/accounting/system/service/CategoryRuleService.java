package com.accounting.system.service;

import com.accounting.system.dto.CategoryRuleDTO;
import com.accounting.system.entity.CategoryRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 分类规则Service接口
 */
public interface CategoryRuleService extends IService<CategoryRule> {

    /**
     * 查询所有规则列表
     *
     * @return 规则列表
     */
    List<CategoryRule> listAll();

    /**
     * 查询所有启用的规则（按优先级降序排列）
     *
     * @return 启用的规则列表
     */
    List<CategoryRule> listEnabledRules();

    /**
     * 根据ID获取规则详情
     *
     * @param id 规则ID
     * @return 规则详情
     */
    CategoryRule getDetailById(Long id);

    /**
     * 新增规则
     *
     * @param dto 规则请求DTO
     * @return 新增的规则
     */
    CategoryRule addRule(CategoryRuleDTO dto);

    /**
     * 更新规则
     *
     * @param dto 规则请求DTO
     * @return 更新后的规则
     */
    CategoryRule updateRule(CategoryRuleDTO dto);

    /**
     * 删除规则
     *
     * @param id 规则ID
     */
    void deleteRule(Long id);

    /**
     * 测试规则匹配
     *
     * @param dto 规则请求DTO（包含要测试的匹配值）
     * @return 是否匹配
     */
    boolean testRule(CategoryRuleDTO dto);
}
