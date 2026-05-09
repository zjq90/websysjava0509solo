package com.inventory.repository;

import com.inventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门店Repository接口
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * 根据状态查询门店列表
     */
    List<Store> findByStatus(Integer status);

    /**
     * 根据名称模糊查询
     */
    List<Store> findByNameContaining(String name);

    /**
     * 根据编码查询
     */
    Store findByCode(String code);

    /**
     * 根据门店类型查询
     */
    List<Store> findByType(Integer type);

    /**
     * 根据区域查询
     */
    List<Store> findByRegion(String region);

    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查编码是否存在
     */
    boolean existsByCode(String code);
}
