package com.websys.service;

import com.websys.entity.Menu;
import com.websys.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 菜单服务类
 * 提供菜单管理的业务逻辑，包括增删改查等操作
 */
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    /**
     * 查询所有菜单
     * @return 菜单列表
     */
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    /**
     * 查询一级菜单
     * @return 菜单列表
     */
    public List<Menu> findRootMenus() {
        return menuRepository.findByParentIdIsNullOrderBySortOrder();
    }

    /**
     * 查询子菜单
     * @param parentId 父菜单ID
     * @return 菜单列表
     */
    public List<Menu> findChildMenus(Long parentId) {
        return menuRepository.findByParentIdOrderBySortOrder(parentId);
    }

    /**
     * 根据ID查询菜单
     * @param id 菜单ID
     * @return 菜单对象
     */
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    /**
     * 保存菜单（新增/更新）
     * @param menu 菜单对象
     * @return 保存后的菜单对象
     */
    @Transactional
    public Menu save(Menu menu) {
        if (menu.getId() != null) {
            menu.setUpdateTime(LocalDateTime.now());
        }
        return menuRepository.save(menu);
    }

    /**
     * 删除菜单
     * @param id 菜单ID
     */
    @Transactional
    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    /**
     * 根据类型查询菜单
     * @param type 菜单类型
     * @return 菜单列表
     */
    public List<Menu> findByType(Integer type) {
        return menuRepository.findByTypeOrderBySortOrder(type);
    }
}
