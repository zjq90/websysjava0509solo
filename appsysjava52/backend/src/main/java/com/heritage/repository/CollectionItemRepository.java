package com.heritage.repository;

import com.heritage.entity.CollectionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收藏项Repository
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long> {

    /**
     * 根据用户ID查询收藏
     */
    List<CollectionItem> findByUserId(Long userId);

    /**
     * 根据标签模糊查询
     */
    List<CollectionItem> findByUserIdAndTagsContaining(Long userId, String tag);

    /**
     * 根据类别查询
     */
    List<CollectionItem> findByUserIdAndCategory(Long userId, Integer category);

    /**
     * 查询启用环境监测的收藏
     */
    List<CollectionItem> findByEnvMonitorEnabled(Integer enabled);
}