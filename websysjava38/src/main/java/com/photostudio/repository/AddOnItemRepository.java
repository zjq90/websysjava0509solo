package com.photostudio.repository;

import com.photostudio.entity.AddOnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 加购项数据访问接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface AddOnItemRepository extends JpaRepository<AddOnItem, Long>, JpaSpecificationExecutor<AddOnItem> {

    /**
     * 根据类别查询
     */
    List<AddOnItem> findByCategory(String category);

    /**
     * 查询可用的加购项
     */
    List<AddOnItem> findByStatusOrderBySortOrderAsc(Integer status);
}
