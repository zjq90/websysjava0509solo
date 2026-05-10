package com.websys.repository;

import com.websys.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单数据访问接口
 * 提供菜单的增删改查操作
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

    /**
     * 根据父菜单ID查找子菜单
     * @param parentId 父菜单ID
     * @return 菜单列表
     */
    List<Menu> findByParentIdOrderBySortOrder(Long parentId);

    /**
     * 查找一级菜单（parentId为null或0）
     * @return 菜单列表
     */
    List<Menu> findByParentIdIsNullOrderBySortOrder();

    /**
     * 根据菜单类型查找
     * @param type 菜单类型
     * @return 菜单列表
     */
    List<Menu> findByTypeOrderBySortOrder(Integer type);
}
