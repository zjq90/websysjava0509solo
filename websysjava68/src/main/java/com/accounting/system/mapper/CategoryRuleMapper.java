package com.accounting.system.mapper;

import com.accounting.system.entity.CategoryRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 分类规则Mapper接口
 */
@Mapper
public interface CategoryRuleMapper extends BaseMapper<CategoryRule> {

    /**
     * 查询所有启用的规则（按优先级降序排列）
     *
     * @return 规则列表
     */
    @Select("SELECT * FROM category_rule WHERE is_enabled = 1 AND deleted = 0 ORDER BY priority DESC")
    List<CategoryRule> selectEnabledRules();
}
